package edu.fpt.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import edu.fpt.groupproject.api.IApiMessage;
import edu.fpt.groupproject.model.message.MessageDetail;
import edu.fpt.groupproject.retrofit.BaseRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {


    public static Retrofit retrofit = null;
    public static IApiMessage myRetrofitAPI = null;

    public final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImhpZW50cSIsIm5hbWUiOiJUcuG7i25oIFF1YW5nIEhp4buBbiIsImFkZHJlc3MiOiJUaOG7q2EgVGhpw6puIEh14bq_IiwicGhvbmUiOiIwMTIzNDU2Nzg5Iiwicm9sZSI6IkFETUlOIiwiZ29vZ2xlSWQiOm51bGwsImlhdCI6MTYyNjg4MDQ0MywiZXhwIjoxNjI2OTY2ODQzfQ.8hPkkcB7Q15z08MumqGSyKiGKCU9pKz7qddXP7zTBK4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (retrofit == null){
            retrofit = BaseRetrofit.RetrofitBuilder();
        }

        if (myRetrofitAPI == null){
            myRetrofitAPI = retrofit.create(IApiMessage.class);
        }

        myRetrofitAPI.getListMessagesByInbox(2, token).enqueue(new Callback<List<MessageDetail>>() {
            @Override
            public void onResponse(Call<List<MessageDetail>> call, Response<List<MessageDetail>> response) {
                Log.e("TEST",response.body().toString());
            }

            @Override
            public void onFailure(Call<List<MessageDetail>> call, Throwable t) {
                Log.e("TEST", t.toString());
            }
        });
    }


}