package edu.fpt.groupproject.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import edu.fpt.groupproject.R;

public class ChangePasswordPopup extends AppCompatDialogFragment {

    private EditText txtCurrentPassword;
    private EditText txtNewPassword;
    private ChangePasswordPopupListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_popup, null);

        builder.setView(view)
                .setTitle("Đổi mật khẩu")
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Đổi ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String currentPassword = txtCurrentPassword.getText().toString();
                        String newPassword = txtNewPassword.getText().toString();
                        listener.applyTextPassword(currentPassword, newPassword);
                    }
                });

        txtCurrentPassword = view.findViewById(R.id.txtCurrentPassword);
        txtNewPassword = view.findViewById(R.id.txtNewPassword);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ChangePasswordPopupListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "ChangePasswordPopupListener null");
        }
    }

    public interface ChangePasswordPopupListener {
        void applyTextPassword(String currentPassword, String newPassword);
    }
}
