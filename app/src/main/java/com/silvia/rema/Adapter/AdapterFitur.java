package com.silvia.rema.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.rema.API;
import com.silvia.rema.BeritaActivity;
import com.silvia.rema.EventActivity;
import com.silvia.rema.MapsTes;
import com.silvia.rema.Model.ModelFitur;
import com.silvia.rema.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFitur extends RecyclerView.Adapter<AdapterFitur.ViewHolder> {
    Context context;
    List<ModelFitur> dataFitur;

    public AdapterFitur(List<ModelFitur> dataFitur) {
        this.dataFitur = dataFitur;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fitur,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        API api = new API();
        ModelFitur data = dataFitur.get(position);
        holder.fitur.setText(data.getNama());

        String fitur = dataFitur.get(position).getNama();

        Picasso.get()
                .load(api.URL_GAMBAR+data.getGambar())
                .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                .placeholder(R.drawable.ic_baseline_home_24)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .centerInside()
                .into(holder.imgFitur);

        if (fitur.equalsIgnoreCase("Up! Maps")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MapsTes.class);
                    context.startActivity(i);
                }
            });

        }
        else if (fitur.equalsIgnoreCase("Up! Event")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, EventActivity.class);
                    context.startActivity(i);
                }
            });
        }else if (fitur.equalsIgnoreCase("Up! Today")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, BeritaActivity.class);
                    i.putExtra("kategori","today");
                    context.startActivity(i);
                }
            });
        } else if (fitur.equalsIgnoreCase("Up! Beasiswa")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,BeritaActivity.class);
                    i.putExtra("kategori","beasiswa");
                    context.startActivity(i);
                }
            });
        } else if (fitur.equalsIgnoreCase("Up! Volunteer")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,BeritaActivity.class);
                    i.putExtra("kategori", "volunteer");
                    context.startActivity(i);
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return dataFitur.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fitur;
        ImageView imgFitur;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            fitur = itemView.findViewById(R.id.namaFitur);
            imgFitur= itemView.findViewById(R.id.imgFitur);
        }
    }
}
