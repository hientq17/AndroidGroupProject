package edu.fpt.groupproject.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IRoomApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.common.ReturnModel;
import edu.fpt.groupproject.model.room.AddOrUpdateRoom;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.room.RoomBase64;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UpdateRoomActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton imgBtnBack, imgBtnDelete;
    Button btnUpdate;
    EditText txtTitle, txtAddress, txtPrice, txtElectric, txtWater, txtWifi, txtDescription;
    ImageView imgRoom1, imgRoom2, imgRoom3;
    Room room;
    SharedPreferences sharedPreferences;
    List<String> listBase64 = new ArrayList<>(Arrays.asList("","",""));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_room_layout);
        sharedPreferences = getSharedPreferences("FTRO", Context.MODE_PRIVATE);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(this);
        imgBtnDelete = findViewById(R.id.imgBtnDelete);
        imgBtnDelete.setOnClickListener(this);
        txtTitle = findViewById(R.id.txtTitle);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        txtElectric = findViewById(R.id.txtElectric);
        txtWater = findViewById(R.id.txtWater);
        txtWifi = findViewById(R.id.txtWifi);
        imgRoom1 = findViewById(R.id.imgRoom1);
        imgRoom2 = findViewById(R.id.imgRoom2);
        imgRoom3 = findViewById(R.id.imgRoom3);

        //get room from intent
        room = (Room)getIntent().getSerializableExtra("ROOM");
        txtTitle.setText(room.getTitle());
        txtPrice.setText(String.valueOf(room.getPrice()));
        txtAddress.setText(room.getAddress());
        txtDescription.setText(room.getDescription());
        txtElectric.setText(room.getElectricity());
        txtWater.setText(room.getWater());
        txtWifi.setText(room.getWifi());
        //show image
        String[] listUrl = room.getImage().split(";");
        Picasso.with(this).load(listUrl[0]).into(imgRoom1);
        Picasso.with(this).load(listUrl[1]).into(imgRoom2);
        Picasso.with(this).load(listUrl[2]).into(imgRoom3);
        listBase64.set(0,listUrl[0]);
        listBase64.set(1,listUrl[1]);
        listBase64.set(2,listUrl[2]);
        onUploadImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBack:
                UpdateRoomActivity.this.finish();
                break;
            case R.id.imgBtnDelete:
                new AlertDialog.Builder(this)
                        .setMessage("Bạn muốn xoá bài viết này?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteRoom();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                break;
            case R.id.btnUpdate:
                if(updateRoomValidation()){
                    updateRoom();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            switch (requestCode){
                case 1:
                    imgRoom1.setImageURI(imageUri);
                    listBase64.set(0, "data:image/jpg;base64,"+imageToBase64(BitmapFactory.decodeStream(imageStream)));
                    break;
                case 2:
                    imgRoom2.setImageURI(imageUri);
                    listBase64.set(1, "data:image/jpg;base64,"+imageToBase64(BitmapFactory.decodeStream(imageStream)));
                    break;
                case 3:
                    imgRoom3.setImageURI(imageUri);
                    listBase64.set(2, "data:image/jpg;base64,"+imageToBase64(BitmapFactory.decodeStream(imageStream)));
                    break;
            }

        }
    }

    private void onUploadImage(){
        imgRoom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(1);
            }
        });
        imgRoom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
            }
        });
        imgRoom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(3);
            }
        });
    }

    private void openGallery(int index) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, index);
    }

    private String imageToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private boolean updateRoomValidation(){
        if(txtTitle.getText().length()==0){
            Toast.makeText(UpdateRoomActivity.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtAddress.getText().length()==0){
            Toast.makeText(UpdateRoomActivity.this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtPrice.getText().length()==0){
            Toast.makeText(UpdateRoomActivity.this, "Vui lòng nhập giá cho thuê", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtElectric.getText().length()==0){
            Toast.makeText(UpdateRoomActivity.this, "Vui lòng nhập giá điện", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtWater.getText().length()==0){
            Toast.makeText(UpdateRoomActivity.this, "Vui lòng nhập giá nước", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtWifi.getText().length()==0){
            Toast.makeText(UpdateRoomActivity.this, "Vui lòng nhập giá wifi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updateRoom(){
        //String base64 = null; // List image here
        //convert date to sql date
        String sqlFormat = "yyyy-MM-dd'T'HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(sqlFormat);
        Date date = new Date();
        RoomBase64 roomBase64 = new RoomBase64();
        roomBase64.setId(room.getId());
        roomBase64.setTitle(txtTitle.getText().toString());
        roomBase64.setAddress(txtAddress.getText().toString());
        roomBase64.setPrice(Double.parseDouble(txtPrice.getText().toString()));
        roomBase64.setElectricity(txtElectric.getText().toString());
        roomBase64.setWater(txtWater.getText().toString());
        roomBase64.setWifi(txtWifi.getText().toString());
        roomBase64.setDescription(txtDescription.getText().toString());
        roomBase64.setAuthor(sharedPreferences.getString("username",null));
        roomBase64.setTime(dateFormat.format(date));
        roomBase64.setImage1(listBase64.get(0));
        roomBase64.setImage2(listBase64.get(1));
        roomBase64.setImage3(listBase64.get(2));
        AddOrUpdateRoom addOrUpdateRoom = new AddOrUpdateRoom(roomBase64,"UPDATE");
        UpdateRoomActivity.this.setContentView(R.layout.create_room_loading);
        //set timeout 30s
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL).client(client)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.insertOrUpdateRoom(addOrUpdateRoom,sharedPreferences.getString("token",null))
                .enqueue(new Callback<ReturnModel>() {
                    @Override
                    public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                        ReturnModel returnModel = response.body();
                        if(returnModel.isSuccess()){
                            getRoomById(Integer.parseInt(returnModel.getReturnId()));
                        } else {
                            UpdateRoomActivity.this.setContentView(R.layout.update_room_layout);
                            Toast.makeText(UpdateRoomActivity.this,"Lỗi hệ thống: "+returnModel.getMessage(),Toast.LENGTH_LONG).show();
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

    private void getRoomById(int id){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.getRoomById(id).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                Intent intent = new Intent(UpdateRoomActivity.this,DetailActivity.class);
                room = response.body();
                intent.putExtra("ROOM",room);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    private void deleteRoom(){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();
        IRoomApi roomApi = retrofit.create(IRoomApi.class);
        roomApi.deleteRoom(room.getId(),sharedPreferences.getString("token",null))
                .enqueue(new Callback<ReturnModel>() {
                    @Override
                    public void onResponse(Call<ReturnModel> call, Response<ReturnModel> response) {
                        ReturnModel returnModel = response.body();
                        if(returnModel.isSuccess()){
                            Toast.makeText(UpdateRoomActivity.this,"Xoá bài viết thành công",Toast.LENGTH_SHORT).show();
                            //move to profile activity after 1 second
                            new Handler().postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    Intent intent = new Intent(UpdateRoomActivity.this,ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(UpdateRoomActivity.this,"Lỗi hệ thống: "+returnModel.getMessage(),Toast.LENGTH_LONG).show();
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
