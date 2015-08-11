package com.kaspat.geekshare;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
   
public class MyCustomBaseAdapter extends BaseAdapter implements Filterable {
    
	private ArrayList<HashMap<String, String>> dataList;
	    
    private Context mContext;
    private LayoutInflater mInflater;
   
    public MyCustomBaseAdapter(Context context, ArrayList<HashMap<String, String>> results) {
        this.dataList = results;        
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
   
    
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_layout, null);
            holder = new ViewHolder();
            holder.Name = (TextView) convertView.findViewById(R.id.mname);
            holder.Author = (TextView) convertView.findViewById(R.id.mauthor);
            holder.Number = (TextView) convertView.findViewById(R.id.mnumber);
            holder.imageIcon=(ImageView) convertView.findViewById(R.id.list_image);
   
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Name.setTextColor(Color.parseColor("#2B2929"));
        holder.Author.setTextColor(Color.parseColor("#7D7A7A"));
        
        holder.Name.setText(dataList.get(position).get("mname"));
        holder.Author.setText(dataList.get(position).get("mauthor"));
        holder.Number.setText(dataList.get(position).get("mnumber"));
        holder.imageIcon.setImageResource(mContext.getResources().getIdentifier(dataList.get(position).get("category") , "drawable", mContext.getPackageName()));
        
          
        return convertView;
    }
   
    static class ViewHolder {
        TextView Name;
        TextView Author;
        TextView Number;
        ImageView imageIcon;
    }

    public int getCount() {
        return dataList.size();
    }
   
    public Object getItem(int position) {
        return dataList.get(position);
    }
   
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                dataList = (ArrayList<HashMap<String, String>>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
                
            }

            @SuppressLint("DefaultLocale")
			@Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<HashMap<String, String>> FilteredArrList = new ArrayList<HashMap<String, String>>();
                
                int flag = 0;
                /********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = dataList.size();
                    results.values = dataList;
                } else {
                	flag = 0;
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < dataList.size(); i++) {
                        String data = dataList.get(i).get("mname");
                        if (data.toLowerCase().startsWith(constraint.toString())) { 
                        	HashMap<String, String> moviesMap = new HashMap<String, String>();
                            moviesMap.put("mname",dataList.get(i).get("mname"));
                            moviesMap.put("mauthor",dataList.get(i).get("mauthor"));
                            moviesMap.put("mnumber",dataList.get(i).get("mnumber"));
                            moviesMap.put("category",dataList.get(i).get("category"));
                            FilteredArrList.add(moviesMap);
                            flag = 1;                                                          
                        }                        
                    }                    
                                      
                    // set the Filtered result to return                    
                    if(flag == 1){
                    	dataList.clear();
                    	dataList.addAll(FilteredArrList);
                    	results.count = dataList.size();
                    	results.values = dataList;
                    }
                    else{
                    	results.count = dataList.size();
                        results.values = dataList;
                    }
                }
                return results;
            }
        };        
        return filter;
    }
    

    
}