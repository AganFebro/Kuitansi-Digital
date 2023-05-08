package crud.if21f.crud2_21sa1247;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginOTP extends AppCompatActivity {

    private Button btn_login_otp, get_otp, btn_login_email;
    private EditText nohp_login, kode_otp;
    private TextView forget_password, signup_reg, main_act_debug;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener Listener;
    private String getNohp, getPassword, verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        btn_login_otp = findViewById(R.id.btn_login_otp);
        btn_login_email = findViewById(R.id.btn_login_email);
        nohp_login = findViewById(R.id.nohp_login);
        kode_otp = findViewById(R.id.kode_otp);
        get_otp = findViewById(R.id.get_otp);
        forget_password = findViewById(R.id.forget_password);
        signup_reg = findViewById(R.id.signup_reg);
        auth = FirebaseAuth.getInstance();
        EditText mEdit = findViewById(R.id.plusenamduaa);
        mEdit.setEnabled(false);

        Listener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && !TextUtils.isEmpty(user.getPhoneNumber())) {
                    // user sudah pernah login menggunakan nomor telepon dan nomor telepon sudah terverifikasi
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    Toast.makeText(LoginOTP.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // kosong
                }
            }
        };

        btn_login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNohp = nohp_login.getText().toString();
                if (TextUtils.isEmpty(getNohp)) {
                    Toast.makeText(LoginOTP.this, "No HP belum diisi!", Toast.LENGTH_SHORT).show();
                } else{
                    verifyCode(kode_otp.getText().toString());
                }
            }
        });

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking whether the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(nohp_login.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(LoginOTP.this, "Masukkan Nomor HP dengan Benar!", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    String phone = "+62" + nohp_login.getText().toString();
                    checkPhoneNumberExists(phone);
                }
            }
        });

        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        TextView lupa=(TextView)findViewById(R.id.forget_password);

        lupa.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(LoginOTP.this, LupaEmail.class));
            }
        });

        TextView register=(TextView)findViewById(R.id.signup_reg);

        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(LoginOTP.this, SignUp.class));
            }
        });

        TextView main_act_debug=(TextView)findViewById(R.id.main_debug);

        main_act_debug.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(LoginOTP.this, MainActivity.class));
            }
        });

        btn_login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOTP.this, Login.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID,verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }
    private void checkPhoneNumberExists(String phoneNumber) {
        // Get a reference to the Firebase User table
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Pengguna/List");
        // Query the Firebase User table to check if the phone number exists
        String phone = nohp_login.getText().toString();
        Query phoneQuery = userRef.orderByChild("nohp").equalTo(phone);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Phone number exists in Firebase, proceed with sending OTP
                        sendVerificationCode(phoneNumber);
                    } else {
                        // Phone number does not exist in Firebase, display an error message
                        Toast.makeText(LoginOTP.this, "Nomor HP Tidak Terdaftar!", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Display an error message if the query is cancelled
                Toast.makeText(LoginOTP.this, "Gagal Mengecek Nomor HP.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(Listener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (Listener != null) {
            auth.removeAuthStateListener(Listener);
        }
    }
    private void loginUserAccount() {
        auth.signInWithEmailAndPassword(getNohp, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(auth.getCurrentUser().isEmailVerified()) {
                        if (TextUtils.isEmpty(kode_otp.getText().toString())){
                            Toast.makeText(LoginOTP.this, "Masukkan OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            verifyCode(kode_otp.getText().toString());
                        }
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginOTP.this);
                        alert.setTitle("Verifikasi Email terlebih dahulu!");
                        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        alert.create();
                        alert.show();
                    }
                } else {
                    Toast.makeText(LoginOTP.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void signInWithCredential (PhoneAuthCredential credential){
        // inside this method we are checking if
        // the code entered is correct or not.
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                // if the code is correct and the task is successful
                                // we are sending our user to new activity.
                                Intent i = new Intent(LoginOTP.this, MainActivity.class);
                                startActivity(i);
                                finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(LoginOTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode (String number){
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                kode_otp.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(LoginOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode (String code){
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

}