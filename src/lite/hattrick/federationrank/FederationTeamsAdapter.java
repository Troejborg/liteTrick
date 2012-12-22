
/**
 * File : FederationTeamsAdapter.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 22/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.federationrank;

import java.util.List;

import lite.hattrick.main.R;
import lite.hattrick.teams.HattrickTeam;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FederationTeamsAdapter extends ArrayAdapter<HattrickTeam> 
{

	private SherlockFragmentActivity activity; 
	private List<HattrickTeam> data = null;

	public FederationTeamsAdapter(SherlockFragmentActivity sherlockFragmentActivity, List<HattrickTeam> data) {
		super(sherlockFragmentActivity,R.layout.federationranks_item, data);
		this.activity = sherlockFragmentActivity;
		this.data = data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		TeamHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.federationranks_item, null);
			holder = new TeamHolder();
			holder.rankView = (TextView)row.findViewById(R.id.federation_item_rank);
			holder.teamNameView = (TextView)row.findViewById(R.id.federation_item_teamName);
			holder.imgRankView = (ImageView)row.findViewById(R.id.federation_item_rankImage);
			holder.teamRankView = (TextView)row.findViewById(R.id.federation_item_dkRank);

			row.setTag(holder);
		}
		else
			holder = (TeamHolder)row.getTag();


		holder.rankView.setText(String.valueOf(position+1));
		holder.teamNameView.setText(data.get(position).getTeamName());
		holder.teamRankView.setText(String.valueOf(data.get(position).getRank()));
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
			case ITEM_DID_NOT_MOVE:
				holder.imgRankView.setImageResource(android.R.color.transparent);
			}	
		}
		else
			holder.imgRankView.setImageResource(android.R.color.transparent);

		return row;
	}

	private static class TeamHolder
	{
		TextView rankView;
		TextView teamNameView;
		TextView teamRankView;
		ImageView imgRankView;
	}

	public void setData(List<HattrickTeam> data) 
	{
		this.data = data;
	}

}

