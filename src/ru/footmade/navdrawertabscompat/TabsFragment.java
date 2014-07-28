package ru.footmade.navdrawertabscompat;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabsFragment extends Fragment {
	private static final String contentsKey = "contentsMapping";
	
	private HashMap<String, String[]> contentsMapping;
	
	public void setContents(HashMap<String, String[]> contentsMapping) {
		this.contentsMapping = contentsMapping;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			@SuppressWarnings("unchecked")
			HashMap<String, String[]> contents = (HashMap<String, String[]>) savedInstanceState.getSerializable(contentsKey);
			setContents(contents);
		}
		View result = inflater.inflate(R.layout.fragment_tabs, container, false);
		TabHost tabHost = (TabHost) result.findViewById(android.R.id.tabhost);
		tabHost.setup();
		// Defining Tab Change Listener event. This is invoked when tab is changed
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();

				Fragment changed = fm.findFragmentByTag(tabId);
				if (changed == null) {
					ArrayFragment fragment = new ArrayFragment();
					fragment.setContent(contentsMapping.get(tabId));
					changed = fragment;
				}
				ft.replace(R.id.realtabcontent, changed, tabId);
				ft.commit();
			}
		});
		for (String title : contentsMapping.keySet()) {
			setupTab(tabHost, title);
		}
		return result;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(contentsKey, contentsMapping);
	}
	
	private void setupTab(TabHost tabHost, String tag) {
		View tabview = createTabView(tag);
	    TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				return new View(getActivity());
			}
		});
		tabHost.addTab(setContent);
	}

	private View createTabView(String text) {
		ViewGroup root = null; // Stop whining, Lint
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.tabs_bg, root);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}
