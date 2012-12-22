
/**
 * File : HattrickFederation.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 24/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.federationrank;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lite.hattrick.requests.HattrickConnector;
import lite.hattrick.requests.XMLManager;
import lite.hattrick.services.HattrickHelper;
import lite.hattrick.teams.HattrickTeam;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

public class HattrickFederation implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient HattrickConnector instance;
	private transient Element federationDetails;
	private Integer[] userIDs;
	private int federationID;
	private List<HattrickTeam> teams;
	private String federationName;

	public HattrickFederation(int federationID)
	{
		instance = HattrickConnector.getInstance();
		this.federationID = federationID;
		
		try {
			Document doc = XMLManager.parseString(instance.getFederationDetails(federationID));
			federationDetails = doc.getDocumentElement();
		} catch (IOException e) {
			Log.e("parsing", "Could not parse Alliance details with Alliance id : " + federationID);
			e.printStackTrace();
		}
		
		Element node = (Element) federationDetails.getElementsByTagName("Alliance").item(0);
		
		federationName = HattrickHelper.getStringByTag("AllianceName", node);
		
		Element tempNode = (Element)node.getElementsByTagName("Members").item(0);
		NodeList members = tempNode.getElementsByTagName("Member");

		userIDs = new Integer[members.getLength()];
		Log.d("MemberNodes", "Nodes : " + members.getLength());
		teams = new ArrayList<HattrickTeam>();
		for(int i = 0; i<members.getLength(); i++)
		{
			userIDs[i] = Integer.parseInt(HattrickHelper.getStringByTag("UserID", (Element)members.item(i)));
//			Log.d("Federation Teams", "Index " + i + ": User ID is :" + userIDs[i]);
			teams.add(new HattrickTeam(userIDs[i], false));
		}
	}

	public int getFederationID()
	{
		return federationID;
	}
	
	public String getFederationName()
	{
		return federationName;
	}
	
	public List<HattrickTeam> getTeams()
	{
		return teams;
	}

}
