package com.kaspat.geekshare;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class AddActivity extends ActionBarActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
    
    int success;
 
    JSONParser jsonParser = new JSONParser();
    EditText mName;
    EditText mAuthor;
    String value;
    TextView txtName, txtAuthor;
    String name;
    
    Button add_new;
    DatabaseHandler db;
 
    // url to create new product
    private static String url;
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie);
        
        db = new DatabaseHandler(this);
 
        Bundle extras = getIntent().getExtras();
        value = extras.getString("category");
        name = extras.getString("name");
        
        url = "http://kaspat.com/android/insert_";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);                
        url = url + value + ".php";
        // Edit Text
        new AddMovie().execute();

        
    }
 
    /**
     * Background Async Task to Create new product
     * */
    class AddMovie extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddActivity.this);
            pDialog.setMessage("Adding "+value);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String movie = name;
            String author = db.getContactName();
            String number = db.getContactNumber();
 
            // Building Parameters
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mname", movie));
            params.add(new BasicNameValuePair("mauthor", author));
            params.add(new BasicNameValuePair("mnumber", number));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url,
                    "GET", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ViewActivity.class);
                    i.putExtra("category", value);
                    startActivity(i); 
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                	Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                	//Toast.makeText(getApplicationContext(), "Failed to Add Movie. Try Again", Toast.LENGTH_SHORT).show();
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            
        }
 
    }
}