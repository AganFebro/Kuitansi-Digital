package crud.if21f.crud2_21sa1247;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Update extends AppCompatActivity {
    private String[] ListProdi, ListFakultas;
    private EditText nimBaru, namaBaru, nohpBaru, emailBaru, ipkBaru, alamatBaru, tgllahirBaru;
    private Spinner new_fakultas, new_prodi;
    private ImageView fotolama;
    private ProgressBar progressBar;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter, Formatdatalama;
    private RadioButton Pria, Wanita;
    private RadioGroup rg;
    private CheckBox golA, golB, golAB, golO;
    private Button update, btn_gantifoto,showdata;
    private DatabaseReference database;
    private StorageReference storageReference;
    private String cekNim, cekNama, cekFakultas, cekNohp, cekEmail, cekIpk, cekAlamat, cekProdi, cekGolongan, cekJeniskelamin,
            cekTgllahir, outputgolongan, outputjeniskelamin;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nimBaru = findViewById(R.id.new_nim);
        namaBaru = findViewById(R.id.new_nama);
        tgllahirBaru = findViewById(R.id.new_tgllahir);
        showdata = findViewById(R.id.showdata);
        nohpBaru = findViewById(R.id.new_nohp);
        emailBaru = findViewById(R.id.new_crudemail);
        ipkBaru = findViewById(R.id.new_ipk);
        alamatBaru = findViewById(R.id.new_alamat);
        new_fakultas = findViewById(R.id.new_fakultas);
        new_prodi = findViewById(R.id.new_prodi);
        golA = findViewById(R.id.new_golA);
        golB = findViewById(R.id.new_golB);
        golAB = findViewById(R.id.new_golAB);
        golO = findViewById(R.id.new_golO);
        Pria = findViewById(R.id.new_rpria);
        Wanita = findViewById(R.id.new_rwanita);
        btn_gantifoto = findViewById(R.id.new_getfoto);
        update = findViewById(R.id.update);
        fotolama = findViewById(R.id.new_imageContainer);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference();

        getData();

        btn_gantifoto.setOnClickListener(view -> {
            getImage();
        });

        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        update.setOnClickListener(view -> {
            cekNim = nimBaru.getText().toString();
            cekNama = namaBaru.getText().toString();
            cekFakultas = new_fakultas.getSelectedItem().toString();
            cekProdi = new_prodi.getSelectedItem().toString();
            cekNohp = nohpBaru.getText().toString();
            cekEmail = emailBaru.getText().toString();
            cekIpk = ipkBaru.getText().toString();
            cekAlamat = alamatBaru.getText().toString();
            cekTgllahir = tgllahirBaru.getText().toString();

            if (isEmpty(cekNim) || isEmpty(cekNama) || isEmpty(cekEmail) || isEmpty(cekFakultas) || isEmpty(cekIpk) || outputgolongan == null
                    || outputjeniskelamin == null || isEmpty(cekAlamat) || isEmpty(cekNohp) || isEmpty(cekProdi) || isEmpty(cekTgllahir)) {
                Toast.makeText(this, "Data Tidak Boleh Ada Yang Kosong!", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                fotolama.setDrawingCacheEnabled(true);
                fotolama.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) fotolama.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();

                String namafile = UUID.randomUUID() + ".jpg";
                final String pathImage = "foto/" + namafile;
                UploadTask uploadTask = storageReference.child(pathImage).putBytes(bytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                data_mahasiswa setMahasiswa = new data_mahasiswa();
                                setMahasiswa.setNim(nimBaru.getText().toString());
                                setMahasiswa.setNama(namaBaru.getText().toString());
                                setMahasiswa.setFakultas(new_fakultas.getSelectedItem().toString());
                                setMahasiswa.setProdi(new_prodi.getSelectedItem().toString());
                                setMahasiswa.setNohp(nohpBaru.getText().toString());
                                setMahasiswa.setCrudemail(emailBaru.getText().toString());
                                setMahasiswa.setIpk(ipkBaru.getText().toString());
                                setMahasiswa.setAlamat(alamatBaru.getText().toString());
                                setMahasiswa.setHasilgol(outputgolongan.trim());
                                setMahasiswa.setRjeniskelamin(outputjeniskelamin.trim());
                                setMahasiswa.setTgllahir(tgllahirBaru.getText().toString());
                                setMahasiswa.setGambar(uri.toString().trim());

                                updateMahasiswa(setMahasiswa);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Update.this, "Update Gagal", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                    }
                });

            }
        });
    }

    private void getData() {
        final String getNIM = getIntent().getExtras().getString("dataNIM");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getFakultas = getIntent().getExtras().getString("dataFakultas");
        final String getProdi = getIntent().getExtras().getString("dataProdi");
        final String getNohp = getIntent().getExtras().getString("dataNohp");
        final String getEmail = getIntent().getExtras().getString("dataCrudemail");
        final String getIpk = getIntent().getExtras().getString("dataIpk");
        final String getAlamat = getIntent().getExtras().getString("dataAlamat");
        final String getHasilgol = getIntent().getExtras().getString("dataGoldar");
        final String getJenisKelamin = getIntent().getExtras().getString("dataJenisKelamin");
        final String getTgllahir = getIntent().getExtras().getString("dataTanggal");
        final String getGambar = getIntent().getExtras().getString("data_gambar");

        if (isEmpty(getGambar)) {
            fotolama.setImageResource(R.drawable.people);
        } else {
            Glide.with(Update.this)
                    .load(getGambar)
                    .into(fotolama);
        }

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

        ListProdi = new String[]{"Informatika", "Sistem Informasi", "Teknologi Informasi", "Bisnis Digital", "Bahasa Inggris"};
        ArrayAdapter<String> prodiadapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ListProdi);
        new_prodi.setAdapter(prodiadapter);
        new_prodi.setSelection(prodiadapter.getPosition(getProdi.trim()));

        ListFakultas = new String[]{"Ilmu Komputer", "Bisnis dan Ilmu Sosial", "Ekonomi dan Sosial"};
        ArrayAdapter<String> fakultasadapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ListFakultas);
        new_fakultas.setAdapter(fakultasadapter);
        new_fakultas.setSelection(fakultasadapter.getPosition(getFakultas.trim()));

        Pria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    outputjeniskelamin = "Pria";
                }
            }
        });

        Wanita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    outputjeniskelamin = "Wanita";
                }
            }
        });

        dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        tgllahirBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        if (getHasilgol.trim().toString().equals("A")) {
            golA.setChecked(true);
        } else if (getHasilgol.trim().toString().equals("B")) {
            golB.setChecked(true);
        } else if (getHasilgol.trim().toString().equals("AB")) {
            golAB.setChecked(true);
        } else if (getHasilgol.trim().toString().equals("O")) {
            golO.setChecked(true);
        }

        rg = (RadioGroup) findViewById(R.id.rjeniskelamin);

        if (getJenisKelamin.trim().equals("Pria")) {
            Pria.setChecked(true);
        } else if (getJenisKelamin.trim().equals("Wanita")) {
            Wanita.setChecked(true);
        }

        nimBaru.setText(getNIM);
        namaBaru.setText(getNama);
        nohpBaru.setText(getNohp);
        emailBaru.setText(getEmail);
        ipkBaru.setText(getIpk);
        alamatBaru.setText(getAlamat);
        tgllahirBaru.setText(getTgllahir);
    }

    private void getImage() {
        Intent imageIntentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    fotolama.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    fotolama.setImageURI(uri);
                }
                break;
        }
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                tgllahirBaru.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateMahasiswa(data_mahasiswa setMahasiswa) {
        String getKey = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getKey = extras.getString("dataPrimaryKey");
        }
        if (getKey == null) {
            Log.e(TAG, "dataPrimaryKey is null");
            // handle the case where the key is null
        } else {
            Log.i(TAG, "Updating data with key " + getKey);
            // perform the database update using the non-null key
            if (setMahasiswa == null) {
                Log.e(TAG, "setMahasiswa is null");
            } else {
                Log.i(TAG, "setMahasiswa = " + setMahasiswa.toString());
            }
            database.child("Admin")
                    .child("Mahasiswa")
                    .child(getKey)
                    .setValue(setMahasiswa)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "Data updated successfully");
                            nimBaru.setText("");
                            namaBaru.setText("");
                            nohpBaru.setText("");
                            emailBaru.setText("");
                            ipkBaru.setText("");
                            alamatBaru.setText("");
                            tgllahirBaru.setText("");
                            Toast.makeText(Update.this, "Data Berhasil di Update!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to update data: " + e.getMessage());
                            // handle the failure case
                        }
                    });
        }
    }

    public void rbclick(View v) {
    }
}