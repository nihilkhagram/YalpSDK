package com.cointizen.paysdk.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cointizen.paysdk.activity.fragments.MCHBanlanceBBFragment;
import com.cointizen.paysdk.activity.fragments.MCHBanlanceJBFragment;
import com.cointizen.paysdk.activity.fragments.MCHBanlanceJFFragment;
import com.cointizen.paysdk.activity.fragments.MCHBanlancePTBFragment;

/**
 * 描述：余额adapter
 * 时间: 2018-09-26 14:45
 */

public class MCHBanlancePagerAdapter extends FragmentPagerAdapter{

    private MCHBanlancePTBFragment banlancePTBFragment;
    private MCHBanlanceBBFragment banlanceBBFragment;
    private MCHBanlanceJFFragment banlanceJFFragment;
    private MCHBanlanceJBFragment banlanceJBFragment;
    private int anInt;

    public MCHBanlancePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPager(int pager){
        anInt = pager;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
//        if (GamePayTypeEntity.isHavePTB){
//            switch (position){
//                case 0:
//                    if (banlancePTBFragment==null){
//                        banlancePTBFragment = new BanlancePTBFragment();
//                    }
//                    return banlancePTBFragment;
//                case 1:
//                    if (banlanceBBFragment==null){
//                        banlanceBBFragment = new BanlanceBBFragment();
//                    }
//                    return banlanceBBFragment;
//                case 2:
//                    if (banlanceJFFragment==null){
//                        banlanceJFFragment = new BanlanceJFFragment();
//                    }
//                    return banlanceJFFragment;
//                case 3:
//                    if (banlanceJBFragment==null){
//                        banlanceJBFragment = new BanlanceJBFragment();
//                    }
//                    return banlanceJBFragment;
//            }
//        }else {
//            switch (position){
//                case 0:
//                    if (banlanceBBFragment==null){
//                        banlanceBBFragment = new BanlanceBBFragment();
//                    }
//                    return banlanceBBFragment;
//                case 1:
//                    if (banlanceJFFragment==null){
//                        banlanceJFFragment = new BanlanceJFFragment();
//                    }
//                    return banlanceJFFragment;
//                case 2:
//                    if (banlanceJBFragment==null){
//                        banlanceJBFragment = new BanlanceJBFragment();
//                    }
//                    return banlanceJBFragment;
//            }
//        }
        switch (position){
            case 0:
                if (banlancePTBFragment==null){
                    banlancePTBFragment = new MCHBanlancePTBFragment();
                }
                return banlancePTBFragment;
            case 1:
                if (banlanceBBFragment==null){
                    banlanceBBFragment = new MCHBanlanceBBFragment();
                }
                return banlanceBBFragment;
            case 2:
                if (banlanceJFFragment==null){
                    banlanceJFFragment = new MCHBanlanceJFFragment();
                }
                return banlanceJFFragment;
            case 3:
                if (banlanceJBFragment==null){
                    banlanceJBFragment = new MCHBanlanceJBFragment();
                }
                return banlanceJBFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return anInt;
    }

    public void setData(int i, String balance) {
        switch (i){
            case 1:
                if (banlancePTBFragment!=null){
                    banlancePTBFragment.setData(balance);
                }
                break;
            case 3:
                if (banlanceJFFragment!=null){
                    banlanceJFFragment.setData(balance);
                }
                break;
            case 4:
                if(banlanceJBFragment!=null){
                    banlanceJBFragment.setData(balance);
                }
                break;
        }
        notifyDataSetChanged();
    }
}
