package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.administrator.myapplication.Model.UserInfo;
import com.example.administrator.myapplication.greendao.DaoSession;
import com.example.administrator.myapplication.greendao.UserInfoDao;
import org.greenrobot.greendao.query.QueryBuilder;


/**
 * 名称    ：RegisterActivity
 * 主要内容 :用于新用户注册活动，现用数据库代替注册活动，后面需要再修改代码实现后台注册。
 * 创建人  ：wanzhuang
 * 创建时间：2018.9.18
 * 修改内容: 完成注册活动（即对数据库的操作，实现判断邮箱是否被注册。）
 * 还需要修改的内容：对于注册所需信息的正则表达的判断，如用户名，电话号码，邮箱，密码。
 * 修改时间：2018.9.23
 *
 */
public class RegisterActivity extends Activity {

    private Context mcontext;
    private Button registerButton;
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText telephoneInput;
    private UserInfoDao userInfoDao;
    private String userName;
    private String password;
    private String email;
    private String telNumber;
    private Long id = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mcontext=getApplicationContext();
        init();

    }

    private void init(){
     initView();
     initGreenDao();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              attemptRegister();
            }
        });

    }
    /**
     * 尝试注册操作，主要是判断注册信息是否正确。
     */
    private void attemptRegister(){

        nameInput.setError(null);
        passwordInput.setError(null);
        emailInput.setError(null);
        telephoneInput.setError(null);
        //
        userName = nameInput.getText().toString();
        password = passwordInput.getText().toString();
        email = emailInput.getText().toString();
        telNumber=telephoneInput.getText().toString();
        showToast(telNumber,mcontext);

        boolean cancel = false;
        View focusView = null;

        //username
       if(TextUtils.isEmpty(userName)){
            //TODO: 待修改
            nameInput.setError("用户名不正确");
            focusView = nameInput;
            cancel=true;
        }
        //password
        if(TextUtils.isEmpty(password)){
            passwordInput.setError("密码错误!");
            focusView =emailInput;
            cancel =true;
        }
        //email
        if(TextUtils.isEmpty(email)){
            emailInput.setError("必须填写!");
            focusView =emailInput;
            cancel =true;
        }else if (!isEmailVaild(email)){
            emailInput.setError("邮箱格式错误！");
            focusView = emailInput;
            cancel = true;
        }
        //
        if(cancel){
            focusView.requestFocus();
        }else {
            //信息无误后开始注册操作
            addUserInfo(userName,password,email,telNumber);
        }

    }
    /**
     * 正则表达式
     */
    //TODO: editText判断的修改
    private boolean isUserNameValid(String userName){
        return userName.length()>4;
    }
    private boolean isEmailVaild(String email){
        return email.contains("@");
    }
    private boolean isPasswordValid(String password){
        return password.length()>4;
    }
    private boolean isTelnumberValid(long telNumber){ return true;}
    /**
     * 注册操作
     */
    private void addUserInfo(String userName,String password,String email,String telNumber){
       try {
           //final String s = Long.toString(telNumber);
          // showToast(s,mcontext);
           long tel = Long.parseLong(telNumber);

           UserInfo userInfo = new UserInfo(null,userName,password,email,tel);
           //查询邮箱是否被注册
           QueryBuilder<UserInfo> userQB = userInfoDao.queryBuilder();
           if(userQB.where(UserInfoDao.Properties.Email.eq(email)).list().size() > 0){
               Log.d("RegisterUser","用户已存在");
               showToast("邮箱已被使用，注册失败!",mcontext);
           }else {
               userInfoDao.insert(userInfo);
               showToast("注册成功！",mcontext);
               startActivity(new Intent(this,LoginActivity.class));
           }

       }catch (Exception e){
           Log.d("RegisterUser","Insert UserInfo is failed");
       }
    }
    /**
     *初始化数据库
     */
    private void initGreenDao()
    {
        DaoSession daoSession = ((myApp)getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
    }
    /**
     *页面绑定
     */
    private void initView(){
        nameInput = (EditText) findViewById(R.id.nameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);//邮箱输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//密码输入框
        registerButton = (Button) findViewById(R.id.registerButton);//注册按钮
        telephoneInput =(EditText)findViewById(R.id.telephoneInput);
    }

    /**
     *用于调用Toast
     */
    public void showToast(final String msg,final Context context) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
