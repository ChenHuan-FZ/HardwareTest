package com.evideostb.kdroid.app.evfactory.item;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evideostb.kdroid.app.evfactory.R;
import com.evideostb.kdroid.app.evfactory.TestApp;
import com.evideostb.kdroid.app.evfactory.TestItemActivity;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WifiTest extends BaseTest {
    private List<ScanResult> mWifiList;
    private ListView mListView;
    private TextView mTextView;
    private Lock mLock;
    private Condition mCondition;
    private WifiManager mWifiManager;
    private final static int WIFI_SCAN_TIMEOUT = 20;
    private boolean bIsWifiScanComplete = false;
    private boolean bIsDestroyView = false;

    public enum ErrorType {
        SCAN_WIFI_TIMEOUT,
        NO_WIFI_FOUND,
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wifi, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = getView().findViewById(R.id.list);
        mTextView = getView().findViewById(R.id.wifi_text);

        mLock = new ReentrantLock();
        mCondition = mLock.newCondition();

        mWifiManager = (WifiManager)getContext().getApplicationContext().getSystemService(getContext().getApplicationContext().WIFI_SERVICE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bIsDestroyView = true;
        unregisterReceiver();
    }

    class scanWifiTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setButtonEnabled(false);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getString(R.string.wifi_search));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isComplete = false;
            mWifiManager.startScan();

            mLock.lock();

            try {
                mCondition.await(WIFI_SCAN_TIMEOUT, TimeUnit.SECONDS);
                isComplete = bIsWifiScanComplete;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mLock.unlock();
            return isComplete;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            setButtonEnabled(true);

            if (!aBoolean) {
                mListener.onSacnFail(ErrorType.SCAN_WIFI_TIMEOUT);
            }
        }
    }

    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        new scanWifiTask().execute();
    }

    @Override
    public boolean onSingleTapConfirmed() {
        mTextView.setVisibility(View.GONE);
        setButtonVisibility(View.VISIBLE);
        registerReceiver();
        setTimerTask(0, 0, 1000*60);
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.wifi_title);
    }

    @Override
    public boolean isNeedTest() {
        return true;
    }

    class WifiListAdpter extends BaseAdapter {
        LayoutInflater mInflater;
        Context mContext;
        List<ScanResult> mList = new ArrayList<>();

        public WifiListAdpter(Context context, List<ScanResult> list) {
            mInflater = LayoutInflater.from(context);
            mContext = context;

            for(int i = 0 ; i < list.size(); i ++) {
                if (!list.get(i).SSID.isEmpty()) {
                    mList.add(list.get(i));
                }
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.wifi_item, null);

                holder = new ViewHolder();
                holder.textView1 = convertView.findViewById(R.id.text1);
                holder.imageView = convertView.findViewById(R.id.image);
                holder.textView2 = convertView.findViewById(R.id.text2);

                holder.imageView.setImageResource(R.drawable.stat_sys_wifi);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView1.setText(mList.get(position).SSID);
            holder.imageView.setImageLevel(Math.abs(mList.get(position).level));
            holder.textView2.setText(String.valueOf(Math.abs(mList.get(position).level)));

            return convertView;
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
            return mList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        final class ViewHolder {
            TextView textView1;
            TextView textView2;
            ImageView imageView;
        }
    }

    WifiScanCompleteListener mListener = new WifiScanCompleteListener() {
        @Override
        public void onScanSucess() {
            if (!isAdded()) {
                return;
            }

            mWifiList = mWifiManager.getScanResults();
            if (mWifiList != null && mWifiList.size() > 0) {
                mListView.setAdapter(new WifiListAdpter(getContext(), mWifiList));
            }
        }

        @Override
        public void onSacnFail(final ErrorType type) {
            String strErrMsg = "";
            switch (type) {
                case SCAN_WIFI_TIMEOUT:
                    strErrMsg = TestApp.getContext().getString(R.string.wifi_timeout);
                    break;
                case NO_WIFI_FOUND:
                    strErrMsg = TestApp.getContext().getString(R.string.wifi_nofound);
                    break;
                default:
                    return;
            }
            Toast.makeText(TestApp.getContext(), strErrMsg, Toast.LENGTH_LONG).show();
        }
    };

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (mWifiManager.getScanResults().isEmpty()) {
                mListener.onSacnFail(ErrorType.NO_WIFI_FOUND);
            } else {
                mListener.onScanSucess();
            }

            mLock.lock();
            mCondition.signal();
            bIsWifiScanComplete = true;
            mLock.unlock();
        }
    };

    protected void registerReceiver () {
        IntentFilter mFilter = new IntentFilter();
//        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//扫描状态
        getContext().registerReceiver(mReceiver, mFilter);
    }

    protected void unregisterReceiver () {
        getContext().unregisterReceiver(mReceiver);
    }

    private interface WifiScanCompleteListener {
        void onScanSucess();
        void onSacnFail(ErrorType type);
    }

}
