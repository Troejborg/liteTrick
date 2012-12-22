
	/**
	 * File : FederationsAdapter.java
	 * iPinion
	 * Created by : Ronnie Tr¿jborg
	 * Date : 29/06/2012
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
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FederationsAdapter extends ArrayAdapter<HattrickFederation> 
{
	private SherlockFragmentActivity activity; 
	private List<HattrickFederation> data = null;
	
	public FederationsAdapter(SherlockFragmentActivity activity, List<HattrickFederation> data) {
		super(activity,R.layout.federationranks_item, data);
		this.activity = activity;
		this.data = data;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		FederationHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.federations_item, null);
			holder = new FederationHolder();
			
			holder.federationNameView = (TextView)row.findViewById(R.id.federation_item_name);
			holder.federationMembersView = (TextView)row.findViewById(R.id.federation_item_members);
			
			row.setTag(holder);
		}
		else
			holder = (FederationHolder)row.getTag();
		
		holder.federationNameView.setText(data.get(position).getFederationName());
		holder.federationMembersView.setText("" + data.get(position).getTeams().size());
		
		
		return row;
	}
	
	private static class FederationHolder
	{
		TextView federationNameView;
		TextView federationMembersView;
	}
	
	
	public void setData(List<HattrickFederation> data) 
	{
		this.data = data;
	}
}
