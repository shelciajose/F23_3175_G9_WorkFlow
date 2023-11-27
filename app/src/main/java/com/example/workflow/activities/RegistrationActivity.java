package com.example.workflow.activities;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.workflow.R;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.fragments.OTPFragment;
import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.PreferenceUtils;
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
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String userId ="";
    String phoneNumber = "";


    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        context = RegistrationActivity.this;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        (findViewById(R.id.activity_req_for_mobile_no_generate_otp_button_id)).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
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
            phoneNumber = ((AppCompatEditText)
                    findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString().trim();
            if (((AppCompatEditText) findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString().isEmpty())
            {
                ((AppCompatEditText) findViewById(R.id.activity_req_for_mobile_no_editext_id)).setError("Please enter mobile no");
            } else {
                checkPhoneNumberExists();
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
                    bundle.putString("userID", userId);
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
                            getUserDetailsById(context, userId);
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

    private void checkPhoneNumberExists() {
        DatabaseReference userRef = databaseReference.child("users");
        userRef.orderByChild("phoneNumber").equalTo(((AppCompatEditText) findViewById(R.id.activity_req_for_mobile_no_editext_id)).getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Phone number exists, send OTP

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userId = snapshot.getKey();
                        // userId is the user ID based on the provided phone number
                        // You can use this ID as needed
                    }
                    triggerFireBaseOTP();
                } else {
                    // Phone number doesn't exist
                    Toast.makeText(RegistrationActivity.this, "User with this phone number doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
                Toast.makeText(RegistrationActivity.this, "Error checking phone number: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getUserDetailsById(final Context context, final String userId) {
        DatabaseReference userRef = databaseReference.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user details
                    String address = dataSnapshot.child("Address").getValue(String.class);
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String userName = dataSnapshot.child("userName").getValue(String.class);
                    String department = dataSnapshot.child("Dept").getValue(String.class);
                    String email = dataSnapshot.child("emailAddress").getValue(String.class);
                    // Add more fields as needed

                    // Now you can use the retrieved details
                    // For example, log the details
                    Log.d("UserDetails", "Address: " + address);
                    Log.d("UserDetails", "FirstName: " + firstName);

                    PreferenceUtils.addUserDetailsToPreferences(context, phoneNumber, email, firstName + " " + lastName, userId,
                            department, address, userName);

                    new OfflineDatabase(context).addUserDetailsToDB(userId, userName, department, phoneNumber, email, address, firstName, lastName);

                    reqForLoginAfterOTPMatched();

                } else {
                    Toast.makeText(context, "Sorry!!! User Details not fund", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    public void reqForLoginAfterOTPMatched() {
        startActivity(new Intent(RegistrationActivity.this, NavigationActivity.class));
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

}