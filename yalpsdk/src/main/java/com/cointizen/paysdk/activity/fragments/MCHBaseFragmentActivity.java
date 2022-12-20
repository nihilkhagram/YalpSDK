package com.cointizen.paysdk.activity.fragments;

import androidx.fragment.app.FragmentActivity;

import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * Created by Administrator on 2017/3/21.
 */

public class MCHBaseFragmentActivity extends FragmentActivity {

    protected int getLayout(String layoutName) {
        return MCHInflaterUtils.getLayout(this, layoutName);
    }

    protected int getId(String idName){
        return MCHInflaterUtils.getControl(this, idName);
    }

    protected int getDrawable(String draname) {
        return MCHInflaterUtils.getDrawable(this, draname);
    }

}
