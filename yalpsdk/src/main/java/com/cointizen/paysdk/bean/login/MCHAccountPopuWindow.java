package com.cointizen.paysdk.bean.login;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cointizen.paysdk.db.DBAdapter;
import com.cointizen.paysdk.db.UserInfo;
import com.cointizen.paysdk.listener.OnMultiClickListener;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator
 * on 2016/9/30.
 */
public class MCHAccountPopuWindow implements AdapterView.OnItemClickListener{
   public Activity act;
    private List<String> numbers=new ArrayList<String>();
    private NumbersAdapter adapter;
    private PopupWindow popupWindow;
    public EditText etNumber;
    public EditText etPassword;

    /**
     * 弹出选择号码对话框
     */
    public void showSelectNumberDialog() {
        //每次弹出弹窗查找用户登录列表
        DBAdapter dbAdapter=new DBAdapter(act);
        dbAdapter.open();
        UserInfo users []=null;
        try {
            users=dbAdapter.getAllData();
            for (UserInfo user:users) {
                numbers.add(user.account);
            }
            if (numbers.size()<=0)
                return;
            //颠倒number里面东西的顺序
            List<String> temp=new ArrayList<String>();
            for(int i=0;i<numbers.size();i++){
                temp.add(i,numbers.get(numbers.size()-1-i));
            }
            numbers=temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbAdapter.close();
        ListView lv = new ListView(act);
        // 隐藏滚动条
        lv.setVerticalScrollBarEnabled(false);
        // 让listView没有分割线
        lv.setDividerHeight(0);
        lv.setDivider(null);
        lv.setOnItemClickListener(this);

        adapter = new NumbersAdapter();
        lv.setAdapter(adapter);

        popupWindow = new PopupWindow(lv, etNumber.getWidth() - 4, 200);
        // 设置点击外部可以被关闭
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置popupWindow可以得到焦点
        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(etNumber, 2, -5); // 显示

    }

    /**
     * 适配器
     */

    class NumbersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return numbers.size();
        }

        @Override
        public Object getItem(int position) {
            return numbers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NumberViewHolder mHolder = null;
            final StringBuffer account= new StringBuffer("");
            if (convertView == null) {
                mHolder = new NumberViewHolder();
                convertView = LayoutInflater.from(act).inflate(
                        MCHInflaterUtils.getIdByName(act,
                                "layout", "mch_item_spinner_numbers"), null);
                mHolder.tvNumber = (TextView) convertView
                        .findViewById(MCHInflaterUtils.getIdByName(act,
                                "id", "tv_number"));
                mHolder.ibDelete = (ImageButton) convertView
                        .findViewById(MCHInflaterUtils.getIdByName(act,
                                "id", "ib_delete"));
                convertView.setTag(mHolder);
            } else {
                mHolder = (NumberViewHolder) convertView.getTag();
            }

            mHolder.tvNumber.setText(numbers.get(position));
            account.append(mHolder.tvNumber.getText().toString().trim());
            mHolder.ibDelete.setTag(position);
            mHolder.ibDelete.setOnClickListener(new OnMultiClickListener() {

                @Override
                public void onMultiClick(View v) {
                    int index = (Integer) v.getTag();
                    numbers.remove(index);
                    adapter.notifyDataSetChanged();

                    //如果存在删除原有记录
                    DBAdapter dbDelete=new DBAdapter(act);
                    dbDelete.open();
                    dbDelete.deleteByAccount(account.toString());
                    dbDelete.close();

                    if (numbers.size() == 0) {
                        popupWindow.dismiss();
                    }
                }
            });

            return convertView;
        }

    }

    public class NumberViewHolder {
        public TextView tvNumber;
        public ImageButton ibDelete;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       etNumber.setText(numbers.get(i));
        //根据用户名查找密码
        DBAdapter dbGetOne=new DBAdapter(act);
        dbGetOne.open();
        String password=null;
        try {
            UserInfo user = dbGetOne.getOneData(numbers.get(i));
            password=user.password;
        }catch (Exception e){

        }
        dbGetOne.close();
        etPassword.setText(password);
       popupWindow.dismiss();
    }
}
