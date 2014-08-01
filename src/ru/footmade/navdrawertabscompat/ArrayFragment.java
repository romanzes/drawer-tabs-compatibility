package ru.footmade.navdrawertabscompat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ArrayFragment extends ListFragment {

	// An array of items to display in ArrayList
	private static final String itemsKey = "items";
	private String[] items;
	
	public void setContent(Context context, int resourceId) {
		setContent(context.getResources().getStringArray(resourceId));
	}
	
	public void setContent(String[] items) {
		this.items = items;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Load saved items
		Bundle arguments = getArguments();
		if (arguments != null) {
			setContent(arguments.getStringArray(itemsKey));
		}
		
		// Creating array adapter to set data in listview
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
				android.R.layout.simple_list_item_1, items);

		// Setting the array adapter to the listview
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArray(itemsKey, items);
	}
}

