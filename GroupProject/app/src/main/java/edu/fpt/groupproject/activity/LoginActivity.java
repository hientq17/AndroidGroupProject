package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.common.ReturnToken;
import edu.fpt.groupproject.model.user.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnSignUp;
    ImageButton imgBtnBack;
    EditText txtUsername, txtPassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        imgBtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnBack:
                LoginActivity.this.finish();
                break;
            case R.id.btnLogin:
                if(loginValidation()){
                    Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                            .addConverterFactory(GsonConverterFactory.create()) .build();
                    User user = new User(txtUsername.getText().toString(),txtPassword.getText().toString());
                    IUserApi userApi = retrofit.create(IUserApi.class);
                    userApi.login(user).enqueue(new Callback<ReturnToken>() {
                        @Override
                        public void onResponse(Call<ReturnToken> call, Response<ReturnToken> response) {
                            ReturnToken returnToken = response.body();
                            if(returnToken.getReturnModel().isSuccess()){
                                Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token",returnToken.getToken());
                                editor.putString("username",returnToken.getReturnModel().getReturnId());
                                editor.commit();
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            } else if(returnToken.getReturnModel().getMessage().equals("ERROR")) {
                                Toast.makeText(LoginActivity.this,"Tên đăng nhập hoặc mật khẩu không đúng.",Toast.LENGTH_LONG).show();
                            } else{
                                Toast.makeText(LoginActivity.this,"Lỗi hệ thống: "+returnToken.getReturnModel().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ReturnToken> call, Throwable t) {
                            try {
                                throw t;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                }
                break;
            case R.id.btnSignUp:
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean loginValidation() {
        if (txtUsername.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPassword.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}