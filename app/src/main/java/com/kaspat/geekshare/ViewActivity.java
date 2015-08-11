package com.kaspat.geekshare;

import com.kaspat.geekshare.MyCustomBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ViewActivity extends ActionBarActivity implements OnRefreshListener, OnQueryTextListener  {

	private ProgressDialog pDialog; 
	
	SearchView searchView;
	
    private static String url;
    
    String value,category,col,title,name,n,author,number;
    
    EditText eName;
    ImageView img,cancel;
    
    MenuItem item;
    
    ArrayList<HashMap<String, String>> movieList;
    ListView lv;
    SwipeRefreshLayout swipeLayout;
    
    ImageView catImage;
    Drawable image;
    
    MyCustomBaseAdapter adapter;
    
    DatabaseHandler db;
    
    @SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);
        
        db = new DatabaseHandler(this);
       
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);
        
        url = "http://kaspat.com/android/";
        Bundle extras = getIntent().getExtras();
        value = extras.getString("category");
        url = url + value + ".php";
        
        if(value.equals("movies")){
        	category = String.valueOf(R.drawable.ic_launcher_play_movietv);        	
        	col = "#CE2121";
        	title = "Movies";
        }
        if(value.equals("games")){
        	category = String.valueOf(R.drawable.ic_launcher_play_apps);
        	col = "#57BA48";
        	title = "Games";
        }
        if(value.equals("softwares")){
        	category = String.valueOf(R.drawable.ic_launcher_play_newsstand);
        	col = "#683CE9";
        	title = "Softwares";
        }
        if(value.equals("series")){
        	category = String.valueOf(R.drawable.ic_menu_play);
        	col = "#0D94C6";
        	title = "Series";
        }
        if(value.equals("others")){
        	category = String.valueOf(R.drawable.ic_launcher_play_music);
        	col = "#EB7506";
        	title = "Songs";
        }
        if(value.equals("books")){
        	category = String.valueOf(R.drawable.ic_launcher_play_books);
        	col = "#0D94C6";
        	title = "Books";
        }       
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(col)));
        actionBar.setTitle(title);
                       
        movieList = new ArrayList<HashMap<String, String>>();
        lv = (ListView) findViewById(R.id.list);
        
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (lv == null || lv.getChildCount() == 0) ?
                                0 : lv.getChildAt(0).getTop();
                swipeLayout.setEnabled((topRowVerticalPosition >= 0));
            }
        });
        
     // Listview on item click listeners
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                n = ((TextView) view.findViewById(R.id.mname))
                        .getText().toString();
                author = ((TextView) view.findViewById(R.id.mauthor))
                        .getText().toString();
                number = ((TextView) view.findViewById(R.id.mnumber))
                        .getText().toString();
                
                if(number.equals(db.getContactNumber())){
                	new AlertDialog.Builder(ViewActivity.this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure? You want to Delete the Selected data?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { 
                            // restart activity
                        	Intent i = new Intent(ViewActivity.this,DeleteActivity.class);
                        	i.putExtra("category", value);
                        	i.putExtra("name", n);
                        	i.putExtra("author", author);
                        	i.putExtra("number", number);
                        	finish();
                        	startActivity(i);
                        }
                     })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { 
                            // finish activity
                        	
                        }
                     })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                     .show();
     
                }
                else{
                	Toast.makeText(getApplicationContext(), "You can delete only your added data",Toast.LENGTH_LONG).show();
                }
 
                
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
                	Intent i = new Intent(ViewActivity.this,MainActivity.class);
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
            pDialog = new ProgressDialog(ViewActivity.this);      
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
                    movies = jsonObj.getJSONArray(value);
 
                    // looping through All Contacts
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);
                         
                        String id = c.getString("id");
                        String movieName = c.getString("Name");
                        String authorName = c.getString("Author");
                        String mNumber = c.getString("Number");
                        
                        // tmp hashmap for single contact
                        HashMap<String, String> moviesMap = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        moviesMap.put("id", id);                      
                        moviesMap.put("mname", movieName);
                        moviesMap.put("mauthor", authorName);
                        moviesMap.put("mnumber", mNumber);
                        moviesMap.put("category", category);
 
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
            adapter = new MyCustomBaseAdapter(getApplicationContext(), movieList);
            lv.setAdapter(adapter);   
            adapter.notifyDataSetChanged();
        }
 
    }
    
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
            	movieList.clear();
            	new GetMovies().execute();
                swipeLayout.setRefreshing(false);
            }
        }, 1000);
    }


    @SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
 
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onQueryTextChange(String newText)
    {
     // this is your adapter that will be filtered
    	if(newText == ""){
    		adapter.getFilter().filter("");
    		adapter.notifyDataSetChanged();
    	}
    	else{    		
    		adapter.getFilter().filter(newText);
    		adapter.notifyDataSetChanged();
    	}
            
         return true;
    }
    
    public boolean onQueryTextSubmit(String query) {
     // TODO Auto-generated method stub
    	adapter.getFilter().filter(query);
     return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {        	
            return true;
        }
        if (id == R.id.action_new) {
        	add_new();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }   
    
    
    public void add_new(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle("Add New "+title);
    	alert.setMessage(title+" Name");

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(this);
    	input.setFilters(new InputFilter[] {
    		    new InputFilter() {
					@Override
					public CharSequence filter(CharSequence src, int start,
							int end, Spanned dest, int dstart, int dend) {
						// TODO Auto-generated method stub
						if(src.equals("")){ // for backspace
    		                return src;
    		            }
    		            if(src.toString().matches("[a-zA-Z 0-9 - _]+")){
    		                return src;
    		            }
    		            return "";
					}
    		    }
    		});
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    	  String name = input.getText().toString();
    	  // Do something with value!    	  				
			Intent i = new Intent(ViewActivity.this,AddActivity.class);			
			i.putExtra("name", name);
			i.putExtra("category", value);
			startActivity(i);
			finish();
    	  }
    	});

    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    	    // Canceled.
    	  }
    	});

    	alert.show();
    }	
    
}
