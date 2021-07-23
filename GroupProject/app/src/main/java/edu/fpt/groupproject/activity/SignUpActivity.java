package edu.fpt.groupproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton imgBtnBack;
    Button btnSignUp;
    EditText txtUsername, txtNewPassword, txtConfirmPassword, txtName, txtAddress, txtPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        txtUsername = findViewById(R.id.txtUsername);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBack:
                SignUpActivity.this.finish();
                break;
            case R.id.btnSignUp:
                if(signUpValidation()){
                    User user = new User();
                    user.setUsername(txtUsername.getText().toString());
                    user.setPassword(txtNewPassword.getText().toString());
                    user.setName(txtName.getText().toString());
                    user.setAddress(txtAddress.getText().toString());
                    user.setPhone(txtPhone.getText().toString());
                    Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                            .addConverterFactory(GsonConverterFactory.create()) .build();
                    IUserApi userApi = retrofit.create(IUserApi.class);
                    userApi.signup(user).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Log.e("SIGNUP",response.body().toString());
                            JSONArray jsonArray = null;
                            String outputMessage = null;
                            try {
                                jsonArray = new JSONArray(response.body().toString());
                                outputMessage = jsonArray.getJSONObject(0).getString("outputMessage");
                                if(outputMessage.equals("SUCCESS")){
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công. Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable(){
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 1500);
                                } else if(outputMessage.equals("EXISTED")){
                                    Toast.makeText(SignUpActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Lỗi hệ thống: "+outputMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            try {
                                throw t;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }

    public boolean signUpValidation(){
        if(txtUsername.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtNewPassword.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!String.valueOf(txtNewPassword.getText()).equals(String.valueOf(txtConfirmPassword.getText()))){
            Toast.makeText(SignUpActivity.this, "Nhập lại mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtName.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập tên của bạn", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtAddress.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtPhone.getText().toString().length()==0){
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}