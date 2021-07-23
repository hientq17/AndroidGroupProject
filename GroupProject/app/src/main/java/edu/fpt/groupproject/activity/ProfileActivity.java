package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtName, txtPhone, txtAddress;
    SharedPreferences sharedPreferences;
    ImageButton imgBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.getUser(sharedPreferences.getString("username",null),sharedPreferences.getString("token",null))
                .enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                    Log.e("TEST", response.body().toString());
                    txtName.setText(response.body().getName());
                    txtPhone.setText("Điện thoại: "+response.body().getPhone());
                    txtAddress.setText("Địa chỉ: "+response.body().getAddress());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgBtnBack:
                ProfileActivity.this.finish();
                break;
        }
    }
}
