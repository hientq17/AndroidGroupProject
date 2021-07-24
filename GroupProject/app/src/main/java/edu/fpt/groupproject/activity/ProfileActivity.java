package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.adapter.RoomAdapter;
import edu.fpt.groupproject.api.IRoomApi;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import edu.fpt.groupproject.model.user.UserChangePassword;
import edu.fpt.groupproject.popup.ChangeInfoPopup;
import edu.fpt.groupproject.popup.ChangePasswordPopup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,RoomAdapter.OnItemClickListener, ChangePasswordPopup.ChangePasswordPopupListener, ChangeInfoPopup.ChangeInfoPopupPopupListener {
    TextView txtName, txtPhone, txtAddress, txtRoleMessage, txtMessage, txtCurrentPassword, txtNewPassword, txtFullName, txtNewAddress, txtPhoneNumber;;
    ImageButton imgBtnBack, imgBtnLogout, imgBtnHome, imgBtnSearch, imgBtnAdd;
    Button btnEdit, btnChangePassword;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    List<Room> roomList;
    RoomAdapter roomAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        imgBtnLogout.setOnClickListener(this);
        imgBtnAdd = findViewById(R.id.imgBtnAdd);
        imgBtnAdd.setOnClickListener(this);
        imgBtnHome = findViewById(R.id.imgBtnHome);
        imgBtnHome.setOnClickListener(this);
        imgBtnSearch = findViewById(R.id.imgBtnSearch);
        imgBtnSearch.setOnClickListener(this);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtRoleMessage = findViewById(R.id.txtRoleMessage);
        txtMessage = findViewById(R.id.txtMessage);
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        getUser();
        getBookedRooms();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.imgBtnBack:
                ProfileActivity.this.finish();
                break;
            case R.id.imgBtnLogout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("token");
                editor.commit();
                Toast.makeText(ProfileActivity.this,"Đăng xuất thành công!",Toast.LENGTH_SHORT).show();
                //move to home activity after 1 second
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }, 1000);
                break;
            case R.id.imgBtnHome:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtnSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtnAdd:
                intent = new Intent(this, CreateRoomActivity.class);
                startActivity(intent);
            case R.id.btnEdit:
                showPopup("UPDATE_INFO");
                break;
            case R.id.btnChangePassword:
                showPopup("UPDATE_PASSWORD");
                break;
        }
    }

    public void getUser(){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.getUser(sharedPreferences.getString("username",null),sharedPreferences.getString("token",null))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        txtName.setText(response.body().getName());
                        txtPhone.setText("Điện thoại: "+response.body().getPhone());
                        txtAddress.setText("Địa chỉ: "+response.body().getAddress());
                        if(response.body().getRole().equals("ADMIN")){
                            txtRoleMessage.setText("Bài viết của bạn");
                            imgBtnAdd.setVisibility(View.VISIBLE);
                            getListRoomsByAuthor();
                        } else {
                            txtRoleMessage.setText("Phòng trọ đã liên hệ");
                            getBookedRooms();
                        }
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

    public void changePassword(String currentPassword, String newPassword){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        String username = sharedPreferences.getString("username",null);
        UserChangePassword user = new UserChangePassword();
        user.setUsername(username);
        user.setCurrentPassword(currentPassword);
        user.setNewPassword(newPassword);

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.changePassword(user, sharedPreferences.getString("token",null))
                .enqueue(new Callback<ReturnModel>() {
                    @Override
                    public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                        ReturnModel returnModel = response.body();
                        if(returnModel.isSuccess()){
                            Toast.makeText(ProfileActivity.this, "Đổi mật khẩu thành công. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                            autoLogout();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Đổi thất bại. Mật khẩu hiện tại không đúng.", Toast.LENGTH_SHORT).show();
                            showPopup("UPDATE_PASSWORD");
                        }
                    }
                    @Override
                    public void onFailure(Call<ReturnModel> call, Throwable t) {
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    public void autoLogout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("token");
        editor.commit();

        //move to home activity after 1 second
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }

    public void updateUserInfo(String name, String address, String phone){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        String username = sharedPreferences.getString("username",null);
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setAddress(address);
        user.setPhone(phone);

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.updateUserInfo(user, sharedPreferences.getString("token",null))
                .enqueue(new Callback<ReturnModel>() {
                    @Override
                    public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                        ReturnModel returnModel = response.body();
                        if(returnModel.isSuccess()){
                            Toast.makeText(ProfileActivity.this, "Sửa thông tin thành công.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Sửa thông tin thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ReturnModel> call, Throwable t) {
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    public void getBookedRooms(){
        //get booked rooms
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.getListBookedRoomsByUsername(sharedPreferences.getString("username",null),sharedPreferences.getString("token",null))
                .enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                roomList = response.body();
                //create recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.listRooms);
                roomAdapter = new RoomAdapter(roomList, ProfileActivity.this);
                recyclerView.setAdapter(roomAdapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                roomAdapter.notifyDataSetChanged();
                //if there is no booked room
                if(roomList.size()==0){
                    txtMessage.setText("Bạn chưa liên hệ với phòng trọ nào.");
                    txtMessage.setVisibility(View.VISIBLE);
                } else {
                    txtMessage.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    public void getListRoomsByAuthor(){
        //get list rooms by author
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.getListRoomsByAuthor(sharedPreferences.getString("username",null),sharedPreferences.getString("token",null))
                .enqueue(new Callback<List<Room>>() {
                    @Override
                    public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                        roomList = response.body();
                        //create recyclerview
                        recyclerView = (RecyclerView) findViewById(R.id.listRooms);
                        roomAdapter = new RoomAdapter(roomList, ProfileActivity.this);
                        recyclerView.setAdapter(roomAdapter);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        roomAdapter.notifyDataSetChanged();
                        //if there is no booked room
                        if(roomList.size()==0){
                            txtMessage.setText("Bạn chưa đăng bài viết nào.");
                            txtMessage.setVisibility(View.VISIBLE);
                        } else {
                            txtMessage.setVisibility(View.INVISIBLE);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Room>> call, Throwable t) {
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    public void showPopup(String action){
        if (action.equals("UPDATE_INFO")){
            ChangeInfoPopup popup = new ChangeInfoPopup();
            popup.show(getSupportFragmentManager(), "UPDATE_INFO");
        }else if(action.equals("UPDATE_PASSWORD")){
            ChangePasswordPopup popup = new ChangePasswordPopup();
            popup.show(getSupportFragmentManager(), "UPDATE_PASSWORD");
        }
    }

    public boolean validationUpdate(String fullName, String address, String phoneNumber){
        if (fullName == null || fullName.equals("") || address == null || address.equals("")|| phoneNumber == null || phoneNumber.equals("")){
            return false;
        }
        return true;
    }

    public boolean validationChangePassword(String currentPassword, String newPassword, String confirmPassword){
        if (currentPassword == null || currentPassword.equals("")
                || newPassword == null || newPassword.equals("")
                || confirmPassword == null || confirmPassword.equals("")){
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(Room room) {
        Intent intent = new Intent(ProfileActivity.this,DetailActivity.class);
        intent.putExtra("ROOM",room);
        startActivity(intent);
    }

    @Override
    public void applyTextInfo(String fullName, String address, String phoneNumber) {
        if(validationUpdate(fullName, address, phoneNumber)){
            txtName.setText(fullName);
            txtPhone.setText("Điện thoại: " + phoneNumber);
            txtAddress.setText("Địa chỉ: " + address);

            updateUserInfo(fullName, address, phoneNumber);
        }else{
            Toast.makeText(ProfileActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            showPopup("UPDATE_INFO");
        }
    }

    @Override
    public void applyTextPassword(String currentPassword, String newPassword, String confirmPassword) {
        if(validationChangePassword(currentPassword, newPassword, confirmPassword)){
            if (newPassword.equals(confirmPassword)){
                changePassword(currentPassword, newPassword);
            }else{
                Toast.makeText(ProfileActivity.this, "Xác nhận mật khẩu không khớp. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                ChangePasswordPopup popup = new ChangePasswordPopup(currentPassword, newPassword, "");
                popup.show(getSupportFragmentManager(), "UPDATE_FAIL_PASSWORD1");
            }
        }else{
            Toast.makeText(ProfileActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            ChangePasswordPopup popup = new ChangePasswordPopup(currentPassword, newPassword, confirmPassword);
            popup.show(getSupportFragmentManager(), "UPDATE_FAIL_PASSWORD2");
        }
    }
}
