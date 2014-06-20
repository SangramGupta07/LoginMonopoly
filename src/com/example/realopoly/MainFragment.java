package com.example.realopoly;

import android.content.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification.Action;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.example.realopoly.R;

public class MainFragment extends Fragment {
	
	Button b1;
	
	private ArrayList<String> permission;
	private static final String TAG = "MainFragment";
	private TextView userInfoTextView;
	private Button idauthbutton;

	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) 
        {
            onSessionStateChange(session, state, exception);
        }
    };
    
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, 
			Bundle savedInstanceState) {		
		View view = inflater.inflate(R.layout.activity_main, container, false);		
		userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
		idauthbutton = (Button) view.findViewById(R.id.authButton);
		

		b1 = (Button) view.findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity().getApplicationContext(), IntActivity.class);
	            startActivity(intent);
				
			}
		});
		
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("user_about_me", "user_birthday", "user_activities", "user_website", "email"));
		 
		return view;
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}
		
        uiHelper.onResume();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
  
    	if (state.isOpened()) {
    	    userInfoTextView.setVisibility(View.VISIBLE);
    	    idauthbutton.setVisibility(View.INVISIBLE);
    	    b1.setVisibility(View.VISIBLE);
    	    // Request user data and show the results
    	    
    	    
     	   try{ 
     	    		   
     	/*	   Request request=new Request().newMeRequest(session, new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (session==Session.getActiveSession())
					{
						if(user!=null)
							user.get
					}	
				}
			}); */
 	    	 
     		        		   
 	    	    	Runnable runable = new Runnable() 
 	    	    	{
						public void run() 
						{
							try
							{
								String token = session.getAccessToken();
								 HttpClient httpClient = new DefaultHttpClient();
							 	    
				 	    	    String url = "https://graph.facebook.com/v2.0/me?fields=about,birthday,name,email&oauth_token=" + URLEncoder.encode("" + token, "UTF-8");
				 	    	    HttpGet request = new HttpGet(url);
				 	    	    HttpResponse response = null;
								response = httpClient.execute(request);
		 			    	    
		 			    	    StringBuilder sb = new StringBuilder();
		 					    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		 					    
		 					    for (String line; null != (line = reader.readLine());) 
		 					    {
		 					            sb.append(line);
		 					    }
		 					    
		 					    String output = sb.toString();		    
		 					    JSONObject json = new JSONObject(output);
		 					   
		 					    String id = json.getString("id");
		 					    String email = json.getString("email");
		 					    
		 					    sendDetailsToOurServer(id,token,email);
							}
			 	    	    catch(Exception e)
			 	    	    	{
			 	    	    		e.printStackTrace();
			 	    	    	}
						}
					};
					new Thread(runable).start();
 			
 	    	}
     	   catch (Exception e) 
     	   {
     		   e.printStackTrace();
     	   }
    	        	   
    	    Request.newMeRequest(session, new Request.GraphUserCallback() {
    	    	
    	    	@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
	    	                // Display the parsed user info
	    	                userInfoTextView.setText(buildUserInfoDisplay(user));	
	    	                //session.getAccessToken();
	    	            }
				}
    	    }).executeAsync();   
    	
    	    
    	} 
    	else if (state.isClosed()) {
    		b1.setVisibility(View.INVISIBLE);
        	userInfoTextView.setVisibility(View.INVISIBLE);
        	Log.i(TAG, "Logged out...");
        }
    	  	
    }
    
    protected void sendDetailsToOurServer(final String id,final String token,final String email) 
    {
    	
		  final  String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		  
	    
    	try{
	   
 	    	    	Runnable runable2 = new Runnable() 
 	    	    	{
						public void run() 
						{
							try
							{
								
								HttpClient httpClient = new DefaultHttpClient();
							 	String url = "http://ap.realopoli.com:80/api/v1/authenticate/";
				 	    	    HttpPost httppost = new HttpPost(url);
				 	    	    List<NameValuePair> params = new ArrayList<NameValuePair>(4);
		//		 	    	    Log.i("Date YYYY-MM-DD", date);
				 	    	    params.add(new BasicNameValuePair("oauth2_token_validity", date));
				 	    	    params.add(new BasicNameValuePair("fb_id", id));
				 	    	    params.add(new BasicNameValuePair("oauth2_access_token", token));
				 	    	    params.add(new BasicNameValuePair("email", email));
				 	    	    
				 	    	   httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				 	    	    
				 	    	    HttpResponse response = null;
								response = httpClient.execute(httppost);
								
								StatusLine statusLine = response.getStatusLine();
							    // int statusCode = statusLine.getStatusCode();
							     StringBuilder builder = new StringBuilder();
							     
							     
							         HttpEntity entity = response.getEntity();
							         InputStream content = entity.getContent();
							         
							         BufferedReader reader = new BufferedReader(new InputStreamReader(content));
							         String line;
							         
							         while ((line = reader.readLine()) != null) {
							           builder.append(line);
							         }
							     String output = builder.toString();
							     Log.i("Output", output);
							     
		 			    	}
			 	    	    catch(Exception e)
			 	    	    	{
			 	    	    		e.printStackTrace();
			 	    	    	}
						}
					};
					new Thread(runable2).start();
   	 			
   	 	    	}
   	     	   catch (Exception e) 
   	     	   {
   	     		   e.printStackTrace();
   	     	   }
   	   
		
	}

	private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        userInfo.append(String.format("Name: %s\n\n", 
            user.getName()));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        userInfo.append(String.format("Birthday: %s\n\n", 
            user.getBirthday()));


        return userInfo.toString();
    }
    
}
