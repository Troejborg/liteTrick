
/**
 * File : HattrickHelper.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 21/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import lite.hattrick.matches.MatchType;
import lite.hattrick.players.PlayerRole;
import lite.hattrick.requests.XMLManager;

import org.w3c.dom.Element;

import android.content.Context;
import android.util.Log;

public class HattrickHelper 
{
	public static String getStringByTag(String tag, Element node)
	{
		Element ele;
		ele = (Element) node.getElementsByTagName(tag).item(0);
		return XMLManager.getFirstChildNodeValue(ele);

	}

	/**
	 * 
	 * @param obj saves an instance of the object locally
	 * @return the result of the attempt at storing the object. 
	 */
	public static boolean save(Context ctx, Object obj, String filename)
	{
		new File(filename);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 

		try { 


			ObjectOutput out = new ObjectOutputStream(bos); 
			out.writeObject(obj);
			byte[] buf = bos.toByteArray(); 

			FileOutputStream fos = ctx.openFileOutput(filename,                      
					Context.MODE_PRIVATE);
			fos.write(buf);
			fos.close(); 

		} catch(IOException ioe) { 
			Log.e("serializeObject", "error", ioe); 
			return false;

		} 
		File f = ctx.getDir(filename, 0);
		Log.v("FILE",f.getName());    
		return true;
	}

	/**
	 *  Attempts to load the stored instance of the object stored in the specificed file
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws OptionalDataException 
	 */
	public static Object load(Context ctx, String filename) throws FileNotFoundException 
	{
		Object loadedObj = null;
		InputStream instream = null;

		instream = ctx.openFileInput(filename);

		try {
			ObjectInputStream ois = new ObjectInputStream(instream);
			loadedObj = ois.readObject();
			return loadedObj;
			
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static MatchType getMatchType(int matchTypeID)
	{
		switch(matchTypeID)
		{
		case 1:
			return MatchType.LEAGUE_MATCH;
		case 2:
			return MatchType.QUAL_MATCH;
		case 3:
			return MatchType.CUP_MATCH;
		case 4:
		case 5:
			return MatchType.FRIENDLY_NATIONAL;
		case 7:
			return MatchType.MASTERS_MATCH;
		case 8:
		case 9:
			return MatchType.FRIENDLY_INTERNATIONAL;
		case 10:
		case 11:
			return MatchType.NT_NORMAL_MATCH;
		case 12:
			return MatchType.NT_FRIENDLY_MATCH;

		default:
			return MatchType.UNKNOWN_MATCHTYPE;
		}
	}

	public static HattrickSkillLevel getSkillLevel(int skillLevel) 
	{
		switch(skillLevel)
		{
		case 0:
			return HattrickSkillLevel.NONE_EXISTENT;
		case 1:
			return HattrickSkillLevel.DISASTROUS;
		case 2:
			return HattrickSkillLevel.WRETCHED;
		case 3:
			return HattrickSkillLevel.POOR;
		case 4:
			return HattrickSkillLevel.WEAK;
		case 5:
			return HattrickSkillLevel.INADEQUATE;
		case 6:
			return HattrickSkillLevel.PASSABLE;
		case 7:
			return HattrickSkillLevel.SOLID;
		case 8:
			return HattrickSkillLevel.EXCELLENT;
		case 9:
			return HattrickSkillLevel.FORMIDABLE;
		case 10:
			return HattrickSkillLevel.OUTSTANDING;
		case 11:
			return HattrickSkillLevel.BRILLIANT;
		case 12:
			return HattrickSkillLevel.MAGNIFICENT;
		case 13:
			return HattrickSkillLevel.WORLD_CLASS;
		case 14:
			return HattrickSkillLevel.SUPERNATURAL;
		case 15:
			return HattrickSkillLevel.TITANIC;
		case 16:
			return HattrickSkillLevel.EXTRA_TERRESTRIAL;
		case 17:
			return HattrickSkillLevel.MYTHICAL;
		case 18:
			return HattrickSkillLevel.MAGICAL;
		case 19:
			return HattrickSkillLevel.UTOPIAN;
		case 20:
			return HattrickSkillLevel.DIVINE;
		default:
			return HattrickSkillLevel.UNKNOWN;
		}
	}

	public static PlayerRole getPlayerPosition(String pos) 
	{
		int position = Integer.parseInt(pos);

		switch(position)
		{
		case 100:
			return PlayerRole.KEEPER;
		case 101:
			return PlayerRole.BACK_RIGHT;
		case 102:	
			return PlayerRole.CD_RIGHT;
		case 103:
			return PlayerRole.CD_MIDDLE;
		case 104:
			return PlayerRole.CD_LEFT;
		case 105:
			return PlayerRole.BACK_LEFT;
		case 106:
			return PlayerRole.WINGER_RIGHT;
		case 107:
			return PlayerRole.IM_RIGHT;
		case 108:
			return PlayerRole.IM_MIDDLE;
		case 109:
			return PlayerRole.IM_LEFT;
		case 110:
			return PlayerRole.WINGER_LEFT;
		case 111:
			return PlayerRole.FORWARD_RIGHT;
		case 112:
			return PlayerRole.FORWARD_MIDDLE;
		case 113:
			return PlayerRole.FORWARD_LEFT;

		default:
			return PlayerRole.UNKNOWN_POSITION;

		}
	}
}
