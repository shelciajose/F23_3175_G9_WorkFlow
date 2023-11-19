package com.example.workflow.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workflow.R;
import com.example.workflow.activities.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPFragment extends Fragment implements View.OnClickListener {


    private View viewFragment = null;

    private long minOTPWaitTime = 30000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewFragment = inflater.inflate(R.layout.fragment_o_t_p, container, false);
        countDownTimer.start();
//        viewFragment.findViewById(R.id.fragment_verify_otp_submit_button_id)
//                .setOnClickListener(this);
        viewFragment.findViewById(R.id.fragment_verify_otp_resend_otp_textview_id)
                .setOnClickListener(this);

        ((TextView)viewFragment.findViewById(R.id.phonenumber_tv)).setText(getArguments().getString("mobno"));

        viewFragment.findViewById(R.id.numberEdit_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegistrationActivity.class));
            }
        });


        viewFragment.findViewById(R.id.fragment_verify_otp_submit_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((AppCompatEditText) viewFragment.findViewById(R.id.fragment_verify_otp_enter_otp_edittext_id))
                        .getText().toString().isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getArguments()
                                    .getString("mVerificationId"),
                            ((AppCompatEditText) viewFragment.findViewById(R.id.fragment_verify_otp_enter_otp_edittext_id))
                                    .getText().toString());
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(getActivity(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return viewFragment;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_verify_otp_resend_otp_textview_id) {
                if (((AppCompatTextView) viewFragment.findViewById(R.id.fragment_verify_otp_resend_otp_textview_id))
                        .getText().toString().equals("Resend OTP?"))
                    ((RegistrationActivity) getActivity()).triggerFireBaseOTP();

            else if (v.getId() == R.id.fragment_verify_otp_submit_button_id)

                if (!((AppCompatEditText) viewFragment.findViewById(R.id.fragment_verify_otp_enter_otp_edittext_id))
                        .getText().toString().isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getArguments()
                                    .getString("mVerificationId"),
                            ((AppCompatEditText) viewFragment.findViewById(R.id.fragment_verify_otp_enter_otp_edittext_id))
                                    .getText().toString());
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(getActivity(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void autoFill(String otp) {
        ((AppCompatEditText) viewFragment.findViewById(R.id.fragment_verify_otp_enter_otp_edittext_id))
                .setText("" + otp);
        viewFragment.findViewById(R.id.fragment_verify_otp_submit_button_id).performClick();

    }


    private CountDownTimer countDownTimer = new CountDownTimer(minOTPWaitTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_verify_otp_resend_otp_textview_id))
                    .setText("Max wait time : " + millisUntilFinished / 1000);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onFinish() {
            ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_verify_otp_resend_otp_textview_id))
                    .setText("Resend OTP?");

        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            ((RegistrationActivity)getActivity()).reqForLoginAfterOTPMatched();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getActivity(), "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }



    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////// progress. . .
    private Dialog dialogProgress = null;

    //
    public void showProgressDialog() {
        if (dialogProgress == null)
            dialogProgress = new Dialog(getActivity());
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogProgress.setContentView(R.layout.loading_layout);
        if (!dialogProgress.isShowing())
            dialogProgress.show();

    }

    public void dismissProgressDialog() {
        if (dialogProgress != null) {
            if (dialogProgress.isShowing()) {
                dialogProgress.dismiss();
            }
            dialogProgress = null;
        }
    }


}