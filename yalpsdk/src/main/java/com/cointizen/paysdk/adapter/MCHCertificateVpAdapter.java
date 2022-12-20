package com.cointizen.paysdk.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by LeBron on 2017/5/4.
 */

public class MCHCertificateVpAdapter extends FragmentPagerAdapter{
    private List<Fragment> views;

    public MCHCertificateVpAdapter(FragmentManager fm, List<Fragment> views) {
        super(fm);
        this.views = views;
    }


    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

}
