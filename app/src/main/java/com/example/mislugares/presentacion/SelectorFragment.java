package com.example.mislugares.presentacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mislugares.Aplicacion;
import com.example.mislugares.R;
import com.example.mislugares.casos_uso.CasosUsoLugar;
import com.example.mislugares.datos.LugaresBD;
import com.example.mislugares.modelo.Lugar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment {
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    public static AdaptadorLugaresFirestoreUI adaptador2;
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
        usoLugar = new CasosUsoLugar(getActivity(), this, lugares, adaptador);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = FirebaseFirestore.getInstance().collection("lugares").limit(50);
        FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        adaptador2 = new AdaptadorLugaresFirestoreUI(opciones, getContext());
        recyclerView.setAdapter(adaptador2);
        adaptador2.startListening();
        adaptador2.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) (v.getTag());
                usoLugar.mostrar(pos);
            }
        });

    }
    @Override public void onDestroy() {super.onDestroy();adaptador2.stopListening();}

    @Override
    public void onStart() {
        super.onStart();
        adaptador2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptador2.stopListening();
    }

}
