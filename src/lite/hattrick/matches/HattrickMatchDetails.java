
	/**
	 * File : HattrickMatch.java
	 * iPinion
	 * Created by : Ronnie Tr¿jborg
	 * Date : 27/06/2012
	 * Copyright (c) 2012 iPinion. All rights reserved.
	 */

package lite.hattrick.matches;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;
import lite.hattrick.requests.HattrickConnector;
import lite.hattrick.requests.XMLManager;

public class HattrickMatchDetails 
{
	private int matchID;
	private HattrickConnector connector;
	private transient Element matchDetails;
	
	public HattrickMatchDetails(int matchID)
	{
		this.matchID = matchID;
		connector = HattrickConnector.getInstance();
		
		try {
			Document doc = XMLManager.parseString(connector.getMatchDetails(matchID));
			matchDetails = doc.getDocumentElement();
		} catch (IOException e) {
			Log.e("parsing", "Could not parse match details with match id : " + matchID);
			e.printStackTrace();
		}

		Element node = (Element) matchDetails.getElementsByTagName("Match").item(0);
		
		
	}
	
	
}
