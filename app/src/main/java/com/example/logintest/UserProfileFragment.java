package com.example.logintest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.logintest.databinding.FragmentUserProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    UsersManager usersManager = new UsersManager();

    TextView textUsername;
    TextView textID;
    TextView textEmail;

    public UserProfileFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        textUsername = binding.getRoot().findViewById(R.id.tvUsernameUP);
        textID = binding.getRoot().findViewById(R.id.tvIDUP);
        textEmail = binding.getRoot().findViewById(R.id.tvEmailUP);
        updateUserProfile();

        return binding.getRoot();
    }

    private void updateUserProfile(){
        //TODO get userid to set things
        SharedPreferencesManager.initialize(getActivity());
        long userId = SharedPreferencesManager.retrieveUserId();
        JSONObject userInfo = usersManager.getUserInformation();
        try {
            textUsername.setText(userInfo.getString("username"));
            textID.setText(Long.toString(userId));
            textEmail.setText(userInfo.getString("email"));
        }catch (JSONException e){
            Toast.makeText(getActivity(),"Invalid user",Toast.LENGTH_SHORT).show();
        }

    }

}

