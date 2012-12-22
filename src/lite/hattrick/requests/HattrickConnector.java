package lite.hattrick.requests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.scribe.builder.ServiceBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.content.Context;
import android.util.Log;

public class HattrickConnector
{
	public static String m_sIDENTIFIER = "liteTrick 1.0";
	static final private String htUrl = "http://chpp.hattrick.org/chppxml.ashx";
	private final static String VERSION_PLAYERS = "2.1";
	private final static String VERSION_PLAYERDETAILS = "2.3";
	private final static String VERSION_FEDERATION = "1.4";
	private static final String VERSION_MATCHDETAILS = "2.3";
	private static final String VERSION_MATCHLINEUP = "1.8";
	private String m_ProxyUserName = "";
	private String m_ProxyUserPWD = "";
	private boolean m_bProxyAuthentifactionNeeded;
	private final static String CONSUMER_KEY = "1epxwz4iZGjzxAo8kwXIax";
	private final static String CONSUMER_SECRET = "xI3k5nO4BZlZdP7uJeYd57SUs3w1INhLteubi9nYSWe";
	
	// Debugging ?
	private boolean DEBUGMODE = true;
	
	private HattrickUserParameter userParams;
	private Context context;

	private static HattrickConnector instance;
	private OAuthService authService;
	private Token requestToken;
	private Token accessToken;
	public static HattrickConnector getInstance()
	{
		if(instance == null)
			instance = new HattrickConnector();
		return instance;
	}

	private HattrickConnector()
	{

		authService = new ServiceBuilder()
		.provider(HattrickAPI.class)
		.apiKey(CONSUMER_KEY)
		.apiSecret(CONSUMER_SECRET)
		.callback("callback://whodunit")
		.build();

		requestToken = authService.getRequestToken();
		userParams = new HattrickUserParameter();
		accessToken = new Token(
				userParams.getAccessToken(),
				userParams.getTokenSecret());
	}

	public void initConnector(Context context)
	{
		userParams = new HattrickUserParameter(context);
		accessToken = new Token(
				userParams.getAccessToken(),
				userParams.getTokenSecret());
	}

	public String getAuthUrl()
	{
		return authService.getAuthorizationUrl(requestToken);
	}

	public boolean isAccessTokenValid()
	{
		OAuthRequest request = new OAuthRequest(Verb.GET, htUrl + "?file=players&version=" + VERSION_PLAYERS);
		int iResponse = 404;
		try{
			if(accessToken == null || accessToken.getToken().length() == 0)
			{
				iResponse = 401;
			}
			else
			{
				authService.signRequest(accessToken, request);
				Response response = request.send();
				iResponse = response.getCode();
			}

		}catch(NullPointerException e)
		{
			iResponse = 401;
		}

		switch(iResponse)
		{
		case 200:
		case 201:
			return true;
		default:
			Log.d("Response :", iResponse + "");
			return false;



		}
	}

	public void setCredentials(Verifier verifier) 
	{
		System.out.println("Trading the Request Token for an Access Token...");
		System.out.println(" ---> Request Token: " + requestToken.getToken());
		System.out.println(" ---> Request Token Secret: " + requestToken.getSecret());
		System.out.println(" ---> Verifier: " + verifier.getValue());
		try
		{
			accessToken = authService.getAccessToken(requestToken, verifier);

		}catch(OAuthException e)
		{

			e.printStackTrace();
			return;
		}

		userParams.setAccessToken(accessToken.getToken());
		userParams.setTokenSecret(accessToken.getSecret());

	}

	public String getPlayers() throws IOException 
	{
		final String url = htUrl + "?file=players&version=" + VERSION_PLAYERS;
		return getCHPPWebFile(url);
	}

	public String getPlayerDetails(int playerID) throws IOException
	{
		String url = htUrl + "?file=playerdetails&includeMatchInfo=true&version="+ VERSION_PLAYERDETAILS;
		if(playerID > 0)
		{
			url += ("&playerID=" + playerID);
		}

		return getCHPPWebFile(url);
	}

	public String getTeamdetails(int id, boolean isTeamID) throws IOException 
	{
		String url = htUrl + "?file=teamdetails";
		if(isTeamID && id > 0)
			url += ("&teamID=" + id);
		else
			url +=("&userID=" + id);

		return getCHPPWebFile(url);
	}

	public String getFederationDetails(int allianceId) throws IOException 
	{
		String url = htUrl + "?file=alliancedetails&actionType=members&version="+ VERSION_FEDERATION;
		if (allianceId > 0) 
		{
			url += ("&allianceID=" + allianceId);
		}

		return getCHPPWebFile(url);
	}
	
	public String getMatchDetails(int matchID) throws IOException
	{
		String url = htUrl + "?file=matchdetails&sourceSystem=hattrick&version=" + VERSION_MATCHDETAILS;
		if(matchID > 0)
			url += ("&matchID=" + matchID);
		
		return getCHPPWebFile(url);
	}
	
	public String getMatchLineup(int matchID, int teamID) throws IOException
	{
		String url = htUrl + "?file=matchlineup&version=" + VERSION_MATCHLINEUP;
		if(matchID > 0)
			url += ("&matchID=" + matchID + "&teamID=" + teamID);
		
		return getCHPPWebFile(url);
	}

	private String getCHPPWebFile(String surl)
	{
		String returnString = null;
		Response response = null;
		int iResponse = 200;
		boolean tryAgain = true;
		while(tryAgain == true)
		{
			OAuthRequest request = new OAuthRequest(Verb.GET, surl);
			request.setConnectTimeout(60, TimeUnit.SECONDS);
			request.setConnectionKeepAlive(true);

			if(accessToken == null || accessToken.getToken().length() == 0)
			{
				iResponse = 401;
			}
			else
			{
				authService.signRequest(accessToken, request);
				response = request.send();
				
				iResponse = response.getCode();
			}
			switch(iResponse)
			{
			case 200:
			case 201:
				returnString = response.getBody().toString();
				Log.d("Response", "CODE " + response.getCode());
				Log.d("Response BODY", returnString);
				tryAgain = false;
			default:
				Log.d("Response", iResponse + "");

			}
		}
		return returnString;
	}
}