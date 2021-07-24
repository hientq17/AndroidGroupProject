package edu.fpt.groupproject.popup;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.fpt.groupproject.R;
import edu.fpt.groupproject.activity.DetailActivity;
import edu.fpt.groupproject.activity.SearchActivity;
import edu.fpt.groupproject.adapter.ListUserBookingAdapter;
import edu.fpt.groupproject.adapter.RoomAdapter;
import edu.fpt.groupproject.api.IBookApi;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import edu.fpt.groupproject.model.user.UserBooking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListUserBookingPopup extends AppCompatDialogFragment implements View.OnClickListener, RoomAdapter.OnItemClickListener {

    TextView txtName;
    TextView txtPhoneNumber;
    TextView txtTime;
    TextView txtTotal;
    List<UserBooking> list = new ArrayList<>();;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    ListUserBookingAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.list_user_booking, null);

        builder.setView(view)
                .setTitle("Danh sách đặt phòng")
                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });

        txtName = view.findViewById(R.id.txtText);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtTime = view.findViewById(R.id.txtTime);
        txtTotal = view.findViewById(R.id.txtTotal);
        getListUserBooking(getArguments().getInt("roomId"), view);

        return builder.create();
    }


    public void getListUserBooking(int roomId, View view){

        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        IBookApi bookApi = retrofit.create(IBookApi.class);
        bookApi.getListUserBooking(roomId, sharedPreferences.getString("token",null))
                .enqueue(new Callback<List<UserBooking>>() {
                    @Override
                    public void onResponse(Call<List<UserBooking>> call, Response<List<UserBooking>> response) {

                        txtTotal.setText(response.body().size()+" người đã đặt phòng");
                        list = response.body();
                        //create recyclerview
                        recyclerView = (RecyclerView) view.findViewById(R.id.listUserBooking);
                        adapter = new ListUserBookingAdapter(list);
                        recyclerView.setAdapter(adapter);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<UserBooking>> call, Throwable t) {
                        try {
                            throw t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(Room room) {

    }

    @Override
    public void onClick(View v) {

    }

}
