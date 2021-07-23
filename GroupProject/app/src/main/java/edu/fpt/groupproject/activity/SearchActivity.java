package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IRoomApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.Room;
import edu.fpt.groupproject.recyclerview.RoomAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, RoomAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    List<Room> roomList;
    RoomAdapter roomAdapter;
    TextView txtTotal;
    ImageButton imgBtnUser,imgBtnHome;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        txtTotal = findViewById(R.id.txtTotal);
        imgBtnUser = findViewById(R.id.imgBtnUser);
        imgBtnUser.setOnClickListener(SearchActivity.this);
        imgBtnHome = findViewById(R.id.imgBtnHome);
        imgBtnHome.setOnClickListener(SearchActivity.this);
        getAllRooms();
    }

    public void getAllRooms(){
        //get list all rooms
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.getListAllRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.e("TESTT",response.body().toString());
                txtTotal.setText(response.body().size()+" kết quả được tìm thấy");
                roomList = response.body();
                //create recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.listRooms);
                roomAdapter = new RoomAdapter(roomList,SearchActivity.this);
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
            case R.id.imgBtnHome:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(Room room) {
        Intent intent = new Intent(SearchActivity.this,DetailActivity.class);
        intent.putExtra("ROOM",room);
        startActivity(intent);
    }
}
