package com.cointizen.paysdk.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cointizen.paysdk.bean.UserInfoBean;
import com.cointizen.paysdk.callback.PopWindowClearCallback;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.List;

/**
 * 描述：自定义PopupWindow  主要用来显示ListView
 * 作者：Administrator
 * 时间: 2018-03-10 9:57
 */

public class SpinerPopWindow extends PopupWindow {
    private LayoutInflater inflater;
    private ListView mListView;
    private List<UserInfoBean> list;
    private MyAdapter mAdapter;
    private Context context;

    public SpinerPopWindow(Context context, List<UserInfoBean> list, AdapterView.OnItemClickListener clickListener, PopWindowClearCallback popWindowClearCallback) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        init(clickListener, popWindowClearCallback);
    }


    private void init(AdapterView.OnItemClickListener clickListener, PopWindowClearCallback popWindowClearCallback) {
        View view = inflater.inflate(MCHInflaterUtils.getLayout(context, "mch_spiner_window"), null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(MCHInflaterUtils.getIdByName(context, "id", "lv_mch_spiner_window"));
        mListView.setAdapter(mAdapter = new MyAdapter(popWindowClearCallback));
        mListView.setOnItemClickListener(clickListener);
    }

    private class MyAdapter extends BaseAdapter {
        private PopWindowClearCallback popWindowClearCallback;

        public MyAdapter(PopWindowClearCallback popWindowClearCallback) {
            this.popWindowClearCallback = popWindowClearCallback;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position).getAccount();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(MCHInflaterUtils.getLayout(context, "mch_item_spiner_window"), null);
                holder.tvName = (TextView) convertView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "tv_mch_spiner_window"));
                holder.rlClear = (RelativeLayout) convertView.findViewById(MCHInflaterUtils.getIdByName(context, "id", "rl_mch_spiner_window_clear"));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            UserInfoBean userInfoBean = list.get(position);
            if(userInfoBean!=null&&userInfoBean.getAccount()!=null){
                holder.tvName.setText(userInfoBean.getAccount());
            }
            holder.rlClear.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (popWindowClearCallback != null) {
                        popWindowClearCallback.reslut(position);
                    }
                }
            });
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvName;
        private RelativeLayout rlClear;
    }

}
