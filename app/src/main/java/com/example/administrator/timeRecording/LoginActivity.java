package com.example.administrator.timeRecording;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.content.Intent;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.timeRecording.greendao.UserInfoDao;
import com.example.administrator.timeRecording.utils.AppManager;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.administrator.timeRecording.DbOperator.getConnection;

/**
 *
 * 名称：LoginActivity
 * 主要内容：用于用户登陆，目前采用数据库登陆，要修改为后台验证登陆
 * 创建人：
 * 创建时间：
 */

public class LoginActivity extends BaseActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private UserInfoDao userInfoDao;
    //以下两个参数用于显示时间
    private TextView sys_time;
    private static final int msgKey1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        //测试用
        mEmailView.setText("123@123.com");
        mPasswordView.setText("123456");

        //在密码编辑界面判断软键盘，正确后尝试登陆
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                //判断软键盘
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        TextView mRegister = (TextView) findViewById(R.id.tv_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        /*
         *注册活动跳转
         */
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        /*
        * 实时时间显示
         */
        sys_time = (TextView)findViewById(R.id.timeText);
        new TimeThread().start();

    }


    /**
     * 检查表单内容，内容正确则进行登录
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        //设置输入框的错误提示为空
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        // 获取输入框的邮箱和密码
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //格式（不能为空，不能小于4位）如果格式错误重新获得焦点，并提示错误内容
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            //如果格式错误，输入框重新获得输入焦点
            focusView.requestFocus();
        } else {
            //如果输入的格式正确，显示验证等待对话框，并启动验证线程
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password); //通过JDBC，登录mysql数据库检测
            mAuthTask.execute((Void) null);
            ////***********************//////
        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    //

    /**
     * Shows the progress UI and hides the login form.//指出应用程序的API版本
     * 显示过渡UI，隐藏登录文本框
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        //获取运行平台的版本与应用的版本对比实现功能的兼容性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            //获取系统定义时间
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);//设置验证对话框为可显示
            //设置动画时间
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)//设置动画渐变效果
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                            //跟据参数控制该控件显示或隐藏
                        }
                    });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);//设置输入界面可显
            mProgressView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        } else {
            // 跟据参数控制该控件显示或隐藏
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.后台用户验证
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            boolean loginFlag = false;

            //使用数据库登陆
/*            DaoSession daoSession = ((myApp)getApplication()).getDaoSession();
            userInfoDao = daoSession.getUserInfoDao();
            QueryBuilder<UserInfo> userQB = userInfoDao.queryBuilder();

            if(userQB.where(UserInfoDao.Properties.Email.eq(mEmail),UserInfoDao.Properties.Password.eq(mPassword)).list().size() > 0){
                  return true;
            }else {
                showToast("登录错误！");
                  return false;
            }*/

            //直连后台数据库，查找用户信息，并登陆
            try {
                Class.forName("com.mysql.jdbc.Driver");
                try {
                    Connection cn;
//                    cn = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.1.200:3306/INFO","root","123456");
                    cn = (Connection) getConnection();

                    String sql="select * from userInfo";
/*                    PreparedStatement pst = cn.prepareStatement(sql);
                    ResultSet rs = (ResultSet) pst.executeQuery();*/
                    Statement st = cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        String userEmail = rs.getString("user_email");
                        String userPass = rs.getString("user_password");
                        String userName = rs.getString("user_name");
                        if (userEmail.equals(mEmail) && userPass.equals(mPassword)) {
                            showToast("欢迎："+userName+" 登陆时间管理系统！");
                            loginFlag = true;
                        }
                        if(loginFlag)
                            break;
                    }
                    //关闭数据库相关资源
                    if(cn!=null)
                        cn.close();
                    if (st!=null)
                        st.close();
                    if (rs!=null)
                        rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //************************* end ******************************
            return loginFlag;

        }

        @Override
        //线程结束后的ui处理
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);//隐藏验证延时对话框

            if (success) {
//                showToast("登录成功！");
                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                in.putExtra("email",mEmail);
                startActivity(in);
                finish();
            } else {
                //密码错误，输入框获得焦点，并提示错误
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mEmailView.requestFocus();
            }
        }

        //取消验证
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /***********************revised begin*******************************/
    /*
    * 用于时间显示的线程处理程序
    * revised at 2019-04-21
     */
    public class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
                    sys_time.setText(format.format(date));
                    break;
                    default:
                        break;
            }
        }
    };
    /**************revised at 2019-04-21*******************/


    //用于toast调用
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
             AppManager.getAppManager().AppExit();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }



}
