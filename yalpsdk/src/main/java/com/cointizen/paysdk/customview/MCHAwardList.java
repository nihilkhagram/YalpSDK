package com.cointizen.paysdk.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cointizen.open.YalpGamesSdk;
import com.lidroid.xutils.BitmapUtils;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.entity.AwardEntity;
import com.cointizen.paysdk.http.arawd.AwardProcess;
import com.cointizen.paysdk.utils.BitmapHelp;
import com.cointizen.paysdk.utils.MCHInflaterUtils;

import java.util.List;

public class MCHAwardList extends LinearLayout {

    private final String TAG = "MCHAwardList";

    private Context mContext;
    private TextView txtTitle;
    private GridView gridAward;
    private List<String> awardEntityList;
    private boolean isRegister = false;//0实名 1注册

    private BitmapUtils bitmapUtils;

    public MCHAwardList(Context context) {
        super(context);
        initView(context);
    }

    public MCHAwardList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MCHAwardList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MCHAwardList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    //初始化UI，可根据业务需求设置默认值。
    private void initView(Context context) {
        mContext = context;
        int layoutId = MCHInflaterUtils.getLayout(context, "mch_custom_award");
        LayoutInflater.from(context).inflate(layoutId, this, true);
        bitmapUtils = BitmapHelp.getBitmapUtils(mContext.getApplicationContext());
        txtTitle = (TextView) findViewById(MCHInflaterUtils.getControl(context, "txt_award_title"));
        gridAward = (GridView) findViewById(MCHInflaterUtils.getControl(context, "grid_award"));

        isRegister = Integer.parseInt(getTag().toString()) == 0;
        txtTitle.setText(CustomviewConstants.S_ESjrnPRsQv);
        queryAward();

    }

    private void setGridView() {
        int size = awardEntityList.size();
        int length = 50;
        int space = 10;
        DisplayMetrics dm = new DisplayMetrics();

        YalpGamesSdk.getMainActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + space) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LayoutParams.MATCH_PARENT);
        gridAward.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridAward.setColumnWidth(itemWidth); // 设置列表项宽
        gridAward.setHorizontalSpacing(space); // 设置列表项水平间距
        gridAward.setStretchMode(GridView.NO_STRETCH);
        gridAward.setNumColumns(size); // 设置列数量=列表集合数

        GridViewAdapter adapter = new GridViewAdapter(mContext, awardEntityList);
        gridAward.setAdapter(adapter);
    }

    private void queryAward() {
        AwardProcess awardProcess = new AwardProcess();
        awardProcess.isRegister = isRegister;
        awardProcess.post(awardHandle);
    }

    private final Handler awardHandle = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.AWARD_SUCCESS) {
                AwardEntity awardEntity = (AwardEntity)msg.obj;
                if (awardEntity.isOpen && awardEntity.imageUrlList.size() > 0) {
                    setVisibility(VISIBLE);
                    awardEntityList = awardEntity.imageUrlList;
                    setGridView();
                    return false;
                }
            }
            setVisibility(GONE);
            return false;
        }
    });

    private class GridViewAdapter extends BaseAdapter {
        Context context;
        List<String> list;
        public GridViewAdapter(Context _context, List<String> _list) {
            this.list = _list;
            this.context = _context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            int layoutId = MCHInflaterUtils.getLayout(context, "mch_item_award");
            convertView = layoutInflater.inflate(layoutId, null);

            ImageView img = convertView.findViewById(MCHInflaterUtils.getControl(context, "img_item_award"));
//            TextView tvCode = (TextView) convertView.findViewById(R.id.tvCode);
            String url = list.get(position);
            bitmapUtils.display(img, url);
//            tvCity.setText(city.getCityName());
//            tvCode.setText(city.getCityCode());
            return convertView;
        }
    }

}
