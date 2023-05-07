package crud.if21f.crud2_21sa1247;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LupaSandi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_sandi);
        Button fbr3 = findViewById(R.id.button3);

        fbr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LupaSandi.this, Login.class));
                Toast.makeText(LupaSandi.this, "Password Berhasil Dirubah!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}