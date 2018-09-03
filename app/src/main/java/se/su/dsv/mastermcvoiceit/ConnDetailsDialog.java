package se.su.dsv.mastermcvoiceit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by felix on 2017-12-08.
 */

public class ConnDetailsDialog extends DialogFragment {
    ConnDetailDialogListener listener;

    public interface ConnDetailDialogListener {
        void dialogResult(String ip, String user, String pass, int port);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (ConnDetailDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConnDetailDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_main_connection_details, null);
        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String ip = ((EditText) view.findViewById(R.id.edittext_dialog_ip)).getText().toString();
                        String user = ((EditText) view.findViewById(R.id.edittext_dialog_username)).getText().toString();
                        String pass = ((EditText) view.findViewById(R.id.edittext_dialog_password)).getText().toString();
                        int port = Integer.parseInt( ((EditText) view.findViewById(R.id.edittext_dialog_port)).getText().toString() );

                        listener.dialogResult(ip, user, pass, port);
                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }
}
