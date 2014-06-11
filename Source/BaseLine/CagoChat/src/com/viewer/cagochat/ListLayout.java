package com.viewer.cagochat;

import android.os.Bundle;
import android.app.ListActivity;

 
public class ListLayout extends ListActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        String str[] = new String[] { "C", "C++", "Java", "Android" };
        ListLayoutAdapter adapter = new ListLayoutAdapter(this, str);
        
        setListAdapter(adapter);
    }
 
}
