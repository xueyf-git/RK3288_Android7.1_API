package com.example.apiDemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.apiDemo.R;

public class InputDialogFragment extends DialogFragment {

    public interface OnInputListener {
        void sendInput(String IdAddress, String SubnetMask,String Gateway,String DNS,String BackupDNS);

        }

    private OnInputListener onInputListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ethernet_configure, container, false);

        EditText IpAddress = view.findViewById(R.id.configureIpAddress_tv);
        EditText SubnetMask = view.findViewById(R.id.configureSubNetMask_tv);
        EditText Gateway = view.findViewById(R.id.configureGateway_et);
        EditText DNS = view.findViewById(R.id.configureDNS_tv);
        EditText BackupDNS = view.findViewById(R.id.configureBackupDNS_tv);

        Button submitButton = view.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String IpAddress_st = IpAddress.getText().toString();
            String SubnetMask_st = SubnetMask.getText().toString();
            String Gateway_st = Gateway.getText().toString();
            String DNS_st = DNS.getText().toString();
            String BackupDNS_st = BackupDNS.getText().toString();

            onInputListener.sendInput(IpAddress_st, SubnetMask_st,Gateway_st,DNS_st,BackupDNS_st);
            dismiss(); // Close the dialog
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnInputListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }
}
