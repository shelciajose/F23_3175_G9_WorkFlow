package com.example.workflow.activities;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.workflow.R;
import com.example.workflow.fragments.OTPFragment;
import com.example.workflow.utils.CommonFunc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        (findViewById(R.id.activity_req_for_mobile_no_generate_otp_button_id)).setOnClickListener(this);
    }

    private Dialog dialogProgress = null;

    //
    public void showProgressDialog() {
        if (dialogProgress == null)
            dialogProgress = new Dialog(RegistrationActivity.this);
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


    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.activity_req_for_mobile_no_generate_otp_button_id)
        {
                    if (((AppCompatEditText) findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString().isEmpty())
                    {
                        ((AppCompatEditText) findViewById(R.id.activity_req_for_mobile_no_editext_id)).setError("Please enter mobile no");
                    } else {
                         checkValidUserOrNot(((AppCompatEditText) findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString().trim());
                            }

        }
    }

    ///firebase
    ////generate sms. . . . . . .
    // global variable
    public String mVerificationId = null;
    public PhoneAuthProvider.ForceResendingToken mResendToken = null;
    private PhoneAuthCredential credential = null;

    ////
    public void triggerFireBaseOTP()
    {
            showProgressDialog();
            PhoneAuthProvider.verifyPhoneNumber(
                    PhoneAuthOptions
                            .newBuilder(FirebaseAuth.getInstance())
                            .setActivity(this)
                            .setPhoneNumber("+1" + ((AppCompatEditText)
                                    findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString())
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setCallbacks(onVerificationStateChangedCallbacks)
                            .build());
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential, "", phoneAuthCredential.getSmsCode());

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    dismissProgressDialog();
                    Toast.makeText(RegistrationActivity.this, " User not found ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    dismissProgressDialog();
                    mVerificationId = s;
                    mResendToken = forceResendingToken;
                    Bundle bundle = new Bundle();
                    bundle.putString("mVerificationId", mVerificationId);
                    bundle.putString("mobno", ((AppCompatEditText)
                            findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString());
                    attachFragment(new OTPFragment(), false, "Verify OTP", bundle);
                    Toast.makeText(RegistrationActivity.this, "A verification code has been sent to your mobile no.", Toast.LENGTH_LONG).show();
                }
            };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// sign-in the user. . .
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String verificationId, final String code) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reqForLoginAfterOTPMatched();
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                /*Toast.makeText(LoginActivity.this, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();*/

                            }
                        }
                    }
                });
    }


    private void attachFragment(Fragment fragment, boolean isAddToBackStack, String tag, Bundle bundle) {
        if (bundle != null)
            fragment.setArguments(bundle);
        RelativeLayout fl = (RelativeLayout) findViewById(R.id.req_for_mobile_content_base);
        fl.removeAllViews();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.req_for_mobile_content_base, fragment, tag);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void checkValidUserOrNot(String Phone) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("users").child("101").child("phoneNumber");
        Task<DataSnapshot> num = rootRef.child("users").child("101").child("phoneNumber").get();

        ValueEventListener eventListener = new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        dismissProgressDialog();
                        CommonFunc.commonDialog(RegistrationActivity.this, getString(R.string.alert),
                                "User Doesn't Exist", false,
                                RegistrationActivity.this, findViewById(R.id.req_for_mobile_content_base));
                    }
                    else{
                        triggerFireBaseOTP();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
                CommonFunc.commonDialog(RegistrationActivity.this, getString(R.string.alert),
                        databaseError.getMessage(), false,
                        RegistrationActivity.this, findViewById(R.id.req_for_mobile_content_base));
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);
    }

    public void reqForLoginAfterOTPMatched() {

        startActivity(new Intent(RegistrationActivity.this, NavigationActivity.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

}