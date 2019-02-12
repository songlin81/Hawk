package com.chubbymobile.wwh.hawk.Logon;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chubbymobile.wwh.hawk.DBHelper;
import com.chubbymobile.wwh.hawk.DBLoginInfoObj;
import com.chubbymobile.wwh.hawk.LoginInfoHandler;
import com.chubbymobile.wwh.hawk.R;
import com.chubbymobile.wwh.hawk.UOptionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class LogQQActivity extends Activity implements Handler.Callback {

    private PopupWindow mPopupWindow;// 浮动窗口
    private UOptionsAdapter mOptionsAdapter;//适配器
    private List<String> mUserNames;// 保存用户名
    private List<String> mPassWords;// 保存密码
    private List<DBLoginInfoObj> infoObjs;// 保存登陆信息数据对象
    private LinearLayout parent;//浮动窗口依附布局
    private int pwidth;// 浮动宽口的宽度

    private EditText login_edit_userName;// 账号输入框
    private EditText login_edit_password;// 密码输入框
    private Button down_but;
    private Button login_but;
    private Handler mHandler;// 处理消息更新UI
    private boolean init_flag = false;// 浮动窗口显示标示符
    private static final String TBL_LOGIN_INFO = "LoginInfo";// 用户登陆信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logqq);
        // 初始化
        this.mUserNames = new ArrayList<String>();
        this.mPassWords = new ArrayList<String>();
        this.infoObjs = new ArrayList<DBLoginInfoObj>();
        this.mHandler = new Handler(this);
    }

    /**
     * 在此方法中初始化可以获得输入框的宽度，以便于创建同样宽的浮动窗口
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        while (!init_flag) {
            // 初始化UI
            initWedget();
            // 初始化浮动窗口
            initPopuWindow();
            init_flag = true;
        }
    }

    /**
     * 初始化UI控件
     */
    private void initWedget() {
        // 浮动窗口依附的布局
        this.parent = this.findViewById(R.id.username_layout);
        this.login_edit_userName = this.findViewById(R.id.login_edit_userName);
        this.login_edit_password = this.findViewById(R.id.login_edit_password);
        this.down_but = this.findViewById(R.id.down_but);
        login_but = this.findViewById(R.id.login_but_landing);
        // 获取登陆数据
        getData();

        if(this.mUserNames.size()!=0) {
            this.login_edit_userName.setText(this.mUserNames.get(0));
            this.login_edit_password.setText(this.mPassWords.get(0));
        }
        // 获取地址输入框的宽度，用于创建浮动窗口的宽度
        int w = parent.getWidth();
        pwidth = w;

        down_but.setOnClickListener(Listener_but);
        login_but.setOnClickListener(Listener_but);
    }

    /**
     * 初始化浮动窗口
     */
    public void initPopuWindow() {
        // 浮动窗口的布局
        View loginwindow = (View) this.getLayoutInflater().inflate(
                R.layout.activity_options, null);
        ListView listView = (ListView) loginwindow.findViewById(R.id.list);
        // 初始化适配器
        this.mOptionsAdapter = new UOptionsAdapter(LogQQActivity.this,
                mUserNames, mHandler);
        listView.setAdapter(mOptionsAdapter);
        // 定义一个浮动窗口，并设置
        this.mPopupWindow = new PopupWindow(loginwindow, pwidth,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    /**
     * 获取登录用户名数据
     */
    private void getData() {
        //获取数据对象
        LoginInfoHandler mHandler = LoginInfoHandler.create(LogQQActivity.this);
        this.infoObjs = mHandler.getLoginInfoObj();
        for (int i = 0; i < this.infoObjs.size(); i++) {
            this.mUserNames.add(this.infoObjs.get(i).getUserName());
            this.mPassWords.add(this.infoObjs.get(i).getPassword());
        }
    }

    View.OnClickListener Listener_but = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.down_but:// 浮动地址下拉框按钮事件
                    if (init_flag) {
                        // 显示浮动窗口
                        mPopupWindow.showAsDropDown(parent, 0, -3);
                    }
                    break;
                case R.id.login_but_landing:// 登录按钮事件
                    // 登录成功将数据保存到SQLite中
                    ContentValues values = new ContentValues();
                    String userName = login_edit_userName.getText().toString();
                    String passWord = login_edit_password.getText().toString();
                    values.put("UserName", userName);
                    values.put("PassWord", passWord);
                    DBHelper dbHelper = new DBHelper(LogQQActivity.this);
                    // 第一次登陆直接保存
                    if (mUserNames.size() == 0) {
                        dbHelper.insert(values, TBL_LOGIN_INFO);
                    } else {
                        // 标示符是否要插入
                        boolean flag = false;
                        // 判断当前用户名是否在数据库存在
                        LoginInfoHandler mHandler = LoginInfoHandler
                                .create(LogQQActivity.this);
                        List<DBLoginInfoObj> info = mHandler.getLoginInfoObj();
                        for (int i = 0; i < info.size(); i++) {
                            String username = info.get(i).getUserName();
                            if (userName.equals(username)) {
                                flag = true;
                                Log.d("zzz", "sss");
                            }
                        }
                        if (!flag) {
                            Log.d("zzzNop", "sss");
                            dbHelper.insert(values, TBL_LOGIN_INFO);
                        }
                    }

                    Toast.makeText(LogQQActivity.this, "登陆成功", Toast.LENGTH_LONG);

//                    Intent i = new Intent(LogQQActivity.this, MainActivity.class);
//                    startActivity(i);
                    break;
            }
        }
    };

    /**
     * 处理浮动窗口传回来的数据
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        Bundle bundle = msg.getData();
        DBHelper dbHelper = new DBHelper(LogQQActivity.this);
        switch (msg.what) {
            case 1:// 根据返回的id，将数据显示在输入框中
                int sel_id = bundle.getInt("sel_id");
                int _id = infoObjs.get(sel_id).getID();
                String UserName = infoObjs.get(sel_id).getUserName();
                String PassWord = infoObjs.get(sel_id).getPassword();
                // 先删除再插入
                dbHelper.del(_id, TBL_LOGIN_INFO);
                login_edit_userName.setText(UserName);
                login_edit_password.setText(PassWord);
                mPopupWindow.dismiss();
                break;
            case 2:// 根据返回的ID，删除数据
                int del_id = bundle.getInt("del_id");
                int id = infoObjs.get(del_id).getID();
                dbHelper.del(id, TBL_LOGIN_INFO);
                mUserNames.remove(del_id);
                mOptionsAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }
}