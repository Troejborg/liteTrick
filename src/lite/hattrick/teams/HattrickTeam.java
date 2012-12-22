
/**
 * File : HattrickTeamDetails.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 19/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.teams;

import java.io.IOException;
import java.io.Serializable;

import lite.hattrick.requests.HattrickConnector;
import lite.hattrick.requests.XMLManager;
import lite.hattrick.services.HattrickHelper;
import lite.hattrick.services.RankStatus;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

public class HattrickTeam implements Serializable, Comparable<HattrickTeam>
{
	private static final long serialVersionUID = 1L;
	private transient HattrickConnector instance;
	private transient Element teamDetails;
	private String teamName, arenaName, loginName, lastLogin;
	private Integer id, arenaID, teamRank, teamID;
	private RankStatus rankStatus;
	private boolean isTeamID;
	private int rank;

	public HattrickTeam(int id, boolean isTeamID)
	{
		this.id = id;
		this.isTeamID = isTeamID;
		instance = HattrickConnector.getInstance();
		getTeamDetails();
	}


	/**
	 * 
	 * @return HashMap containing basic Team info with the following tags :
	 *  TeamName - Team Name
	 *  TeamID - Team ID
	 *  ArenaName - Arena Name
	 *  ArenaID - Arena ID
	 *  LeagueLevelUnitName - Division Name
	 */
	private void getTeamDetails()
	{

		try {
			Document doc = XMLManager.parseString(instance.getTeamdetails(id, isTeamID));
			teamDetails = doc.getDocumentElement();
		} catch (IOException e) {
			Log.e("parsing", "Could not parse team details with team id : " + id);
			e.printStackTrace();
		}

		Element node = (Element) teamDetails.getElementsByTagName("Team").item(0);

		Element tempNode = node;
		teamName = HattrickHelper.getStringByTag("TeamName", tempNode);
		teamID = Integer.parseInt(HattrickHelper.getStringByTag("TeamID", tempNode));
		teamRank = Integer.parseInt(HattrickHelper.getStringByTag("TeamRank", tempNode));

		tempNode = (Element)node.getElementsByTagName("Arena").item(0);
		arenaName = HattrickHelper.getStringByTag("ArenaName", tempNode);
		arenaID = Integer.parseInt(HattrickHelper.getStringByTag("ArenaID", tempNode));

		tempNode = (Element)teamDetails.getElementsByTagName("User").item(0);
		loginName = HattrickHelper.getStringByTag("Loginname", tempNode);
		lastLogin = HattrickHelper.getStringByTag("LastLoginDate", tempNode);
	}


	public String getTeamName() 
	{
		return teamName;
	}
	
	public String getArenaName()
	{
		return arenaName;
	}
	
	public String getLoginName()
	{
		return loginName;
	}
	
	public String getLastLoginDate()
	{
		return lastLogin;
	}
	
	public RankStatus getRankStatus()
	{
		return rankStatus;
	}
	public void setRankStatus(RankStatus rankStatus)
	{
		this.rankStatus = rankStatus;
	}


	@Override
	public int compareTo(HattrickTeam another) {
		return (another.teamRank.compareTo(this.teamRank));
	}


	public int getRank() {
		return teamRank;
	}


	public int getTeamID() {
		// TODO Auto-generated method stub
		return teamID;
	}


	public void setPlayerRank(int rank) 
	{
		this.rank = rank;
	}
	
	

}
