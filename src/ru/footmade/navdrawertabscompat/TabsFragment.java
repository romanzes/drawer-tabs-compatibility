package ru.footmade.navdrawertabscompat;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		FragmentTabHost tabHost = (FragmentTabHost) result.findViewById(android.R.id.tabhost);
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
		for (String tabId : contentsMapping.keySet()) {
			Bundle bundle = new Bundle();
			bundle.putStringArray("items", contentsMapping.get(tabId));
			ViewGroup root = null;
			View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tabs_bg, root);
			TextView tv = (TextView) indicator.findViewById(R.id.tabsText);
			tv.setText(tabId);
			tabHost.addTab(tabHost.newTabSpec(tabId).setIndicator(indicator), ArrayFragment.class, bundle);
		}
		return result;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(contentsKey, contentsMapping);
	}
}
