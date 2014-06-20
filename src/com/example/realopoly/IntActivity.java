package com.example.realopoly;

import com.facebook.Session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class IntActivity extends Activity {

	Button b1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	setContentView(R.layout.int_activity);

	b1 = (Button) findViewById(R.id.authButton);
	b1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Session session = Session.getActiveSession();
			if (session != null) {
			    session.closeAndClearTokenInformation();
			}
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
			
		}
	});
	
	}
	
}
