package crud.if21f.crud2_21sa1247.ui.tools;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import crud.if21f.crud2_21sa1247.ListData;
import crud.if21f.crud2_21sa1247.MainActivity;
import crud.if21f.crud2_21sa1247.R;
import crud.if21f.crud2_21sa1247.RecyclerViewAdapter;
import crud.if21f.crud2_21sa1247.data_mahasiswa;
import crud.if21f.crud2_21sa1247.databinding.FragmentToolsBinding;

public class ToolsFragment extends Fragment {

    private FragmentToolsBinding binding;
    private RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private MainActivity mActivity;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<data_mahasiswa> dataMahasiswa;

    private FloatingActionButton fab, home;
    private EditText searchView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_data, container, false);
        recyclerView = rootView.findViewById(R.id.datalist);

        mActivity = (MainActivity) getActivity();


        GetData("");
        MyRecycleView();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void GetData(String data) {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("Mahasiswa")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataMahasiswa = new ArrayList<>();
                        dataMahasiswa.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            data_mahasiswa mahasiswa = snapshot.getValue(data_mahasiswa.class);
                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswa.add(mahasiswa);
                        }
                        adapter = new RecyclerViewAdapter(dataMahasiswa, mActivity);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Data Gagal Dimuat", Toast.LENGTH_SHORT).show();
                        Log.e("MyListActivity", error.getDetails() + " " + error.getMessage());
                    }
                });
    }

    private void MyRecycleView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration ItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        ItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.btn_underline));
        recyclerView.addItemDecoration(ItemDecoration);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}