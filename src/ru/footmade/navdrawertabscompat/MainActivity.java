package ru.footmade.navdrawertabscompat;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	private ActionBarDrawerToggle mDrawerToggle;
	
	private int categoryId;
	private static final String categoryIdKey = "categoryId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Getting a reference to the drawer listview
		ListView mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting a reference to the sidebar drawer ( Title + ListView )
		final LinearLayout mDrawer = (LinearLayout) findViewById(R.id.drawer);

		String[] mSectionTitles = getResources().getStringArray(R.array.nav_items);
		final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSectionTitles));

		// Creating a ToggleButton for NavigationDrawer with drawer event listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(getString(R.string.select_tab));
				supportInvalidateOptionsMenu();
			}
		};

		// Setting event listener for the drawer
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// ItemClick event handler for the drawer items
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				categoryId = position;
				showCurrentCategory();
				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawer);
			}
		});

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		if (savedInstanceState != null) {
			categoryId = savedInstanceState.getInt(categoryIdKey, 0);
		}
		showCurrentCategory();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(categoryIdKey, categoryId);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showCurrentCategory() {
		String title = getResources().getStringArray(R.array.nav_items)[categoryId];
		getSupportActionBar().setTitle(title);
		
		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getSupportFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();
		
		Fragment section = fragmentManager.findFragmentByTag(title);
		if (section == null) {
			switch (categoryId) {
			case 0:
				HashMap<String, String[]> contentsMapping = new LinkedHashMap<String, String[]>();
				contentsMapping.put(getString(R.string.android), getResources().getStringArray(R.array.android));
				contentsMapping.put(getString(R.string.apple), getResources().getStringArray(R.array.apple));
				TabsFragment tabs = new TabsFragment();
				tabs.setContents(contentsMapping);
				section = tabs;
				break;
			case 1:
				contentsMapping = new LinkedHashMap<String, String[]>();
				contentsMapping.put(getString(R.string.planets), getResources().getStringArray(R.array.planets));
				contentsMapping.put(getString(R.string.stars), getResources().getStringArray(R.array.stars));
				contentsMapping.put(getString(R.string.constellations), getResources().getStringArray(R.array.constellations));
				tabs = new TabsFragment();
				tabs.setContents(contentsMapping);
				section = tabs;
				break;
			case 2:
				section = new StubFragment();
				break;
			}
		}
		ft.replace(R.id.content_frame, section, title);

		// Committing the transaction
		ft.commit();
	}
	
	public static class StubFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater,
				ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_stub, container, false);
		}
	}
}