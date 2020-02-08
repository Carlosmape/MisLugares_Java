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
        return vista;
    }
}