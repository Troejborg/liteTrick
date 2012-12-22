
	/**
	 * File : LoginActivity.java
	 * iPinion
	 * Created by : Ronnie Tr¿jborg
	 * Date : 18/06/2012
	 * Copyright (c) 2012 iPinion. All rights reserved.
	 */

package lite.hattrick.main;

import lite.hattrick.requests.HattrickConnector;

import org.scribe.model.Verifier;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class LoginActivity extends SherlockActivity {
//    private static final String PROTECTED_RESOURCE_URL = "http://chpp.hattrick.org/chppxml.ashx";

    private HattrickConnector connector;
 	private String authURL = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	new LoginTask(this).execute();
    		
    }

    @Override
    protected void onResume() {  
        super.onResume();  
        if (this.getIntent()!=null && this.getIntent().getData()!=null){  
            Uri uri = this.getIntent().getData();  
            if (uri != null && uri.toString().startsWith("callback://whodunit")) 
            {
                Verifier verifier = new Verifier ( uri.getQueryParameter("oauth_verifier") );
                
                new OnCallBackTask(verifier).execute();
            } 
        }
        	

    }
    private class OnCallBackTask extends AsyncTask<Void, Void, Void>
    {
    	private Verifier verifier;
		public OnCallBackTask(Verifier verifier) 
		{
			this.verifier = verifier;
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
            // Trade the Request Token and Verfier for the Access Token
			connector = HattrickConnector.getInstance();
			connector.setCredentials(verifier);
         
			return null;
		}
		
		protected void onPostExecute(Void result)
		{
				startActivityForResult(new Intent(LoginActivity.this, BaseActivity.class), 0);
		}
    	
    }
    
    
	 private class LoginTask extends AsyncTask<Void, Void, Void>
	    {
		 	private boolean isAccessTokenValid;
		 	private Context ctx;
		 	
		 	public LoginTask(Context ctx)
		 	{
		 		this.ctx = ctx;
		 	}
		    
			@Override
			protected Void doInBackground(Void... params) 
			{
				connector = HattrickConnector.getInstance();
				connector.initConnector(ctx);
				isAccessTokenValid = connector.isAccessTokenValid();
				authURL = connector.getAuthUrl();
				return null;
			}
			
			protected void onPostExecute(Void result)
			{
				if(!isAccessTokenValid)
					startActivity (new Intent ( Intent.ACTION_VIEW, Uri.parse(authURL)));
				else
					startActivityForResult(new Intent(LoginActivity.this, BaseActivity.class), 0);
			} 	
	    }
}