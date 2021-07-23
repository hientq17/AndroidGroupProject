package edu.fpt.groupproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.model.Room;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitle, txtTime, txtPrice, txtAddress, txtDescription, txtElectric, txtWater, txtWifi;
    ImageView imgRoom1, imgRoom2, imgRoom3;
    ImageButton imgBtnBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
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
        imgRoom1 = findViewById(R.id.imgRoom1);
        imgRoom2 = findViewById(R.id.imgRoom2);
        imgRoom3 = findViewById(R.id.imgRoom3);
        //get room from intent
        Room room = (Room)getIntent().getSerializableExtra("ROOM");
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
        String[] listUrl = room.getImage().split(";");
        //show image
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
        }
    }
}
