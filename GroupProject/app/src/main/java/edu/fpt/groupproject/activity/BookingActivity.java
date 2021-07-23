package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IBookApi;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import edu.fpt.groupproject.model.book.AddOrUpdateBook;
import edu.fpt.groupproject.model.book.Book;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtTitle, txtAddress;
    EditText txtPhone, txtNote;
    Button btnMore,btnBook;
    ImageButton imgBtnBack;
    ImageView imgRoom;
    Room room;
    User user;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        //get room from intent
        room = (Room)getIntent().getSerializableExtra("ROOM");
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        imgRoom = findViewById(R.id.imgRoom);
        btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
        btnBook = findViewById(R.id.btnBook);
        btnBook.setOnClickListener(this);
        txtPhone = findViewById(R.id.txtPhone);
        txtNote = findViewById(R.id.txtNote);
        txtTitle = findViewById(R.id.txtTitle);
        txtAddress = findViewById(R.id.txtAddress);
        txtTitle.setText(room.getTitle());
        txtAddress.setText("Địa chỉ: "+room.getAddress());
        //show image
        String[] listUrl = room.getImage().split(";");
        Picasso.with(this).load(listUrl[1]).into(imgRoom);
        getUserInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMore:
                BookingActivity.this.finish();
                break;
           case R.id.imgBtnBack:
               BookingActivity.this.finish();
                break;
            case R.id.btnBook:
                addOrUpdateBook();
                break;
        }
    }

    public void getUserInformation(){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.getUser(sharedPreferences.getString("username",null),sharedPreferences.getString("token",null))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.e("TEST", response.body().toString());
                        user = response.body();
                        txtPhone.setText(user.getPhone());
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

    public void addOrUpdateBook(){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        IBookApi bookApi = retrofit.create(IBookApi.class);
        //convert date to sql date
        String sqlFormat = "yyyy-MM-dd'T'HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(sqlFormat);
        Date date = new Date();
        Book book = new Book(
                sharedPreferences.getString("username",null),
                room.getId(),
                dateFormat.format(date),
                txtPhone.getText().toString(),
                txtNote.getText().toString()
        );
        AddOrUpdateBook addOrUpdateBook = new AddOrUpdateBook(book,"INSERT");
        bookApi.insertOrUpdateBook(addOrUpdateBook,sharedPreferences.getString("token",null))
                .enqueue(new Callback<ReturnModel>() {
                    @Override
                    public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                        ReturnModel returnModel = response.body();
                        if(returnModel.isSuccess()){
                            setContentView(R.layout.booking_layout);
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

}
