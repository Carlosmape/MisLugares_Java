package com.example.mislugares.presentacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mislugares.Aplicacion;
import com.example.mislugares.R;
import com.example.mislugares.casos_uso.CasosUsoLugar;
import com.example.mislugares.datos.LugaresAsinc;
import com.example.mislugares.modelo.Lugar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment {
    private LugaresAsinc lugares;
    public AdaptadorLugaresFirestoreUI adaptador;
    private CasosUsoLugar usoLugar;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_selector,
                contenedor, false);
        recyclerView = vista.findViewById(R.id.recyclerView);
        return vista;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        lugares = ((Aplicacion) getActivity().getApplication()).lugares;
        adaptador = ((Aplicacion) getActivity().getApplication()).adaptador;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usoLugar = new CasosUsoLugar(getActivity(), this, lugares, adaptador);
        recyclerView.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) (v.getTag());
                usoLugar.mostrar(pos);
            }
        });

        adaptador.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adaptador.stopListening();
    }
}
