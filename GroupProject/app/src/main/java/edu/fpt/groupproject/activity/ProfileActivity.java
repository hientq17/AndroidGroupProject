package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,RoomAdapter.OnItemClickListener {
    TextView txtName, txtPhone, txtAddress, txtRoleMessage, txtMessage;
    ImageButton imgBtnBack, imgBtnLogout, imgBtnHome, imgBtnSearch, imgBtnAdd;
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

    @Override
    public void onItemClick(Room room) {
        Intent intent = new Intent(ProfileActivity.this,DetailActivity.class);
        intent.putExtra("ROOM",room);
        startActivity(intent);
    }
}
