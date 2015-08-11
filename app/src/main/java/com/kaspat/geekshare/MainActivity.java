package com.kaspat.geekshare;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainActivity extends ActionBarActivity {

    Button  books_button, songs_button, games_button,movies_button,soft_button,series_button;
	private ProgressDialog pDialog;	 
    private static String url = "http://kaspat.com/android/mainActivity.php";	
    
    ArrayList<HashMap<String, String>> movieList, gamesList, softList, seriesList, songsList, bookList;
    ListView listMovies, listGames, listSoft, listSeries, listSongs, listBooks;
    ScrollView myScrollView;
    
    HashMap<String, String> moviesMap = new HashMap<String, String>();
    
    DatabaseHandler db = new DatabaseHandler(this);
   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        AdView adBot=(AdView)findViewById(R.id.adViewBottom);		
		adBot.loadAd(new AdRequest.Builder().build());
        
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#683CE9")));
        //myScrollView =  (ScrollView)findViewById(R.id.scroll1);
        
        Drawable movies = getApplicationContext().getResources().getDrawable( R.drawable.ic_play_widgets_movies );
        Drawable books = getApplicationContext().getResources().getDrawable( R.drawable.ic_play_widgets_books );
        Drawable music = getApplicationContext().getResources().getDrawable( R.drawable.ic_play_widgets_music );
        Drawable newsstand = getApplicationContext().getResources().getDrawable( R.drawable.ic_play_widgets_newsstand );
        Drawable np = getApplicationContext().getResources().getDrawable( R.drawable.ic_play_widgets_np );
        
        movies.setBounds( 0, 0, 30, 30 );
        books.setBounds( 0, 0, 30, 30 );
        music.setBounds( 0, 0, 30, 30 );
        newsstand.setBounds( 0, 0, 30, 30 );
        np.setBounds( 0, 0, 30, 30 );
         
        //Movies Button
        movies_button = (Button) findViewById(R.id.movies_button);
        movies_button.setCompoundDrawables( movies, null, null, null );
        
        movies_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewActivity.class);
				i.putExtra("category", "movies");
				startActivity(i);
			}
		});     
        
        
        //Games Button
        games_button = (Button) findViewById(R.id.games_button);
        games_button.setCompoundDrawables( books, null, null, null );
        
        games_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewActivity.class);
				i.putExtra("category", "games");
				startActivity(i);
			}
		});
        
        //Softawres button
        soft_button = (Button) findViewById(R.id.soft_button);
        soft_button.setCompoundDrawables( newsstand, null, null, null );
        
        soft_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewActivity.class);
				i.putExtra("category", "softwares");
				startActivity(i);
			}
		});       
        
        //Series Button
        series_button = (Button) findViewById(R.id.series_button);
        series_button.setCompoundDrawables( np, null, null, null );
        
        series_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewActivity.class);
				i.putExtra("category", "series");
				startActivity(i);
			}
		});   
        
        //Songs button
        songs_button = (Button) findViewById(R.id.songs_button);
        songs_button.setCompoundDrawables( music, null, null, null );
        
        songs_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewActivity.class);
				i.putExtra("category", "others");
				startActivity(i);
			}
		}); 
        
        //Series button
      //Songs button
        books_button = (Button) findViewById(R.id.books_button);
        books_button.setCompoundDrawables( newsstand, null, null, null );
        
        books_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewActivity.class);
				i.putExtra("category","books");
				startActivity(i);
			}
		});
        
        
        movieList = new ArrayList<HashMap<String, String>>();
        
        listMovies = (ListView) findViewById(R.id.listMovie);        
                
     // Listview on item click listeners
        listMovies.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
               // String name = ((TextView) view.findViewById(R.id.mname))
                 //       .getText().toString();
               // String author = ((TextView) view.findViewById(R.id.mauthor))
                   //     .getText().toString();
 
                // Starting single contact activity
                
                /*Intent in = new Intent(getApplicationContext(),
                        SingleContactActivity.class);
                in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_EMAIL, cost);
                in.putExtra(TAG_PHONE_MOBILE, description);
                startActivity(in);*/
 
            }
        });        
        
        // Calling async task to get json
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
         
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();
        if(isConnected){
        	new GetMovies().execute();
        }
        else{
        	new AlertDialog.Builder(this)
            .setTitle("Internet Error")
            .setMessage("Please check your internet connection")
            .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    // restart activity
                	Intent i = new Intent(MainActivity.this,MainActivity.class);
                	finish();
                	startActivity(i);
                }
             })
            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    // finish activity
                	finish();
                }
             })
            .setIcon(android.R.drawable.ic_dialog_alert)
             .show();
        }
    }

    
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetMovies extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            JSONArray movies = null;            
 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    movies = jsonObj.getJSONArray("movies");                                       
 
                    // looping through All Contacts
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);
                         
                        String id = c.getString("id");
                        String movieName = c.getString("Name");
                        String authorName = c.getString("Author");
                        String cat;
                        switch(i){
                        case 0: cat = String.valueOf(R.drawable.ic_launcher_play_movietv);
                        		break;
                        case 1: cat = String.valueOf(R.drawable.ic_launcher_play_apps);
                        		break;
                        case 2: cat = String.valueOf(R.drawable.ic_menu_play);
                        		break;
                        case 3: cat = String.valueOf(R.drawable.ic_launcher_play_newsstand);
                        		break;
                        case 4: cat = String.valueOf(R.drawable.ic_launcher_play_music);
                        		break;
                        case 5: cat = String.valueOf(R.drawable.ic_launcher_play_books);
                				break;
                        default: cat = String.valueOf(R.drawable.ic_launcher_play_movietv);
                        		break;
                        }
                        
                        // tmp hashmap for single contact
                        HashMap<String, String> moviesMap = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        moviesMap.put("id", id);
                        moviesMap.put("mname", movieName);                        
                        moviesMap.put("mauthor", authorName);
                        moviesMap.put("category", cat);
 
                        // adding contact to contact list
                        movieList.add(moviesMap);
                    }
                    
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */         
            listMovies.setAdapter(new MyCustomBaseAdapter(getApplicationContext(), movieList));
            
        }
 
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
        	new AlertDialog.Builder(MainActivity.this)
            .setMessage("Designed & Developed by Kaspat")            
            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    
                }
             })            
            .setIcon(R.drawable.ic_action_about)
             .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
