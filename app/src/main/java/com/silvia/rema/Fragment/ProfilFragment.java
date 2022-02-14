package com.silvia.rema.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.silvia.rema.ChangePasswordActivity;
import com.silvia.rema.EditProfilActivity;
import com.silvia.rema.LoginActivity;
import com.silvia.rema.MainActivity;
import com.silvia.rema.R;
import com.silvia.rema.TinyDB;
import com.silvia.rema.databinding.FragmentProfilBinding;
import com.silvia.rema.databinding.FragmentSearchBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ProfilFragment extends Fragment {
    private FragmentProfilBinding binding;
    TinyDB tinyDB;
    String nama;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.linePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        binding.Lineedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), EditProfilActivity.class);
                startActivity(i);
            }
        });

        binding.btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tinyDB = new TinyDB(getContext());
        nama = tinyDB.getString("keyNamaMhs");
        binding.namapasienProfil.setText(nama);
        if (tinyDB.getBoolean("keyGoogleSignin")){
            Picasso
                    .get()
                    .load(tinyDB.getString("keyFotoGmail"))
                    .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                    .placeholder(R.drawable.ic_baseline_home_24)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .centerInside()
                    .into(binding.imgProfil);
        }else{
            Picasso
                    .get()
                    .load(tinyDB.getString("keyFoto"))
                    .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                    .placeholder(R.drawable.ic_baseline_home_24)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .centerInside()
                    .into(binding.imgProfil);
        }

    }

    private void signOut() {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        tinyDB.clear();
                        Intent intent=new Intent(getContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }
}