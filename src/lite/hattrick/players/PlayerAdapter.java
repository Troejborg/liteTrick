
/**
 * File : PlayerAdapter.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 21/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.players;

import java.util.List;

import lite.hattrick.main.R;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class PlayerAdapter extends ArrayAdapter<HattrickPlayer> {
	private SherlockFragmentActivity activity; 
	private List<HattrickPlayer> data = null;

	public PlayerAdapter(SherlockFragmentActivity sherlockFragmentActivity, List<HattrickPlayer> data, int layoutResourceId) {
		super(sherlockFragmentActivity,R.layout.playerranks_item, data);
		this.activity = sherlockFragmentActivity;
		this.data = data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		PlayerHolder holder = null;

		if(row == null)
		{
	LayoutInflater inflater = activity.getLayoutInflater();
	row = inflater.inflate(R.layout.playerranks_item, null);
	holder = new PlayerHolder();
			holder.rankView = (TextView)row.findViewById(R.id.player_item_rank);
			holder.firstNameView = (TextView)row.findViewById(R.id.player_item_FirstName);
			holder.lastNameView = (TextView)row.findViewById(R.id.player_item_LastName);
			holder.teamNameView = (TextView)row.findViewById(R.id.player_item_teamName);
			holder.imgRankView = (ImageView)row.findViewById(R.id.player_item_rankImage);

			row.setTag(holder);
		}
		else
			holder = (PlayerHolder)row.getTag();


		holder.rankView.setText(String.valueOf(position+1));
		holder.firstNameView.setText(data.get(position).getFirstName());
		holder.lastNameView.setText(data.get(position).getLastName());
		holder.teamNameView.setText(data.get(position).getTeamName());
		if(data.get(position).getRankStatus() != null)
		{
			switch(data.get(position).getRankStatus())
			{
			case ITEM_IS_NEW:
				holder.imgRankView.setImageResource(R.drawable.rank_plus_icon);
				break;
			case ITEM_MOVED_UP:
				holder.imgRankView.setImageResource(R.drawable.rank_arrow_up);
				break;
			case ITEM_MOVED_DOWN:
				holder.imgRankView.setImageResource(R.drawable.rank_arrow_down);
				break;
			case ITEM_DID_NOT_MOVE:
				holder.imgRankView.setImageResource(android.R.color.transparent);
			}	
		}
		
		return row;
	}

	private static class PlayerHolder
	{
		TextView rankView;
		TextView firstNameView;
		TextView lastNameView;
		TextView teamNameView;
		ImageView imgRankView;
	}

}
