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
    private EditText txtConfirmPassword;
    private ChangePasswordPopupListener listener;
    private String currentPassword, newPassword, confirmPassword;

    public ChangePasswordPopup(){

    }

    public ChangePasswordPopup(String currentPassword, String newPassword, String confirmPassword){
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

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
                        String current = txtCurrentPassword.getText().toString();
                        String newPass = txtNewPassword.getText().toString();
                        String confirm = txtConfirmPassword.getText().toString();
                        listener.applyTextPassword(current, newPass, confirm);
                    }
                });

        txtCurrentPassword = view.findViewById(R.id.txtCurrentPassword);
        txtNewPassword = view.findViewById(R.id.txtNewPassword);
        txtConfirmPassword = view.findViewById(R.id.txtConfirmPassword);

        txtCurrentPassword.setText(this.currentPassword);
        txtNewPassword.setText(this.newPassword);
        txtConfirmPassword.setText(this.confirmPassword);

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
        void applyTextPassword(String currentPassword, String newPassword, String confirmPassword);
    }
}
