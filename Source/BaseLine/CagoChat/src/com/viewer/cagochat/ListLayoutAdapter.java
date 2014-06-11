package com.viewer.cagochat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListLayoutAdapter extends ArrayAdapter<String> {
 
    private final Context context;
    private final String[] var;
 
    public ListLayoutAdapter(Context context, String[] var) {
        super(context, R.layout.peerlist, var);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.var = var;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	 // TODO Auto-generated method stub
        LayoutInflater li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = li.inflate(R.layout.peerlist, parent, false);
        TextView text = (TextView) row.findViewById(R.id.txt);
       // ImageView img = (ImageView) row.findViewById(R.id.icon);
        text.setText(var[position]); 
     //   b1.setText("Hello");
   //     String s = var[position];
        Log.d("tag","Value="+position);
    /*    if (s.startsWith("C")) {
 
            img.setImageResource(R.drawable.c);
        }
        else if(s.startsWith("Java")){
            img.setImageResource(R.drawable.java);
        }
        else if(s.startsWith("Android")){
            img.setImageResource(R.drawable.ic_launcher);
        }
        else{
            //c++
            img.setImageResource(R.drawable.cplus);
        }*/
            return row;
        	
    }
 
}
