
/**
 * File : PlayerRankingFragment.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 21/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.players;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lite.hattrick.main.R;
import lite.hattrick.services.HattrickHelper;
import lite.hattrick.services.MySQLHelper;
import lite.hattrick.services.RankStatus;
import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class PlayerRankingFragment extends SherlockFragment 
{
	private static final int POPULATE_ID = Menu.FIRST;
	private static final int CANCEL_ID = Menu.FIRST+1;
	private final String FILENAME = "playerlist.dat";
	private ListView actualListView;
	private PlayerAdapter adapter;
	private List<HattrickPlayer> loadedList;
	private PullInfoTask pullPlayerDataTask;
	private MenuItem cancelItem;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null) {
			return null;
		}
		return (LinearLayout)inflater.inflate(R.layout.playerranks, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstance)
	{
		super.onActivityCreated(savedInstance);
		setHasOptionsMenu(true);
		actualListView = (ListView)getActivity().findViewById(R.id.lv_playerRanks);
		if(null == savedInstance)
		{
			try {
				loadedList = (List<HattrickPlayer>)HattrickHelper.load(getSherlockActivity(), FILENAME);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(loadedList instanceof List<?>)
			actualListView.setAdapter(new PlayerAdapter(getSherlockActivity(), (List<HattrickPlayer>) loadedList, 0));
		
		actualListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) 
			{
				HattrickPlayerFragment fragment = new HattrickPlayerFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("chosenPlayer", loadedList.get(position));
				fragment.setArguments(bundle);
				
				FragmentTransaction fragmentTransaction = getSherlockActivity().getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.fragmentContainer, fragment);
				fragmentTransaction.commit();				
			}
			
		});
	}

	@Override 
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		MenuItem populateItem = menu.add(Menu.NONE, POPULATE_ID, 0, "Refresh");
		populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		cancelItem = menu.add(Menu.NONE, CANCEL_ID, 0, "Cancel");
		cancelItem.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
		cancelItem.setEnabled(false);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) 
	{
		final ContentResolver cr = getActivity().getContentResolver();

		switch (item.getItemId()) {
		case POPULATE_ID:
			if (pullPlayerDataTask != null) {
				pullPlayerDataTask.cancel(false);
			}
			pullPlayerDataTask = new PullInfoTask();
			pullPlayerDataTask.execute();
			return true;

		case CANCEL_ID:
			if(pullPlayerDataTask != null)
				pullPlayerDataTask.cancel(true);
			pullPlayerDataTask = null;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Class responsible for handling the asynchronous web service call through the UserController class. populates the list of Call forward Items onPostExecute().
	 * @author ronnietrojborg
	 *
	 */
	private class PullInfoTask extends AsyncTask<Void, Void, List<HattrickPlayer>>
	{

		public PullInfoTask()
		{

		}

		@Override
		protected List<HattrickPlayer> doInBackground(Void... arg0) 
		{
			List<HattrickPlayer> hattrickPlayers = new ArrayList<HattrickPlayer>();
			ArrayList<Integer> playerIDs = MySQLHelper.getPlayerIDs();
			for(int i = 0; i<playerIDs.size(); i++)
			{
				HattrickPlayer hattrickPlayer = new HattrickPlayer(playerIDs.get(i));
				hattrickPlayer.getPlayerDetails();
				hattrickPlayers.add(hattrickPlayer);
			}

			return hattrickPlayers;
		}

		protected void onPostExecute(List<HattrickPlayer> result) 
		{
			if(result != null)
			{
				Collections.sort(result);
				Collections.reverse(result);
				adapter = new PlayerAdapter(getSherlockActivity(), result, 0);
				//compareChangesDUMMY((List<HattrickPlayer>)loadedList, result); // TODO : Randomly Generated Rank Changes - Remove.
				compareChanges((List<HattrickPlayer>)loadedList, result);
				actualListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				HattrickHelper.save(getSherlockActivity(), result, FILENAME);
			}
		}

		private void compareChanges(List<HattrickPlayer> oldList,
				List<HattrickPlayer> newList) 
		{
			boolean isPlayerFound;

			if(oldList != null && oldList.size() > 0)
			{
				for(int i = 0; i<newList.size(); i++)
				{
					newList.get(i).setPlayerRank(i);
					isPlayerFound = false;
					for(int k = 0; k<oldList.size(); k++)
					{
						if(newList.get(i).getPlayerID() == oldList.get(k).getPlayerID())
						{
							if(i < k)
							{
								newList.get(i).setRankStatus(RankStatus.ITEM_MOVED_UP);
								isPlayerFound = true;
							}
							else if(i == k)
							{
								newList.get(i).setRankStatus(RankStatus.ITEM_DID_NOT_MOVE);
								isPlayerFound = true;
							}
							else
							{
								newList.get(i).setRankStatus(RankStatus.ITEM_MOVED_DOWN);
								isPlayerFound = true;
							}
						}
					}
					if(!isPlayerFound)
						newList.get(i).setRankStatus(RankStatus.ITEM_IS_NEW);
				}	
			}

		}

		private void compareChangesDUMMY(List<HattrickPlayer> oldList,
				List<HattrickPlayer> newList) 
		{
			Random rndGenerator = new Random();
			for(int i = 0; i<newList.size(); i++)
			{
				newList.get(i).setPlayerRank(i);
				int k = rndGenerator.nextInt(14);

				if(i < k && k < 11)
				{
					newList.get(i).setRankStatus(RankStatus.ITEM_MOVED_UP);
					Log.d("Rank", "Player " + i+ " moved Up");
				}
				else if(i == k)
				{
					newList.get(i).setRankStatus(RankStatus.ITEM_DID_NOT_MOVE);
					Log.d("Rank", "Player " + i +" did not move.");
				}
				else if(i > k && k < 11)
				{
					newList.get(i).setRankStatus(RankStatus.ITEM_MOVED_DOWN);
					Log.d("Rank", "Player " + i+ " moved down.");
				}
				else
				{
					newList.get(i).setRankStatus(RankStatus.ITEM_IS_NEW);
					Log.d("Rank", "Player " + i + " is new.");
				}

			}	
		}
	
	}
}

