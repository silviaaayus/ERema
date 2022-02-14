package com.silvia.rema.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.rema.API;
import com.silvia.rema.DetailBeritaActivity;
import com.silvia.rema.R;
import com.silvia.rema.TinyDB;
import com.silvia.rema.databinding.FragmentHomeBinding;
import com.silvia.rema.databinding.FragmentSearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;


public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    API api;
    HashMap<String,String> hasBerita = new HashMap<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> judulberita = new ArrayList<>();
    ArrayList<String> idberita = new ArrayList<>();
    String id,judul,isi,waktu_rilis,gambar,penulis;
    TinyDB tinyDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = binding.getRoot();
        api = new API();
        tinyDB = new TinyDB(getContext());
        getAllBerita();
        return view;
    }

    public void getAllBerita(){
        AndroidNetworking.get(api.URL_BERITA_ALL)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {

                                JSONArray res = response.getJSONArray("data");
                                for(int i = 0; i<res.length();i++){
                                    JSONObject data = res.getJSONObject(i);


                                    judulberita.add(data.getString("judul")+" | "+data.getString("name"));
                                    idberita.add(data.getString("id"));


                                    hasBerita.put(
                                            data.getString("judul")+" | "+data.getString("name")
                                            ,data.getString("judul")



                                    );


                                }

                                setAdapter();


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

    public void getCariBerita(String sberita){
       AndroidNetworking.get(api.URL_SEARCH+sberita)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("true")) {

                                JSONArray res = response.getJSONArray("data");
                                JSONObject data = res.getJSONObject(0);

                                id = data.getString("id");
                                judul = data.getString("judul");
                                isi = data.getString("isi");
                                waktu_rilis = data.getString("waktu_rilis");
                                gambar = data.getString("thumbnail");
                                penulis = data.getString("name");

                                Intent i = new Intent(getContext(), DetailBeritaActivity.class);
                                i.putExtra("isi",isi);
                                i.putExtra("judul",judul);
                                i.putExtra("gambar",gambar);
                                i.putExtra("tgl",waktu_rilis);
                                i.putExtra("nama",penulis);
                                startActivity(i);



                                Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_SHORT).show();


                            }else {

                                Toast.makeText(getContext(), "Data Kosong, Silahkan Isi Data Anda", Toast.LENGTH_SHORT).show();

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

    private void setAdapter() {
        adapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, judulberita);

        binding.cariNama.setAdapter(adapter);

        binding.cariNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (binding.cariNama.isPopupShowing()){
//                    view.findViewById(R.id.btn_caripasien).setVisibility(GONE);
                }
                else{
//                    view.findViewById(R.id.btn_caripasien).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        binding.cariNama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);
                String juduls = hasBerita.get(selection);

//                Toast.makeText(getContext(), selection, Toast.LENGTH_SHORT).show();
                getCariBerita(juduls);
            }
        });

    }

}