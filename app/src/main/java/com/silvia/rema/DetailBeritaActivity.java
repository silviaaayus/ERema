package com.silvia.rema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.silvia.rema.databinding.ActivityDetailBeritaBinding;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailBeritaActivity extends AppCompatActivity {
    private ActivityDetailBeritaBinding binding;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBeritaBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        api = new API();
        Intent i = getIntent();

        binding.txtJudul.setText(i.getStringExtra("judul"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtIsi.setText(Html.fromHtml(i.getStringExtra("isi"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.txtIsi.setText(Html.fromHtml(i.getStringExtra("isi")));
        }

        Picasso.get().load(api.URL_GAMBAR+i.getStringExtra("gambar")).into(binding.imgBerita);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.penulisBeritaLihat.setText(i.getStringExtra("nama"));

        String date = i.getStringExtra("tgl");

        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        try {
            String tglBaru= dateFormat.format(df.parse(date));
            binding.tanggalBerita.setText(tglBaru);
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }
}