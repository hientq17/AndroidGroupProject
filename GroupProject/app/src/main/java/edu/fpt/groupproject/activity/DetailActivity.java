package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitle, txtTime, txtPrice, txtAddress, txtDescription, txtElectric, txtWater, txtWifi, txtAuthor;
    ImageView imgRoom1, imgRoom2, imgRoom3;
    ImageButton imgBtnBack;
    Button btnBook;
    Room room;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);

        btnBook = findViewById(R.id.btnBook);
        btnBook.setOnClickListener(this);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        txtTitle = findViewById(R.id.txtTitle);
        txtTime = findViewById(R.id.txtTime);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        txtElectric = findViewById(R.id.txtElectric);
        txtWater = findViewById(R.id.txtWater);
        txtWifi = findViewById(R.id.txtWifi);
        txtAuthor = findViewById(R.id.txtAuthor);
        imgRoom1 = findViewById(R.id.imgRoom1);
        imgRoom2 = findViewById(R.id.imgRoom2);
        imgRoom3 = findViewById(R.id.imgRoom3);
        //get room from intent
        room = (Room)getIntent().getSerializableExtra("ROOM");
        //convert sql date to display
        String sqlFormat = "yyyy-MM-dd'T'HH:mm";
        String displayFormat = "dd/MM/yyyy hh:mm";
        String strTime = room.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(sqlFormat);
        Date date = null;
        try {
            date = dateFormat.parse(strTime);
            dateFormat = new SimpleDateFormat(displayFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //display short money
        double price = room.getPrice();
        String strPrice = (Double)price/1000000 + "M";
        txtTitle.setText(room.getTitle());
        txtTime.setText(dateFormat.format(date));
        txtPrice.setText(strPrice);
        txtAddress.setText(room.getAddress());
        txtDescription.setText(room.getDescription());
        txtElectric.setText(room.getElectricity());
        txtWater.setText(room.getWater());
        txtWifi.setText(room.getWifi());
        getAuthor();
        //show image
        String[] listUrl = room.getImage().split(";");
        Picasso.with(this).load(listUrl[0]).into(imgRoom1);
        Picasso.with(this).load(listUrl[1]).into(imgRoom2);
        Picasso.with(this).load(listUrl[2]).into(imgRoom3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBack:
                DetailActivity.this.finish();
                break;
            case R.id.btnBook:
                if(sharedPreferences.getString("username",null)==null){
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, BookingActivity.class);
                    intent.putExtra("ROOM",room);
                    startActivity(intent);
                }
                break;
        }
    }

    public void getAuthor(){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.getUser(sharedPreferences.getString("username",null),sharedPreferences.getString("token",null))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        txtAuthor.setText(response.body().getName());
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
}
