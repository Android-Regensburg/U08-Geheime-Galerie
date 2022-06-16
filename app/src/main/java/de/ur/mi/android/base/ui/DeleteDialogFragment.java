package de.ur.mi.android.base.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class DeleteDialogFragment extends DialogFragment {

    private DeleteDialogListener listener;
    private SecretImage secretImage;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setNegativeButton(getString(R.string.dialog_delete_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton(getString(R.string.dialog_delete_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDeleteConfirmed();
            }
        }). setTitle(getString(R.string.dialog_delete_title).replace("$IMAGE", secretImage.getDescription())).create();
    }

    public void setSecretImage(SecretImage image){
        this.secretImage = image;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        listener = (DeleteDialogListener) context;
        super.onAttach(context);
    }

    public interface DeleteDialogListener{
        void onDeleteConfirmed();
    }
}
