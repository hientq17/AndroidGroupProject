package edu.fpt.groupproject.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.squareup.picasso.Picasso;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import edu.fpt.groupproject.popup.ListUserBookingPopup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitle, txtTime, txtPrice, txtAddress, txtDescription, txtElectric, txtWater, txtWifi, txtAuthor;
    ImageView imgRoom1, imgRoom2, imgRoom3;
    ImageButton imgBtnBack, imgBtnEdit, imgBtnMore;
    Button btnBook, btnCall;
    public Room room;
    User author;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);

        btnBook = findViewById(R.id.btnBook);
        btnBook.setOnClickListener(this);
        btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        imgBtnEdit = findViewById(R.id.imgBtnEdit);
        imgBtnEdit.setOnClickListener(this);
        imgBtnMore = findViewById(R.id.imgBtnMore);
        imgBtnMore.setOnClickListener(this);
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
        //if current user is author
        if(room.getAuthor().equals(sharedPreferences.getString("username",null))){
            imgBtnEdit.setVisibility(View.VISIBLE);
            imgBtnMore.setVisibility(View.VISIBLE);
            btnBook.setEnabled(false);
        } else {
            imgBtnEdit.setVisibility(View.INVISIBLE);
            imgBtnMore.setVisibility(View.INVISIBLE);
            btnBook.setEnabled(true);
        }
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
        getAuthor(room.getAuthor());
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
            case R.id.imgBtnEdit:
                Intent intent = new Intent(this, UpdateRoomActivity.class);
                intent.putExtra("ROOM",room);
                startActivity(intent);
                break;
            case R.id.btnCall:
                phoneCall();
                break;
            case R.id.imgBtnMore:
                showBookingPopup();
                break;
        }
    }

    public void getAuthor(String username){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.getUser(username,sharedPreferences.getString("token",null))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        author = response.body();
                        txtAuthor.setText(author.getName());
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

    public void phoneCall(){
        if (Build.VERSION.SDK_INT > 22) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                return;
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+" + author.getPhone()));
            startActivity(callIntent);
        } else {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+" + author.getPhone()));
            startActivity(callIntent);
        }
    }

    public void showBookingPopup(){
        ListUserBookingPopup popup = new ListUserBookingPopup();
        Bundle args = new Bundle();
        args.putInt("roomId", room.getId());
        popup.setArguments(args);
        popup.show(getSupportFragmentManager(), "UPDATE_INFO");
    }
}
