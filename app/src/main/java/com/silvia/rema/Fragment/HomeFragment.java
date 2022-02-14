package com.silvia.rema.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.silvia.rema.API;
import com.silvia.rema.Adapter.AdapterBerita;
import com.silvia.rema.Adapter.AdapterFitur;
import com.silvia.rema.BeritaActivity;
import com.silvia.rema.Model.ModelBerita;
import com.silvia.rema.Model.ModelFitur;
import com.silvia.rema.R;
import com.silvia.rema.TinyDB;
import com.silvia.rema.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private List<ModelFitur> dataFitur;
    private List<ModelBerita> dataBerita;
    private List<ModelBerita> dataInfo;
    API api;
    String nama;
    TinyDB tinyDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentHomeBinding.inflate(inflater,container,false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = binding.getRoot();
        api = new API();
        AndroidNetworking.initialize(getContext());
        tinyDB = new TinyDB(getContext());

        nama = tinyDB.getString("keyNamaMhs");
        binding.namaUser.setText(nama);

        binding.recFitur.setHasFixedSize(true);
        binding.recFitur.setLayoutManager(new GridLayoutManager(getContext(),4));
                dataFitur = new ArrayList<>();
        getFitur();

        binding.recBerita.setHasFixedSize(true);
        binding.recBerita.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
        dataBerita = new ArrayList<>();
        getUpiToday();

        binding.recInfo.setHasFixedSize(true);
        binding.recInfo.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
        dataInfo = new ArrayList<>();
        getUpiInfo();

        binding.lihat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BeritaActivity.class);
                i.putExtra("kategori","today");
                startActivity(i);
            }
        });

        binding.lihat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),BeritaActivity.class);
                i.putExtra("kategori","berita");
                startActivity(i);
            }
        });
        return  view;



    }



    private void getFitur() {
       AndroidNetworking.get(api.URL_FITUR)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataFitur.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelFitur Isi = gson.fromJson(data + "", ModelFitur.class);
                                    dataFitur.add(Isi);
                                }
                                AdapterFitur adapter = new AdapterFitur(dataFitur);
                                binding.recFitur.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
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

    private void getUpiToday() {
        AndroidNetworking.get(api.URL_UPI_TODAY)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataBerita.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelBerita Isi = gson.fromJson(data + "", ModelBerita.class);
                                    dataBerita.add(Isi);
                                }
                                AdapterBerita adapter = new AdapterBerita(dataBerita);
                                binding.recBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
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
        AndroidNetworking.get(api.URL_UPI_INFO)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {
                                JSONArray res = response.getJSONArray("data");
                                Gson gson = new Gson();
                                dataInfo.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelBerita Isi = gson.fromJson(data + "", ModelBerita.class);
                                    dataInfo.add(Isi);
                                }
                                AdapterBerita adapter = new AdapterBerita(dataInfo);
                                binding.recInfo.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
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