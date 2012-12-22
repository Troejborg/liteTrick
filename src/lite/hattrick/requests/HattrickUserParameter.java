
/**
 * File : UserParameter.java
 * iPinion
 * Created by : Ronnie Tr¿jborg
 * Date : 15/06/2012
 * Copyright (c) 2012 iPinion. All rights reserved.
 */

package lite.hattrick.requests;

import android.content.Context;
import android.content.SharedPreferences;

public class HattrickUserParameter 
{
	// SharedPreferences vars
	private static final String USERINFO_FILE = "StoredUserInfo";
	SharedPreferences userInfo;
	SharedPreferences.Editor userEditor;

	private String accessToken, tokenSecret;

	public HattrickUserParameter(Context context)
	{
		userInfo = context.getSharedPreferences(USERINFO_FILE, 0);
		userEditor = userInfo.edit();
		accessToken = userInfo.getString("accessToken", null);
		tokenSecret = userInfo.getString("tokenSecret", null);

	}
	public HattrickUserParameter()
	{

	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		userEditor.putString("accessToken", accessToken);
		userEditor.commit();
	}

	public String getTokenSecret()
	{
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret)
	{
		userEditor.putString("tokenSecret", tokenSecret);
		userEditor.commit();
	}

}
