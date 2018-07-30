/*
 * Copyright 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.history;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;
import com.yline.application.BaseApplication;
import com.yline.base.BaseActivity;

/**
 * The activity for interacting with the scan history.
 */
public final class HistoryActivity extends ListActivity {
	private HistoryManager historyManager;
	
	private ArrayAdapter<HistoryItem> adapter;
	
	private CharSequence originalTitle;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		BaseApplication.addActivity(this);
		
		this.historyManager = new HistoryManager(this);
		adapter = new HistoryItemAdapter(this);
		setListAdapter(adapter);
		View listview = getListView();
		registerForContextMenu(listview);
		originalTitle = getTitle();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		reloadHistoryItems();
	}
	
	private void reloadHistoryItems() {
		Iterable<HistoryItem> items = historyManager.buildHistoryItems();
		adapter.clear();
		for (HistoryItem item : items) {
			adapter.add(item);
		}
		setTitle(originalTitle + " (" + adapter.getCount() + ')');
		if (adapter.isEmpty()) {
			adapter.add(new HistoryItem(null, null, null));
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseApplication.removeActivity(this);
	}
}
