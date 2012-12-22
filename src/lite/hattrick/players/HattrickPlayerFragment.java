package lite.hattrick.players;

import lite.hattrick.main.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class HattrickPlayerFragment extends SherlockFragment 
{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null) {
			return null;
		}
		
		HattrickPlayer player = (HattrickPlayer)savedInstanceState.getSerializable("chosenPlayer");
		
		final TextView playerName = (TextView)getActivity().findViewById(R.id.playerinfo_name);
		playerName.setText(player.getFirstName() + " " + player.getLastName());
		
		final TextView playerTeamName = (TextView)getActivity().findViewById(R.id.playerinfo_team_name);
		playerTeamName.setText(player.getTeamName());
		
		return (LinearLayout)inflater.inflate(R.layout.playerinfo, container, false);
	}
	
	
}
