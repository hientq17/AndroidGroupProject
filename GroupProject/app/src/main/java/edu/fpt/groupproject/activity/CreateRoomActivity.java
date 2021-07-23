package edu.fpt.groupproject.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.fpt.groupproject.R;
import edu.fpt.groupproject.adapter.MessageRecyclerViewAdapter;
import edu.fpt.groupproject.api.IApiMessage;
import edu.fpt.groupproject.model.message.MessageDetail;
import edu.fpt.groupproject.retrofit.BaseRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CreateRoomActivity extends AppCompatActivity {

    private ImageView imgBtn1;
    private ImageView imgBtn2;
    private ImageView imgBtn3;
    private List<String> listBase64 = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room_layout);

        onClickButton();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                    imgBtn1.setImageURI(imageUri);
                    listBase64.add(imageToBase64(BitmapFactory.decodeStream(imageStream)));
                    break;
                case 2:
                    imgBtn2.setImageURI(imageUri);
                    listBase64.add(imageToBase64(BitmapFactory.decodeStream(imageStream)));
                    break;
                case 3:
                    imgBtn3.setImageURI(imageUri);
                    listBase64.add(imageToBase64(BitmapFactory.decodeStream(imageStream)));
                    break;
            }

            String base64 = String.join("@", listBase64); // List image here

        }
    }

    private void onClickButton(){
        imgBtn1 = (ImageView)findViewById(R.id.btnImageRoom1);
        imgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(1);
            }
        });

        imgBtn2 = (ImageView)findViewById(R.id.btnImageRoom2);
        imgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
            }
        });

        imgBtn3 = (ImageView)findViewById(R.id.btnImageRoom3);
        imgBtn3.setOnClickListener(new View.OnClickListener() {
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

}