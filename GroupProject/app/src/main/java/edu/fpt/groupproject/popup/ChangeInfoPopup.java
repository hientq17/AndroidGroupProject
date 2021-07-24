package edu.fpt.groupproject.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import edu.fpt.groupproject.R;
import edu.fpt.groupproject.api.IUserApi;
import edu.fpt.groupproject.constant.SysConstant;
import edu.fpt.groupproject.model.user.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeInfoPopup extends AppCompatDialogFragment {

    private EditText txtFullName;
    private EditText txtAddress;
    private EditText txtPhoneNumber;
    private ChangeInfoPopupPopupListener listener;
    SharedPreferences sharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("FTRO", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_info_popup, null);

//        view.

        builder.setView(view)
                .setTitle("Đổi thông tin")
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                })
                .setPositiveButton("Đổi ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fullName = txtFullName.getText().toString();
                        String address = txtAddress.getText().toString();
                        String phoneNumber = txtPhoneNumber.getText().toString();
                        listener.applyTextInfo(fullName, address, phoneNumber);
                    }
                });

        txtFullName = view.findViewById(R.id.txtFullName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);

        getUser();

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ChangeInfoPopupPopupListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "ChangeInfoPopupPopupListener null");
        }
    }

    public interface ChangeInfoPopupPopupListener {
        void applyTextInfo(String fullName, String address, String phoneNumber);
    }

    public void getUser(){
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(SysConstant.BaseURL)
                .addConverterFactory(GsonConverterFactory.create()) .build();

        IUserApi userApi = retrofit.create(IUserApi.class);
        userApi.getUser(sharedPreferences.getString("username",null), sharedPreferences.getString("token",null))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        txtFullName.setText(response.body().getName());
                        txtPhoneNumber.setText(response.body().getPhone());
                        txtAddress.setText(response.body().getAddress());
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
}
