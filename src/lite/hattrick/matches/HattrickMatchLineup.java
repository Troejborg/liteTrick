
	/**
	 * File : HattrickMatchLineup.java
	 * iPinion
	 * Created by : Ronnie Tr¿jborg
	 * Date : 27/06/2012
	 * Copyright (c) 2012 iPinion. All rights reserved.
	 */

package lite.hattrick.matches;

import java.io.IOException;

import lite.hattrick.players.PlayerRole;
import lite.hattrick.requests.HattrickConnector;
import lite.hattrick.requests.XMLManager;
import lite.hattrick.services.HattrickHelper;
import lite.hattrick.services.HattrickSkillLevel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

public class HattrickMatchLineup 
{
	private int matchID, teamID, awayTeamID;
	private String homeTeamName, awayTeamName, arenaName;
	private HattrickConnector connector;
	private transient Element matchLineup;
	private MatchType matchType;
	private HattrickSkillLevel teamExperienceLevel;
	private LineupPlayers[] lineupPlayers;
	
	public HattrickMatchLineup(int matchID, int teamID) 
	{
		this.matchID = matchID;
		this.teamID = teamID;
		connector = HattrickConnector.getInstance();
		
		try {
			Document doc = XMLManager.parseString(connector.getMatchLineup(matchID, teamID));
			matchLineup = doc.getDocumentElement();
		} catch (IOException e) {
			Log.e("parsing", "Could not parse match lineup with match id : " + matchID);
			e.printStackTrace();
		}

		matchType = HattrickHelper.getMatchType(Integer.parseInt(HattrickHelper.getStringByTag("MatchType", (Element)matchLineup)));
		
		Element node = (Element) matchLineup.getElementsByTagName("HomeTeam").item(0);
		homeTeamName = HattrickHelper.getStringByTag("HomeTeamName", node);
		
		node = (Element)matchLineup.getElementsByTagName("AwayTeam").item(0);
		awayTeamName = HattrickHelper.getStringByTag("AwayTeamName", node);
		awayTeamID = Integer.parseInt(HattrickHelper.getStringByTag("AwayTeamID", node));
		
		node = (Element)matchLineup.getElementsByTagName("Team").item(0);
		teamExperienceLevel = HattrickHelper.getSkillLevel(Integer.parseInt(HattrickHelper.getStringByTag("ExperienceLevel", node)));
		
		node = (Element)node.getElementsByTagName("Lineup").item(0);
		NodeList players = node.getElementsByTagName("Player");

		lineupPlayers = new LineupPlayers[players.getLength()];
		
		for(int i = 0; i<players.getLength(); i++)
		{
			lineupPlayers[i].PlayerID = Integer.parseInt(HattrickHelper.getStringByTag("PlayerID", (Element)players.item(i)));
			lineupPlayers[i].FirstName = HattrickHelper.getStringByTag("FirstName", (Element)players.item(i));
			lineupPlayers[i].LastName = HattrickHelper.getStringByTag("LastName", (Element)players.item(i));
			lineupPlayers[i].RatingStars = Float.parseFloat(HattrickHelper.getStringByTag("RatingStars", (Element)players.item(i)));
			lineupPlayers[i].RatingStarsEndOfMatch = Float.parseFloat(HattrickHelper.getStringByTag("RatingStarsEndOfMatch", (Element)players.item(i)));
			lineupPlayers[i].Role = HattrickHelper.getPlayerPosition(HattrickHelper.getStringByTag("FirstName", (Element)players.item(i)));
		}
	}
	
	public HattrickSkillLevel getTeamExperienceLevel()
	{
		return teamExperienceLevel;
	}
	
	public LineupPlayers[] getLineup()
	{
		return lineupPlayers;
	}
	
	public class LineupPlayers
	{
		public Integer PlayerID;
		public String FirstName, LastName;
		public Float RatingStars, RatingStarsEndOfMatch;
		public PlayerRole Role;
	}
}
