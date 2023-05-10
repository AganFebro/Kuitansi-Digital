package crud.if21f.crud2_21sa1247;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class crud extends AppCompatActivity {
    RadioGroup rg;
    RadioButton rb;

    private ProgressBar progress;
    private EditText nim, nama, tgllahir, nohp, crudemail, ipk, alamat;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    private TextView hasil;
    private Spinner fakultas, prodi;
    private CheckBox golA, golB, golAB, golO;
    private ImageView imageContainer;
    private Button save, showdata, getfoto;
    private String getNIM, getNama, getProdi, getFakultas, getAlamat, getCrudemail, getGambar;
    private String getTgllahir;
    private String getNohp, getIpk, getRjeniskelamin, getHasilgol, outputgolongan;
    private ArrayList<String> hasilgol;
    public Uri imageUrl, uri;
    public Bitmap bitmap;
    private StorageReference reference;

    DatabaseReference getReference;
    FirebaseStorage storage;
    DatabaseReference database;
    StorageReference storageReference;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        rg = (RadioGroup) findViewById(R.id.rjeniskelamin);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        save = findViewById(R.id.save);
        showdata = findViewById(R.id.showdata);
        getfoto = findViewById(R.id.getfoto);
        imageContainer = findViewById(R.id.imageContainer);
        nim = findViewById(R.id.nim);
        nama = findViewById(R.id.nama);
        fakultas = findViewById(R.id.fakultas);
        prodi = findViewById(R.id.prodi);
        nohp = findViewById(R.id.nohp);
        crudemail = findViewById(R.id.crudemail);
        ipk = findViewById(R.id.ipk);
        alamat = findViewById(R.id.alamat);
        golA = findViewById(R.id.golA);
        golB = findViewById(R.id.golB);
        golAB = findViewById(R.id.golAB);
        golO = findViewById(R.id.golO);
        hasil = findViewById(R.id.hasil);
        hasilgol = new ArrayList<>();
        hasil.setEnabled(false);

        tgllahir = findViewById(R.id.tgllahir);

        EditText edit = (EditText)findViewById(R.id.nim);
        String input;
        input = String.valueOf(edit.getText());
        input = input.toUpperCase(); //converts the string to uppercase


        dateFormatter = new SimpleDateFormat("dd MMM YYYY");
        tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        golA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    golB.setChecked(false);
                    golAB.setChecked(false);
                    golO.setChecked(false);
                    outputgolongan = "A";
                }
            }
        });
        golB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    golA.setChecked(false);
                    golAB.setChecked(false);
                    golO.setChecked(false);
                    outputgolongan = "B";
                }
            }
        });
        golAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    golA.setChecked(false);
                    golB.setChecked(false);
                    golO.setChecked(false);
                    outputgolongan = "AB";
                }
            }
        });
        golO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    golA.setChecked(false);
                    golB.setChecked(false);
                    golAB.setChecked(false);
                    outputgolongan = "O";
                }
            }
        });

        reference = FirebaseStorage.getInstance().getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNIM = nim.getText().toString();
                getNama = nama.getText().toString();
                getFakultas = fakultas.getSelectedItem().toString();
                getProdi = prodi.getSelectedItem().toString();
                if (outputgolongan == null) {
                    getHasilgol = null;
                } else {
                    getHasilgol = outputgolongan.trim();
                }
                if (rb == null) {
                    getRjeniskelamin = null;
                } else {
                    getRjeniskelamin = rb.getText().toString();
                }

                getTgllahir = tgllahir.getText().toString();
                getNohp = nohp.getText().toString();
                getCrudemail = crudemail.getText().toString();
                getIpk = ipk.getText().toString();
                getAlamat = alamat.getText().toString();
                getGambar = imageContainer.toString().trim();

                checkUser();
            }
        });

        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getimage();
            }
        });
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                tgllahir.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getimage() {
        Intent imageIntentGallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntentGallery, 2);
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    imageContainer.setVisibility(View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageContainer.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_CODE_GALLERY:
                if(resultCode == RESULT_OK) {
                    imageContainer.setVisibility(View.VISIBLE);
                    uri = data.getData();
                    imageContainer.setImageURI(uri);
                }
                break;
        }
    }

    private void checkUser() {
        if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getProdi) || isEmpty(getFakultas) ||
                isEmpty(getAlamat) || TextUtils.isEmpty(getHasilgol) || outputgolongan.equals("") ||
                outputgolongan == null || TextUtils.isEmpty(getIpk) || TextUtils.isEmpty(getRjeniskelamin) ||
                TextUtils.isEmpty(getCrudemail) || TextUtils.isEmpty(getTgllahir) || TextUtils.isEmpty(getNohp) ||
                uri == null) {
            Toast.makeText(crud.this, "Data Tidak Boleh Ada Yang Kosong", Toast.LENGTH_SHORT).show();
        } else {
            imageContainer.setDrawingCacheEnabled(true);
            imageContainer.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageContainer.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();

            String namaFile = UUID.randomUUID() + ".jpg";
            final String pathImage = "gambar/" + namaFile;
            UploadTask uploadTask = reference.child(pathImage).putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getReference.child("Admin").child("Mahasiswa").push()
                                    .setValue(new data_mahasiswa(getNIM, getNama, getFakultas, getProdi, getHasilgol, getRjeniskelamin,
                                            getTgllahir, getNohp, getCrudemail, getIpk, getAlamat,
                                            uri.toString().trim()))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            nim.setText("");
                                            nama.setText("");
                                            tgllahir.setText("");
                                            nohp.setText("");
                                            crudemail.setText("");
                                            ipk.setText("");
                                            alamat.setText("");
                                            Toast.makeText(crud.this, "Data Berhasil Tersimpan", Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.GONE);
                                            Intent intent = new Intent(crud.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(crud.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void rbclick(View v) {
        int radiobuttonid = rg.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(radiobuttonid);
    }
}