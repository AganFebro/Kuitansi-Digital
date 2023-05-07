package crud.if21f.crud2_21sa1247.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import crud.if21f.crud2_21sa1247.ListData;
import crud.if21f.crud2_21sa1247.Login;
import crud.if21f.crud2_21sa1247.LupaEmail;
import crud.if21f.crud2_21sa1247.MainActivity;
import crud.if21f.crud2_21sa1247.R;
import crud.if21f.crud2_21sa1247.SignUpOTP;
import crud.if21f.crud2_21sa1247.crud;
import crud.if21f.crud2_21sa1247.databinding.ActivityMainBinding;
import crud.if21f.crud2_21sa1247.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button btnRead, btnCreate;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        View root = binding.getRoot();

        btnRead = (Button) v.findViewById(R.id.btn_tambah);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sekarang Pindah ke Fragment Read!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreate = (Button) v.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), crud.class);
                startActivity(intent);
            }
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}