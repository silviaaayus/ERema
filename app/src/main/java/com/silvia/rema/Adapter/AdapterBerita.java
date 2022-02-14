package com.silvia.rema.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.rema.API;
import com.silvia.rema.DetailBeritaActivity;
import com.silvia.rema.Model.ModelBerita;
import com.silvia.rema.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class AdapterBerita extends RecyclerView.Adapter<AdapterBerita.ViewHolder> {

    Context context;
    List<ModelBerita> dataBerita;

    public AdapterBerita(List<ModelBerita> dataBerita) {
        this.dataBerita = dataBerita;
    }

    @NonNull
    @Override
    public AdapterBerita.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBerita.ViewHolder holder, int position) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        API api = new API();

         SimpleDateFormat dateFormatter;

        ModelBerita  data = dataBerita.get(position);

        holder.judul.setText(data.getJudul());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           holder.isi.setText(Html.fromHtml(data.getIsi(), Html.FROM_HTML_MODE_COMPACT));
        } else {
           holder.isi.setText(Html.fromHtml(data.getIsi()));
        }

        String date = data.getWaktu_rilis();
//
//        String[] kal = date.split("-");
//        String[] day = kal[2].split(" ");
//        String mounth = kal[1];
//        String year = kal[0];
//
//        String tanggal = (day[0]+"-"+mounth+"-"+year);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        try {
            String tglBaru= dateFormat.format(df.parse(date));
            holder.tgl.setText(tglBaru);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso
                .get()
                .load(api.URL_GAMBAR+data.getThumbnail())
                .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                .placeholder(R.drawable.ic_baseline_home_24)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .centerInside()
                .into(holder.imgberita);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailBeritaActivity.class);
                i.putExtra("isi",data.getIsi());
                i.putExtra("judul",data.getJudul());
                i.putExtra("gambar",data.getThumbnail());
                i.putExtra("nama",data.getName());
                i.putExtra("tgl",data.getWaktu_rilis());
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataBerita.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,judul,tgl,isi;
        ImageView imgberita;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            judul = itemView.findViewById(R.id.judulBerita);
            imgberita = itemView.findViewById(R.id.imgBerita);
            card = itemView.findViewById(R.id.cardBerita);
            tgl = itemView.findViewById(R.id.tglBerita);
            isi = itemView.findViewById(R.id.isi);

        }
    }
}
