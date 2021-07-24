package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IRoomApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.adapter.RoomAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, RoomAdapter.OnItemClickListener{

    ImageButton imgBtnUser,imgBtnSearch, imgBtnExit;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    List<Room> roomList;
    RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        imgBtnUser = findViewById(R.id.imgBtnUser);
        imgBtnUser.setOnClickListener(HomeActivity.this);
        imgBtnSearch = findViewById(R.id.imgBtnSearch);
        imgBtnSearch.setOnClickListener(HomeActivity.this);
        imgBtnExit = findViewById(R.id.imgBtnExit);
        imgBtnExit.setOnClickListener(HomeActivity.this);
        getTopRooms();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnUser:
                if(sharedPreferences.getString("username",null)==null){
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.imgBtnSearch:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtnExit:
                new AlertDialog.Builder(this)
                        .setMessage("Bạn muốn thoát ứng dụng?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                System.exit(0);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                break;
        }
    }

    public void getTopRooms(){
        //get top rooms
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.getTopRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                roomList = response.body();
                //create recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.listRooms);
                roomAdapter = new RoomAdapter(roomList, HomeActivity.this);
                recyclerView.setAdapter(roomAdapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                roomAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(HomeActivity.this,DetailActivity.class);
        intent.putExtra("ROOM",room);
        startActivity(intent);
    }

}