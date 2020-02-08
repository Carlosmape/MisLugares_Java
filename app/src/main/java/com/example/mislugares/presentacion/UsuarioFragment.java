package com.example.mislugares.presentacion;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mislugares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario, contenedor, false);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView nombre = (TextView) vista.findViewById(R.id.nombre);
        nombre.setText(usuario.getDisplayName());
        TextView email = (TextView) vista.findViewById(R.id.email);
        email.setText(usuario.getEmail());
        TextView provider = (TextView) vista.findViewById(R.id.proveedor);
        provider.setText(usuario.getProviderId());
        TextView phone = (TextView) vista.findViewById(R.id.movilenumber);
        phone.setText(usuario.getPhoneNumber());
        TextView uid = (TextView) vista.findViewById(R.id.iduser);
        uid.setText(usuario.getUid());
        return vista;
    }
}