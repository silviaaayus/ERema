package com.silvia.rema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.rema.databinding.ActivityChangePasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    API api;
    TinyDB tinyDB;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AndroidNetworking.initialize(this);
        api = new API();
        tinyDB = new TinyDB(this);

         id = tinyDB.getString("keyIdUser");

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.newPass.getText().toString().equals(binding.confirm.getText().toString())){

                    editPassword();



                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Password Tidak Valid!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void editPassword(){
            AndroidNetworking.post(api.URL_EDIT_PASSWORD)
                .addBodyParameter("id", id)
                .addBodyParameter("password", binding.newPass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respon = response.getString("response");
                            if (respon.equalsIgnoreCase("Sukses")){
                                Intent i = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                                startActivity(i);
                                Toast.makeText(ChangePasswordActivity.this, "Password Telah diganti", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Kesalahan update, Kode 2"
                                ,Toast.LENGTH_LONG).show();

                    }


                });
    }
}