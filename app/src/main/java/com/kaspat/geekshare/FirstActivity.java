package com.kaspat.geekshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity {

	Button submit;
	EditText yourNumber, yourName;
	
	String name, number;
	DatabaseHandler db;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        
        db = new DatabaseHandler(this);        
        if(db.getContactName() != "" ){
        	Intent i = new Intent(FirstActivity.this,MainActivity.class);
			startActivity(i);
			finish();
        }              
               
        submit = (Button)findViewById(R.id.submit);
        yourNumber = (EditText)findViewById(R.id.yourNumber);
        yourName = (EditText)findViewById(R.id.yourName);
        
        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = yourName.getText().toString();
				number = yourNumber.getText().toString();							
				db.addContact(new Contact(name, number));        
				Intent i = new Intent(FirstActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}
		});
	}  
	
}
