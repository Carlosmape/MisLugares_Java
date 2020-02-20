package com.example.mislugares.presentacion;

import android.view.View;

import com.example.mislugares.modelo.Lugar;

public interface AdaptadorLugaresInterface {
    public String getKey(int pos);

    public int getPos(String id);

    public Lugar getItem(int pos);

    public void setOnItemClickListener(View.OnClickListener onClick);

    public void startListening();

    public void stopListening();

    public int getItemCount();

    public void notifyDataSetChanged();
}
