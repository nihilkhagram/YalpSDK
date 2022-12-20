package com.cointizen.paysdk.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtils {

    public static void calculateListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter != null) {
            int totalHeight = 0;
            int i = 0;

            for(int len = listAdapter.getCount(); i < len; ++i) {
                View listItem = listAdapter.getView(i, (View)null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
            listView.setLayoutParams(params);
        }
    }
}
