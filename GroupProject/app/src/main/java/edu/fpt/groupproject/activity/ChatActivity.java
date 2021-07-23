package edu.fpt.groupproject.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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


public class ChatActivity extends AppCompatActivity {

    public static Retrofit retrofit = null;
    public static IApiMessage myRetrofitAPI = null;
    private List<MessageDetail> listMessage;

    RecyclerView recyclerView;
    MessageRecyclerViewAdapter messageRecyclerViewAdapter;

    public final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImhpZW50cSIsIm5hbWUiOiJUcuG7i25oIFF1YW5nIEhp4buBbiIsImFkZHJlc3MiOiJUaOG7q2EgVGhpw6puIEh14bq_IiwicGhvbmUiOiIwMTIzNDU2Nzg5Iiwicm9sZSI6IkFETUlOIiwiZ29vZ2xlSWQiOm51bGwsImlhdCI6MTYyNjk2NzY5NywiZXhwIjoxNjI3MDU0MDk3fQ.ziIp8rlANYj0QZHOMG6OectebTuOmHrbQgM3YNCGWYg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        recyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        if (retrofit == null){
            retrofit = BaseRetrofit.RetrofitBuilder();
        }

        if (myRetrofitAPI == null){
            myRetrofitAPI = retrofit.create(IApiMessage.class);
        }

        myRetrofitAPI.getListMessagesByInbox(2, token).enqueue(new Callback<List<MessageDetail>>() {
            @Override
            public void onResponse(Call<List<MessageDetail>> call, Response<List<MessageDetail>> response) {

                listMessage = new ArrayList<>();

                if (response.body() != null){
                    listMessage = new ArrayList<>(response.body());
                }

                messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(ChatActivity.this, listMessage);
                recyclerView.setAdapter(messageRecyclerViewAdapter);

//                messageRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<MessageDetail>> call, Throwable t) {
                Log.e("TEST", t.toString());
            }
        });
    }


}