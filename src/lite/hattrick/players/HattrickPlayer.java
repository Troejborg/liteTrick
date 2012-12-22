
/**
 * File : Player.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 21/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.players;

import java.io.IOException;
import java.io.Serializable;

import lite.hattrick.requests.HattrickConnector;
import lite.hattrick.requests.XMLManager;
import lite.hattrick.services.HattrickHelper;
import lite.hattrick.services.RankStatus;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

public class HattrickPlayer implements Comparable<HattrickPlayer>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int HATTRICKYEAR = 112;
	
	private Integer tsi, lastMatchID, ageYears, ageDays;
	private int tsiDiff;
	private String firstName, lastName, teamName;
	private DateTime arrivalDateTime;
	private int leagueID;
	private String lastMatchDate;
	private String lastMatchPosition;
	private Double lastMatchRating;

	private transient HattrickConnector connector;
	private transient Element playerDetails;
	private int playerID;
	private RankStatus rankStatus;
	private int rank;
	
	public HattrickPlayer(int playerID) 
	{
		this.playerID = playerID;
		connector = HattrickConnector.getInstance();
	}

	public void getPlayerDetails()
	{
		try {
			Document doc = XMLManager.parseString(connector.getPlayerDetails(playerID));
			playerDetails = doc.getDocumentElement();
		} catch (IOException e) {
			Log.e("parsing", "Could not parse player details with team id : " + playerID);
			e.printStackTrace();
		}

		
		Element node = (Element) playerDetails.getElementsByTagName("Player").item(0);

		Element tempNode = node;
		firstName = HattrickHelper.getStringByTag("FirstName", tempNode);
		lastName = HattrickHelper.getStringByTag("LastName", tempNode);
		ageYears = Integer.parseInt(HattrickHelper.getStringByTag("Age", tempNode)); 
		ageDays = Integer.parseInt(HattrickHelper.getStringByTag("AgeDays", tempNode));
		tsi = Integer.parseInt(HattrickHelper.getStringByTag("TSI", tempNode));		
		
		arrivalDateTime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		arrivalDateTime = formatter.parseDateTime(HattrickHelper.getStringByTag("ArrivalDate", tempNode));

		Log.d("isPlayerValid", "Is Player Valid : " + isPlayerValid());
		
		tempNode = (Element)node.getElementsByTagName("OwningTeam").item(0);
		teamName = HattrickHelper.getStringByTag("TeamName", tempNode);
		leagueID = Integer.parseInt(HattrickHelper.getStringByTag("LeagueID", tempNode));

		tempNode = (Element)node.getElementsByTagName("LastMatch").item(0);

		lastMatchDate = HattrickHelper.getStringByTag("Date", tempNode);
		lastMatchID = Integer.parseInt(HattrickHelper.getStringByTag("MatchId", tempNode));
		lastMatchPosition = getPlayerPosition(HattrickHelper.getStringByTag("PositionCode", tempNode));
		lastMatchRating = Double.parseDouble(HattrickHelper.getStringByTag("Rating", tempNode));
		

	}
	
	public boolean isPlayerValid()
	{
		int daysBetween = Days.daysBetween(arrivalDateTime.toDateMidnight() , DateTime.now().toDateMidnight() ).getDays(); 
		int totalPlayerDays = ageYears * HATTRICKYEAR + ageDays;
		
		int daysOnArrival = totalPlayerDays - daysBetween;
		int playerAgeOnArrival = daysOnArrival/HATTRICKYEAR;
		int playerDaysOnArrival = daysOnArrival%HATTRICKYEAR;
		
		Log.d("PlayerAge", "Players Age on Arrival was : " + playerAgeOnArrival + "." + playerDaysOnArrival);
		if(playerAgeOnArrival == 17 && (playerDaysOnArrival <= 10))
			return true;
		else return false;
	}

	// Rank Methods
	public int getPlayerRank()
	{
		return rank;
	}
	
	public void setPlayerRank(int rank)
	{
		this.rank = rank;
	}
	
	public RankStatus getRankStatus()
	{
		return rankStatus;
	}
	
	public void setRankStatus(RankStatus rankStatus)
	{
		this.rankStatus = rankStatus;
	}
	
	public Integer getTSIdiff()
	{
		return tsiDiff;
	}
	
	@Override
	public int compareTo(HattrickPlayer hattrickPlayer) 
	{
		return (this.tsi.compareTo(hattrickPlayer.tsi));
	}
	
	// Manager info
	public int getPlayerID()
	{
		return playerID;
	}
	
	// Match Info
	public Integer getLastMatchID()
	{
		return lastMatchID;
	}

	public String getLastMatchDate()
	{
		return lastMatchDate;
	}

	public String getLastMatchPosition()
	{
		return lastMatchPosition;
	}

	public Double getLastMatchRating()
	{
		return lastMatchRating;
	}

	private String getPlayerPosition(String pos) 
	{
		int position = Integer.parseInt(pos);

		switch(position)
		{
		case 100:
			return "Keeper";
		case 101:
			return "Right Back";
		case 102:	
			return "Right Central Defender";
		case 103:
			return "Middle Central Defender";
		case 104:
			return "Left Central Defender";
		case 105:
			return "Left Back";
		case 106:
			return "Right Winger";
		case 107:
			return "Right Inner Midfield";
		case 108:
			return "Middle Inner Midfield";
		case 109:
			return "Left Inner Midfield";
		case 110:
			return "Left Winger";
		case 111:
			return "Right Forward";
		case 112:
			return "Middle Forward";

		default:
			return "Unknown Position";

		}
	}
	
	// Team INFO
	public int getLeagueID()
	{
		return leagueID;
	}

	public String getTeamName()
	{
		return teamName;
	}

	// Personal Player Info
	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public Integer getAgeYears()
	{
		return ageYears;
	}
	
	public Integer getAgeDays()
	{
		return ageDays;
	}

	public Integer getTSI()
	{
		return tsi;
	}
	
	public DateTime getArrivalDate()
	{
		return arrivalDateTime;
	}

}
