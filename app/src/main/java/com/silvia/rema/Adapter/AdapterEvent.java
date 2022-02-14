package com.silvia.rema.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.rema.Model.ModelEvent;
import com.silvia.rema.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.ViewHolder> {
    Context context;
    List<ModelEvent> dataEvent;

    public AdapterEvent(List<ModelEvent> dataEvent) {
        this.dataEvent = dataEvent;
    }

    @NonNull
    @Override
    public AdapterEvent.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEvent.ViewHolder holder, int position) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SimpleDateFormat dateFormatter;
        holder.judul.setText(dataEvent.get(position).getNama_event());
        holder.jam.setText(dataEvent.get(position).getJam_mulai());
        holder.lokasi.setText(dataEvent.get(position).getLokasi_event());
        String tgl = dataEvent.get(position).getTanggal_event();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        try {
            String tglBaru= dateFormat.format(df.parse(tgl));
            holder.tgl.setText(tglBaru);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul,jam,lokasi,tgl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            judul = itemView.findViewById(R.id.judul_event);
            jam = itemView.findViewById(R.id.jam_event);
            lokasi = itemView.findViewById(R.id.lokasi);
            tgl = itemView.findViewById(R.id.tglEvent);
        }
    }
}
