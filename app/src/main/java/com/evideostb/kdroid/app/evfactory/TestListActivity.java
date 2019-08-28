package com.evideostb.kdroid.app.evfactory;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.evideostb.kdroid.app.evfactory.constant.Constant;

public class TestListActivity extends ListActivity implements CompoundButton.OnCheckedChangeListener {
    private TestListAdpter mAdpter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdpter = new TestListAdpter(getApplicationContext());
        this.setListAdapter(mAdpter);

        setActionBarOptions();
    }

    private void setActionBarOptions() {
        View v = getLayoutInflater().inflate(R.layout.test_list_options, null);

        SharedPreferences sp = getSharedPreferences(Constant.TEST_SINGLE_MODE, MODE_PRIVATE);
        CheckBox cb = (CheckBox) v.findViewById(R.id.single_mode);
        cb.setChecked(sp.getBoolean(Constant.TEST_SINGLE_MODE, false));
        cb.setOnCheckedChangeListener(this);

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        getActionBar().setCustomView(v, lp);

        int flags = ActionBar.DISPLAY_SHOW_CUSTOM;
        int options = getActionBar().getDisplayOptions() ^ flags;
        getActionBar().setDisplayOptions(options, flags);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sp = getSharedPreferences(Constant.TEST_SINGLE_MODE, MODE_PRIVATE);
        sp.edit().putBoolean(Constant.TEST_SINGLE_MODE, isChecked).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdpter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, TestItemActivity.class);
        intent.putExtra("ITEM_INDEX", position);
        getApplicationContext().startActivity(intent);
    }

    class TestListAdpter extends BaseAdapter{
        LayoutInflater mInflater;
        Context mContext;

        public TestListAdpter(Context context) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.from(mContext).inflate(R.layout.list_item, null);

                holder = new ViewHolder();
                holder.textView1 = (TextView) convertView.findViewById(R.id.text1);
                holder.textView2 = (TextView) convertView.findViewById(R.id.text2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            setTitleView(position, holder.textView1);
            setResultView(position, holder.textView2);

            return convertView;
        }

        private void setTitleView(final int position, TextView tv) {
            tv.setText(String.format("%s、%s", String.valueOf(position + 1), TestList.get(position).getTestName()));
        }

        private void setResultView(final int position, TextView tv) {
            SharedPreferences sp = getSharedPreferences(Constant.TEST_ITEM_RESULT, MODE_PRIVATE);
            int result = sp.getInt(String.valueOf(position), -1);
            if (result == 1) {
                tv.setText(R.string.pass_text);
                tv.setTextColor(Color.GREEN);
            } else if (result == 0){
                tv.setText(R.string.fail_text);
                tv.setTextColor(Color.RED);
            } else {
                tv.setText("");
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return TestList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getCount() {
            return TestList.getCount();
        }

        final class ViewHolder {
            TextView textView1;
            TextView textView2;
        }
    }
}
