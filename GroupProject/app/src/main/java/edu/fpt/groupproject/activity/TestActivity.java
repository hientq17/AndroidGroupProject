package edu.fpt.groupproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.fpt.groupproject.R;
import edu.fpt.groupproject.popup.ChangeInfoPopup;
import edu.fpt.groupproject.popup.ChangePasswordPopup;
import edu.fpt.groupproject.popup.ListUserBookingPopup;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        btnClick = (Button) findViewById(R.id.btnTestShow);
        btnClick.setOnClickListener(this);
    }

    public void showPopup(){
        ListUserBookingPopup popup = new ListUserBookingPopup();
        popup.show(getSupportFragmentManager(), "UPDATE_INFO");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTestShow:
                showPopup();
                break;
        }
    }
}