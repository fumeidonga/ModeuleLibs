package com.rebeau.technology.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.demo.AppAboutFragment;
import com.rebeau.technology.demo.TestSerializable;
import com.rebeau.views.loading.RBLoadStatusView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends BaseFragmentActivity {

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.text2)
    TextView textView;

    @BindView(R.id.text3)
    TextView textViewWifi;

    private int lastX = 0;
    private int lastY = 0;

    @Override
    protected View createSuccessView() {
        RBLogUtil.dt();
        View view = LayoutInflater.from(this).inflate(R.layout.activity_error, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RBLogUtil.dt();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RBLogUtil.dt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RBLogUtil.dt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RBLogUtil.dt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RBLogUtil.dt();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        imageView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;

                    int left = v.getLeft() + dx;
                    int top = v.getTop() + dy;
                    int right = v.getRight() + dx;
                    int bottom = v.getBottom() + dy;

                    v.layout(left, top, right, bottom);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();

                    Rect localRect = new Rect();
                    //getLocalVisibleRect的作用是获取视图本身可见的坐标区域，坐标以自己的左上角为原点（0，0）
                    v.getLocalVisibleRect(localRect);
                    StringBuilder sb = new StringBuilder();
                    sb.append("getLocalVisibleRect : " + localRect.toString());
                    sb.append("\n");

                    Rect globalRect = new Rect();
                    Point globalOffset = new Point();
                    //getGlobalVisibleRect方法的作用是获取视图在屏幕坐标中的可视区域
                    v.getGlobalVisibleRect(globalRect, globalOffset);
                    sb.append("getGlobalVisibleRect : " + globalRect.toString());
                    sb.append("\n");
                    sb.append("View原点偏离屏幕坐标原点的距离 : " + globalOffset.x + "," + globalOffset.y);
                    sb.append("\n");

                    final long visibleViewArea = (long) globalRect.height()
                            * globalRect.width();
                    final long totalViewArea = (long) imageView.getHeight() * imageView.getWidth();
                    sb.append("visibleViewArea : " + visibleViewArea);
                    sb.append("\n");
                    sb.append("totalViewArea : " + totalViewArea);
                    sb.append("\n");

                    int[] location = new int[2];
                    imageView.getLocationOnScreen(location);
                    int left1 = location[0];
                    int top1 = location[1];
                    int right1 = left1 + imageView.getMeasuredWidth();
                    int bottom1 = top1 + imageView.getMeasuredHeight();
                    sb.append("click pos  : (" + left1 + ", " + top1 + ", " + right1 + ", " + bottom1 + ")");
                    sb.append("\n");

                    textView.setText(sb.toString());
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @Override
    protected String getTitleBarName() {
        return "开发奇淫技巧";
    }

    @OnClick(R.id.array_map)
    public void array_map() {

        MarkdownUtils.setData(this, "android/two/array_map_error.md");
    }

    @OnClick(R.id.button17)
    public void button17() {
        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.chaozh.iReaderFree", "com.chaozh.iReader.ui.activity.WelcomeActivity"));
//        intent.setComponent(new ComponentName("com.kmxs.reader", "com.km.app.home.view.LoadingActivity"));
//        intent.setComponent(new ComponentName("com.sina.weibo", "com.sina.weibo.MainTabActivity"));
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity"));
        intent.putExtra("anykey", new TestSerializable());
        startActivity(intent);

        /*Intent intent=new Intent();
        //包名 包名+类名（全路径）
        intent.setClassName("com.kmxs.reader", "com.km.app.home.view.LoadingActivity");
        startActivity(intent);*/
    }

    @OnClick(R.id.button18)
    public void array_map18() {
        MarkdownUtils.setData(this, "android/thread/定时任务.md");

    }

    @OnClick(R.id.button19)
    public void array_map19() {
        MarkdownUtils.setData(this, "android/问题集锦.md");
    }

    @OnClick(R.id.button20)
    public void array_map20() {

    }

    /**
     * wifi
     */
    @OnClick(R.id.button21)
    public void array_map21() {
        getWifiInfo();
    }
    @OnClick(R.id.button22)
    public void array_map22() {

    }


    /**
     * 获取Wifi信息
     * 这样/data/misc/wifi/wpa_supplicant.conf 文件的内容就被保存下来了，
     * 接下来只需要解析获取到的内容就可以了
     */
    private void getWifiInfo() {
        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            dataOutputStream.writeBytes("cat /data/misc/wifi/wpa_supplicant.conf\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            return;
        } finally {
            if (TextUtils.isEmpty(wifiConf.toString())) {
                textViewWifi.setText("请先获取Root权限...");
            }

            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }

        ArrayList<WifiInfo> mWifiInfoList = new ArrayList<>();

        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString());
        WifiInfo wifiInfo;
        while (networkMatcher.find()) {
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);
            if (ssidMatcher.find()) {
                wifiInfo = new WifiInfo();
                wifiInfo.setName(ssidMatcher.group(1));
                Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                Matcher pskMatcher = psk.matcher(networkBlock);
                if (pskMatcher.find()) {
                    wifiInfo.setPassword(pskMatcher.group(1));
                } else {
                    wifiInfo.setPassword("无密码");
                }
                mWifiInfoList.add(wifiInfo);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 列表倒序
        Collections.reverse(mWifiInfoList);
        for(WifiInfo wifiInfo1 : mWifiInfoList) {
            stringBuilder.append("name : " + wifiInfo1.getName() + " password : " + wifiInfo1.getPassword() );
            stringBuilder.append("\n");
        }
        textViewWifi.setText(stringBuilder.toString());
    }

    public class WifiInfo {
        private String name;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
