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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private EditText nohp_signup, email_signup, password_signup;
    private Button btn_signup;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String getEmail, getPassword, getNohp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nohp_signup=findViewById(R.id.nohp_signup);
        email_signup=findViewById(R.id.email_signup);
        password_signup=findViewById(R.id.password_signup);
        btn_signup=findViewById(R.id.btn_signup);
        progressBar=findViewById(R.id.bar_reg);
        EditText mEdit = findViewById(R.id.plusenamdua);
        mEdit.setEnabled(false);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                cekDataUser();
            }

            private void cekDataUser(){
                getNohp = nohp_signup.getText().toString();
                getEmail = email_signup.getText().toString();
                getPassword = password_signup.getText().toString();
                if(TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword) || TextUtils.isEmpty(getNohp)){
                    Toast.makeText(getApplicationContext(), "No HP, Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (getPassword.length() < 8) {
                        Toast.makeText(getApplicationContext(), "Password Minimal 8 Karakter", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        createUserAccount();
                    }
                }
            }

            private void createUserAccount() {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        User user = new User(getNohp, getEmail, getPassword);
                        FirebaseDatabase.getInstance().getReference().child("Pengguna").child("List").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUp.this, "Register Behasil, Cek Email untuk Verifikasi!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUp.this, Login.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });






        /*
        Button fbr6 = findViewById(R.id.btn_signup);

        fbr6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
                Toast.makeText(SignUp.this, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
         */
    }
}