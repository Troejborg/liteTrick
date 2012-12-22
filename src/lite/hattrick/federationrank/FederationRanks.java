
/**
 * File : FederationRankFragment.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 22/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.federationrank;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import lite.hattrick.main.R;
import lite.hattrick.services.HattrickHelper;
import lite.hattrick.services.RankStatus;
import lite.hattrick.teams.HattrickTeam;
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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class FederationRanks extends SherlockFragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public static class FederationsFragment extends SherlockFragment 
	{
		private final String FILENAME = "FEDERATIONS.dat";
		private ListView listView;
		private List<HattrickFederation> tempList;
		private FragmentTransaction ft;
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			if (container == null) {
				return null;
			}
			return (LinearLayout)inflater.inflate(R.layout.federations, container, false);
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


			listView = (ListView)getActivity().findViewById(R.id.lv_federations);
			listView.setVisibility(View.GONE);
			if (savedInstance == null)
			{
				new PullFederationDataTask().execute();

			} 		

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) 
				{
//					addFragmentToStack(tempList.get(position));
				}
			});
		}

//		void addFragmentToStack(HattrickFederation federation) 
//		{
//			// Instantiate a new fragment.
//			Fragment federationsFragment = FederationRanksFragment.newInstance(federation);
//
//			// Add the fragment to the activity, pushing this transaction
//			// on to the back stack.
//			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//			ft.replace(R.id.simple_fragment, federationsFragment);
//			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//			ft.addToBackStack(null);
//			ft.commit();
//		}

		private class PullFederationDataTask extends AsyncTask<Void, Void, List<HattrickFederation>>
		{
			@SuppressWarnings("unchecked")
			@Override
			protected List<HattrickFederation> doInBackground(Void... params) 
			{

				Integer[] federations = new Integer[4];
				federations[0] = 99063;
				federations[1] = 39201;
				federations[2] = 36520;
				federations[3] = 74248;
				tempList = new ArrayList<HattrickFederation>();

				try {
					tempList = (List<HattrickFederation>)HattrickHelper.load(getSherlockActivity(), FILENAME);
				} catch (FileNotFoundException e) {
					tempList = null;
					e.printStackTrace();
				}
				if(tempList != null && tempList instanceof List<?>)
				{
					return tempList;
				}
				else
				{
					tempList = new ArrayList<HattrickFederation>();
					for(int i = 0; i<federations.length; i++)
						tempList.add(new HattrickFederation(federations[i]));

					HattrickHelper.save(getSherlockActivity(), tempList, FILENAME);
					return tempList;
				}
			}

			protected void onPostExecute(List<HattrickFederation> result) 
			{

				if(result != null)
				{
					listView.setVisibility(View.VISIBLE);
					listView.setAdapter(new FederationsAdapter(getSherlockActivity(), result));
				}

				else
					Toast.makeText(getSherlockActivity(), "Data load failed. Please try again.", Toast.LENGTH_LONG);
			}
		}
	}

	public static class FederationRanksFragment extends SherlockFragment 
	{
		private final String FILENAME = "federationteams.dat";
		static final int POPULATE_ID = Menu.FIRST;
		private int federationID;
		private static final int CANCEL_ID = Menu.FIRST+1;
		private static final int MORE_ID = Menu.FIRST+2;
		private static final int MORE_GROUP = Menu.FIRST+3;
		private ListView actualListView;
		private FederationTeamsAdapter adapter;
		private HattrickFederation federation;


		public FederationRanksFragment(HattrickFederation federation) 
		{
			this.federation = federation;
		}

		static FederationRanksFragment newInstance(HattrickFederation federation) 
		{
			FederationRanksFragment f = new FederationRanksFragment(federation);
			return f;
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			if (container == null) {
				return null;
			}
			return (LinearLayout)inflater.inflate(R.layout.federationranks, container, false);
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

			actualListView = (ListView)getActivity().findViewById(R.id.lv_federationRanks);
			if(savedInstance == null)
			{
				actualListView.setAdapter(new FederationTeamsAdapter(getSherlockActivity(), federation.getTeams()));
			}				


		}

		@Override 
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
		{
			MenuItem populateItem = menu.add(Menu.NONE, POPULATE_ID, 0, "Refresh");
			populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			MenuItem cancelItem = menu.add(Menu.NONE, CANCEL_ID, 0, "Cancel");
			cancelItem.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
			cancelItem.setEnabled(false);
			MenuItem moreItem = menu.add(MORE_GROUP, MORE_ID, 0, "MORE");
			moreItem.setIcon(R.drawable.ic_list_more);
			moreItem.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);

			MenuItem testItem1 = menu.add(MORE_GROUP, MORE_ID+1, 0, "TestItem1");
			MenuItem testItem2 = menu.add(MORE_GROUP, MORE_ID+2, 0, "TestItem2");
			MenuItem testItem3 = menu.add(MORE_GROUP, MORE_ID+3, 0, "TestItem3");
			MenuItem testItem4 = menu.add(MORE_GROUP, MORE_ID+4, 0, "TestItem4");
			testItem1.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
			testItem2.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
			testItem3.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
			testItem4.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);

		}

		private void compareChanges(List<HattrickTeam> oldList,
				List<HattrickTeam> newList) 
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
						if(newList.get(i).getTeamID() == oldList.get(k).getTeamID())
						{
							if(i < k)
							{
								newList.get(i).setRankStatus(RankStatus.ITEM_MOVED_UP);
								Log.d("RankStatus", newList.get(i).getTeamName() + " moved UP!");
								isPlayerFound = true;
							}
							else if(i == k)
							{
								newList.get(i).setRankStatus(RankStatus.ITEM_DID_NOT_MOVE);
								Log.d("RankStatus", newList.get(i).getTeamName() + " did not move");
								isPlayerFound = true;
							}
							else
							{
								newList.get(i).setRankStatus(RankStatus.ITEM_MOVED_DOWN);
								Log.d("RankStatus", newList.get(i).getTeamName() + " moved DOWN!");
								isPlayerFound = true;
							}
						}
					}
					if(!isPlayerFound)
					{
						newList.get(i).setRankStatus(RankStatus.ITEM_IS_NEW);
						Log.d("RankStatus", newList.get(i).getTeamName() + " is NEW!");
					}

				}	
			}

		}
	}
}
