package com.cointizen.paysdk.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cointizen.paysdk.activity.fragments.MCHCouponGQFragment;
import com.cointizen.paysdk.activity.fragments.MCHCouponKLFragment;
import com.cointizen.paysdk.activity.fragments.MCHCouponYLFragment;

/**
 * 描述：代金券PagerAdapter
 * 时间: 2020-04-03 10:45
 */

public class MCHCouponPagerAdapter extends FragmentPagerAdapter{

    private MCHCouponKLFragment klFragment;
    private MCHCouponYLFragment ylFragment;
    private MCHCouponGQFragment gqFragment;
    private boolean isbind;

    public MCHCouponPagerAdapter(FragmentManager fm, boolean isbind) {
        super(fm);
        this.isbind = isbind;
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               if (klFragment == null){
                   klFragment = new MCHCouponKLFragment();
                   klFragment.setBind(this.isbind);
               }
               return klFragment;
           case 1:
               if (ylFragment == null){
                   ylFragment = new MCHCouponYLFragment();
                   ylFragment.setBind(this.isbind);
               }
               return ylFragment;
           case 2:
               if (gqFragment == null){
                   gqFragment = new MCHCouponGQFragment();
                   gqFragment.setBind(this.isbind);
               }
               return gqFragment;
       }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
