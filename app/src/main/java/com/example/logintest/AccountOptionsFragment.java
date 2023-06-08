package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintest.databinding.FragmentAccountOptionsBinding;

public class AccountOptionsFragment extends Fragment {
    private FragmentAccountOptionsBinding binding;

    public AccountOptionsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnRegister = binding.getRoot().findViewById(R.id.register_button);
        Button btnLogin = binding.getRoot().findViewById(R.id.login_button);

        btnLogin.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),AccountActivity.class);
            intent.putExtra("action", "login");
            startActivity(intent);

        });

        btnRegister.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),AccountActivity.class);
            intent.putExtra("action", "register");
            startActivity(intent);
        });
    }
}
