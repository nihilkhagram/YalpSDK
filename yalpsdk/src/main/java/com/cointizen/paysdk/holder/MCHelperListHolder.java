package com.cointizen.paysdk.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.activity.MCHelperDetActivity;
import com.cointizen.paysdk.adapter.MCHHelperGridviewAdapter;
import com.cointizen.paysdk.bean.HelperListBean;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

/**
 * 描述：帮助中心list条目
 * 时间: 2018-09-19 15:58
 */

public class MCHelperListHolder extends BaseHolder<HelperListBean.DataBean> {

    private final Activity activity;
    private final BitmapUtils bitmapUtils;
    private LayoutInflater mInflater;
    private ImageView imgChong;
    private TextView TvChong;
    private GridView gridview;
    private MCHHelperGridviewAdapter MCHHelperGridviewAdapter;

    public MCHelperListHolder(Activity activity, BitmapUtils bitmapUtils) {
        super(activity);
        this.activity = activity;
        this.bitmapUtils = bitmapUtils;
    }

    @Override
    protected void refreshView(final HelperListBean.DataBean s, int position, final Activity activity) {
//        if(s.getFirst_title().equals(HolderConstants.S_vgzcwKRMJR)){
//            imgChong.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"icon_mima_n"));
//        }
//        if(s.getFirst_title().equals(HolderConstants.S_eROxtKFMMA)){
//            imgChong.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"icon_zhanghu_n"));
//        }
//        if(s.getFirst_title().equals(HolderConstants.S_ESbcJjkzdc)){
//            imgChong.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"icon_chongzhi_n"));
//        }
//        if(s.getFirst_title().equals(HolderConstants.S_MuYDyrblaN)){
//            imgChong.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"icon_libao_n"));
//        }
//        if(s.getFirst_title().equals(HolderConstants.S_NZQOFkEcOC)){
//            imgChong.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"icon_changjian_n"));
//        }
//        if(s.getFirst_title().equals(HolderConstants.S_GeobuMsfCN)){
//            imgChong.setBackgroundResource(MCHInflaterUtils.getDrawable(activity,"icon_shopping"));
//        }

        bitmapUtils.display(imgChong,s.getIcon());
        TvChong.setText(s.getFirst_title());
        MCHHelperGridviewAdapter.setData(s.getSecond_title());
        setGridViewHeightBasedOnChildren(gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, MCHelperDetActivity.class);
                intent.putExtra("mark",s.getMark());
                intent.putExtra("title",s.getFirst_title());
                intent.putExtra("pos",i);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    protected View initView(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = MCHInflaterUtils.getLayout(context, "mch_item_helper_lv");
        View inflate = mInflater.inflate(layout, null);
        imgChong = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "img_mch_chong"));
        TvChong = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_chong"));
        gridview = inflate.findViewById(MCHInflaterUtils.getIdByName(context, "id", "mch_gridview_chongzhi"));
        MCHHelperGridviewAdapter = new MCHHelperGridviewAdapter(context);
        gridview.setAdapter(MCHHelperGridviewAdapter);
        return inflate;
    }

    /**
     * 动态设置GridView高度
     * @param gridView
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int col = 4;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        gridView.setLayoutParams(params);
    }
}
