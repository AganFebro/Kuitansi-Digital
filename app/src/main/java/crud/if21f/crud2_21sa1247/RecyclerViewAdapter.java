package crud.if21f.crud2_21sa1247;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    ArrayList<data_mahasiswa> listMahasiswa;
    MainActivity context;
    ArrayList<data_mahasiswa> listMahasiswaSearch;

    DatabaseReference database;
    StorageReference storage;


    Filter setSearch = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<data_mahasiswa> filterMahasiswa = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterMahasiswa.addAll(listMahasiswaSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (data_mahasiswa item : listMahasiswaSearch) {
                    if (item.getNama().toLowerCase().contains(filterPattern)) {
                        filterMahasiswa.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterMahasiswa;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listMahasiswa.clear();
            listMahasiswa.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public RecyclerViewAdapter(ArrayList<data_mahasiswa> listMahasiswa, MainActivity context) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        this.listMahasiswaSearch = listMahasiswa;
    }

    @Override
    public Filter getFilter() {
        return setSearch;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        data_mahasiswa dm = listMahasiswa.get(position);

        final String NIM = listMahasiswa.get(position).getNim();
        final String Nama = listMahasiswa.get(position).getNama();
        final String Fakultas = listMahasiswa.get(position).getFakultas();
        final String Prodi = listMahasiswa.get(position).getProdi();
        final String Goldar = listMahasiswa.get(position).getHasilgol();
        final String JenisKelamin = listMahasiswa.get(position).getRjeniskelamin();
        final String Tgllahir = listMahasiswa.get(position).getTgllahir();
        final String Nohp = listMahasiswa.get(position).getNohp();
        final String Crudemail = listMahasiswa.get(position).getCrudemail();
        final String Ipk = listMahasiswa.get(position).getIpk();
        final String Alamat = listMahasiswa.get(position).getAlamat();
        final String Gambar = listMahasiswa.get(position).getGambar();

        holder.NIM.setText(": " + NIM);
        holder.Nama.setText(": " + Nama);
        holder.Fakultas.setText(": " + Fakultas);
        holder.Prodi.setText(": " + Prodi);
        holder.Goldar.setText(": " + Goldar);
        holder.JenisKelamin.setText(": " + JenisKelamin);
        holder.Tgllahir.setText(": " + Tgllahir);
        holder.Nohp.setText(": " + Nohp);
        holder.Crudemail.setText(": " + Crudemail);
        holder.Ipk.setText(": " + Ipk);
        holder.Alamat.setText(": " + Alamat);

        if (isEmpty(Gambar)) {
            holder.Gambar.setImageResource(R.drawable.people);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(Gambar.trim())
                    .into(holder.Gambar);
        }

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Apa yang akan anda pilih?");
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNIM", listMahasiswa.get(position).getNim());
                                bundle.putString("dataNama", listMahasiswa.get(position).getNama());
                                bundle.putString("dataFakultas", listMahasiswa.get(position).getFakultas());
                                bundle.putString("dataGoldar", listMahasiswa.get(position).getHasilgol());
                                bundle.putString("dataJenisKelamin", listMahasiswa.get(position).getRjeniskelamin());
                                bundle.putString("dataTanggal", listMahasiswa.get(position).getTgllahir());
                                bundle.putString("dataNohp", listMahasiswa.get(position).getNohp());
                                bundle.putString("dataCrudemail", listMahasiswa.get(position).getCrudemail());
                                bundle.putString("dataIpk", listMahasiswa.get(position).getIpk());
                                bundle.putString("dataAlamat", listMahasiswa.get(position).getAlamat());
                                bundle.putString("dataPrimaryKey", listMahasiswa.get(position).getKey());
                                bundle.putString("dataProdi", listMahasiswa.get(position).getProdi());
                                bundle.putString("data_gambar", listMahasiswa.get(position).getGambar());

                                Intent intent = new Intent(v.getContext(), Update.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;

                            case 1:
                                AlertDialog.Builder alertt = new AlertDialog.Builder(v.getContext());
                                alertt.setTitle("Apakah anda yakin menghapus data ini?");
                                alertt.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        data_mahasiswa data = new data_mahasiswa();
                                        data.setKey(dm.getKey());
                                        delete(database, data);
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                                alertt.create();
                                alertt.show();

                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView NIM, Nama, Prodi, Fakultas, Goldar, JenisKelamin, Tgllahir, Nohp, Crudemail, Alamat, Ipk;
        public ImageView Gambar;
        public LinearLayout ListItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NIM = itemView.findViewById(R.id.nim);
            Nama = itemView.findViewById(R.id.nama);
            Fakultas = itemView.findViewById(R.id.fakultas);
            Prodi = itemView.findViewById(R.id.prodi);
            Goldar = itemView.findViewById(R.id.hasilgol);
            JenisKelamin = itemView.findViewById(R.id.rjeniskelamin);
            Tgllahir = itemView.findViewById(R.id.tgllahir);
            Nohp = itemView.findViewById(R.id.nohp);
            Crudemail = itemView.findViewById(R.id.crudemail);
            Ipk = itemView.findViewById(R.id.ipk);
            Alamat = itemView.findViewById(R.id.alamat);
            ListItem = itemView.findViewById(R.id.list_item);
            Gambar = itemView.findViewById(R.id.gambar);
        }
    }

    public void delete(FirebaseDatabase database, data_mahasiswa data) {
        database.getReference("Admin/Mahasiswa")
                .child(data.getKey())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Berhasil menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
