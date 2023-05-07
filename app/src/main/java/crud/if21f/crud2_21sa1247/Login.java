package crud.if21f.crud2_21sa1247;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;

public class Login extends AppCompatActivity {

    private Button btn_login, btn_login_otp;
    private EditText email_login, password_login;
    private TextView forget_password, signup_reg, main_act_debug;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener Listener;
    private String getEmail, getPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        email_login = findViewById(R.id.email_login);
        btn_login_otp = findViewById(R.id.btn_login_otp);
        password_login = findViewById(R.id.password_login);
        forget_password = findViewById(R.id.forget_password);
        signup_reg = findViewById(R.id.signup_reg);

        auth = FirebaseAuth.getInstance();
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
            }
        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmail = email_login.getText().toString();
                getPassword = password_login.getText().toString();
                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
                    Toast.makeText(Login.this, "Email atau Sandi salah!", Toast.LENGTH_SHORT).show();
                } else{
                    loginUserAccount();
                }
            }
        });



        /*
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
                Toast.makeText(getApplicationContext(),"Login Berhasil!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
         */

        TextView lupa=(TextView)findViewById(R.id.forget_password);

        lupa.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(Login.this, LupaEmail.class));
            }
        });

        TextView register=(TextView)findViewById(R.id.signup_reg);

        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUpOTP.class));
            }
        });

        TextView main_act_debug=(TextView)findViewById(R.id.main_debug);

        main_act_debug.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });

        btn_login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, LoginOTP.class));
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
        auth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(auth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(Login.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
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
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}