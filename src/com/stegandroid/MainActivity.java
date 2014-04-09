package com.stegandroid;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.stegandroid.activities.GeneralActivity;
import com.stegandroid.activities.SettingsActivity;
import com.stegandroid.activities.SocialNetworkActivity;

/* To do (or not): 
 * Find a way to create tab without using TabActivity
 * since it is deprecated
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private TabHost _tabhost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TabSpec generalTab;
		TabSpec	optionsTab;
		TabSpec	socialNetworkTab;
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _tabhost = getTabHost();
        _tabhost.setup();
        
        generalTab = _tabhost.newTabSpec(getResources().getString(R.string.tabhost_general_tab_title));
        generalTab.setIndicator(getResources().getString(R.string.tabhost_general_tab_title));
        generalTab.setContent(new Intent(this, GeneralActivity.class));
        _tabhost.addTab(generalTab);
        
        optionsTab = _tabhost.newTabSpec(getResources().getString(R.string.tabhost_settings_tab_title));
        optionsTab.setIndicator(getResources().getString(R.string.tabhost_settings_tab_title));
        optionsTab.setContent(new Intent(this, SettingsActivity.class));
        _tabhost.addTab(optionsTab);
        
        socialNetworkTab = _tabhost.newTabSpec(getResources().getString(R.string.tabhost_socialnetwork_tab_title));
        socialNetworkTab.setIndicator(getResources().getString(R.string.tabhost_socialnetwork_tab_title));
        socialNetworkTab.setContent(new Intent(this, SocialNetworkActivity.class));
        _tabhost.addTab(socialNetworkTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
