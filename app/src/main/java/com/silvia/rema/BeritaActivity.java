package com.silvia.rema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.silvia.rema.Adapter.AdapterFitur;
import com.silvia.rema.Adapter.AdapterLihatBerita;
import com.silvia.rema.Model.ModelBerita;
import com.silvia.rema.Model.ModelFitur;
import com.silvia.rema.databinding.ActivityBeritaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeritaActivity extends AppCompatActivity {
    private ActivityBeritaBinding binding;
    API api;
    String kategori,lihat;
    private List<ModelBerita> dataLihatBerita;
    TinyDB tinyDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeritaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AndroidNetworking.initialize(this);
        api = new API();
        Intent i = getIntent();
        tinyDB = new TinyDB(this);

        kategori = i.getStringExtra("kategori");
        lihat = i.getStringExtra("lihat");


        binding.recLihatBerita.setHasFixedSize(true);
        binding.recLihatBerita.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
       dataLihatBerita = new ArrayList<>();
        if (kategori.equalsIgnoreCase("today")){
           getUpiToday();
           binding.tittle.setText("Up! Today");
       } else if (kategori.equalsIgnoreCase("beasiswa")){
            getUpiBeasiswa();
            binding.tittle.setText("Up! Beasiswa");
       }else if (kategori.equalsIgnoreCase("volunteer")){
            getUpiVolunteer();
            binding.tittle.setText("Up! Volunteer");
       }else{
            getUpiInfo();
            binding.tittle.setText("Up! Info");
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







    }
    private void getUpiToday() {
        AndroidNetworking.get(api.URL_BERITA+"today")
//                .addHeaders("Authorization", "bearer "+tinyDB.getString("keyToken"))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataLihatBerita.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelBerita Isi = gson.fromJson(data + "", ModelBerita.class);
                                    dataLihatBerita.add(Isi);
                                }
                                AdapterLihatBerita adapter = new AdapterLihatBerita(dataLihatBerita);
                                binding.recLihatBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(BeritaActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }
    private void getUpiBeasiswa() {
        AndroidNetworking.get(api.URL_BERITA+"beasiswa")
//                .addHeaders("Authorization", "bearer "+tinyDB.getString("keyToken"))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataLihatBerita.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelBerita Isi = gson.fromJson(data + "", ModelBerita.class);
                                    dataLihatBerita.add(Isi);
                                }
                                AdapterLihatBerita adapter = new AdapterLihatBerita(dataLihatBerita);
                                binding.recLihatBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(BeritaActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }
    private void getUpiVolunteer() {
        AndroidNetworking.get(api.URL_BERITA+"volunteer")
//                .addHeaders("Authorization", "bearer "+tinyDB.getString("keyToken"))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataLihatBerita.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelBerita Isi = gson.fromJson(data + "", ModelBerita.class);
                                    dataLihatBerita.add(Isi);
                                }
                                AdapterLihatBerita adapter = new AdapterLihatBerita(dataLihatBerita);
                                binding.recLihatBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(BeritaActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }
    private void getUpiInfo() {
        AndroidNetworking.get(api.URL_BERITA_ALL)
//                .addHeaders("Authorization", "bearer "+tinyDB.getString("keyToken"))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataLihatBerita.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelBerita Isi = gson.fromJson(data + "", ModelBerita.class);
                                    dataLihatBerita.add(Isi);
                                }
                                AdapterLihatBerita adapter = new AdapterLihatBerita(dataLihatBerita);
                                binding.recLihatBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(BeritaActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }


}