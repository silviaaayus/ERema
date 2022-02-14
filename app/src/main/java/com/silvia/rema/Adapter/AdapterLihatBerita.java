package com.silvia.rema.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.rema.API;
import com.silvia.rema.DetailBeritaActivity;
import com.silvia.rema.Model.ModelBerita;
import com.silvia.rema.Model.ModelFitur;
import com.silvia.rema.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdapterLihatBerita extends RecyclerView.Adapter<AdapterLihatBerita.ViewHolder> {

    Context context;
    List<ModelBerita> dataLihatBerita;

    public AdapterLihatBerita(List<ModelBerita> dataLihatBerita) {
        this.dataLihatBerita = dataLihatBerita;
    }

    @NonNull
    @Override
    public AdapterLihatBerita.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lihat_berita,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLihatBerita.ViewHolder holder, int position) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        API api = new API();
        ModelBerita dataBerita = dataLihatBerita.get(position);
       holder.judul.setText(dataBerita.getJudul());



        String date = dataBerita.getWaktu_rilis();


        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        try {
            String tglBaru= dateFormat.format(df.parse(date));
            holder.waktu.setText(tglBaru);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.get()
                .load(api.URL_GAMBAR+dataBerita.getThumbnail())
                .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                .placeholder(R.drawable.ic_baseline_home_24)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .centerInside()
                .into(holder.gambar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailBeritaActivity.class);
                i.putExtra("isi",dataBerita.getIsi());
                i.putExtra("judul",dataBerita.getJudul());
                i.putExtra("gambar",dataBerita.getThumbnail());
                i.putExtra("nama",dataBerita.getName());
                i.putExtra("tgl",dataBerita.getWaktu_rilis());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLihatBerita.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul,waktu,penulis;
        ImageView gambar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            judul = itemView.findViewById(R.id.titleBerita);
            waktu = itemView.findViewById(R.id.tanggalBerita);
            penulis = itemView.findViewById(R.id.penulisBeritaLihat);
            gambar = itemView.findViewById(R.id.gambarBerita);

        }
    }
}
