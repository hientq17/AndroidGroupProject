package edu.fpt.groupproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import edu.fpt.groupproject.R;

public class LoadingActivity  extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        homeActivity();
    }

    public void homeActivity()
    {
        //move to home activity after 1 second
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
