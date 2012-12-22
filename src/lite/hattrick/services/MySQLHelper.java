
/**
 * File : MySQLHelper.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 22/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import android.util.Log;

public class MySQLHelper {

	private static final String url = "jdbc:mysql://cupcakeserver.dk/ronnie";	
	private static final String user = "Ronnie";
	private static final String password = "azSLa7frAyFeMxLd";
	
	public static ArrayList<Integer> getPlayerIDs()
	{
		ArrayList<Integer> players = new ArrayList<Integer>();
		try {
		    Properties props = new Properties();
		    props.put("user", user);
		    props.put("password", password);
		    props.put("autoReconnect", "true");
		    
			System.out.println("Fetching JDBC Driver Class");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(url, props);
			System.out.println("Success");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM player");
			while(rs.next()) 
			{
				players.add((Integer)rs.getInt(2));
				Log.d("SQL","PlayerID : " + rs.getInt(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return players;	
	}
	
	public static boolean insertPlayer(int playerID, String playerName)
	{
		try {
			System.out.println("Fetching JDBC Driver Class");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, password);
			System.out.println("Success");
			Statement st = con.createStatement();
			st.executeQuery("INSERT INTO `ronnie`.`player` (`id`, `playerid`, `playername`) VALUES (NULL," + playerID + "," + "'" + playerName + "')");
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

