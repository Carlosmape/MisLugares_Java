package com.example.mislugares.presentacion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mislugares.Aplicacion;
import com.example.mislugares.R;
import com.example.mislugares.casos_uso.CasosUsoLugar;
import com.example.mislugares.datos.LugaresAsinc;
import com.example.mislugares.datos.Preferencias;
import com.example.mislugares.modelo.Lugar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mislugares.datos.Preferencias.SELECCION_MIOS;
import static com.example.mislugares.datos.Preferencias.SELECCION_TIPO;

public class SelectorFragment extends Fragment {
    private LugaresAsinc lugares;
    public static AdaptadorLugaresInterface adaptador;
    private static CasosUsoLugar usoLugar;
    private static RecyclerView recyclerView;

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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usoLugar = new CasosUsoLugar(getActivity(), this, lugares, adaptador);
        adaptador = ((Aplicacion) getActivity().getApplication()).adaptador;
        recyclerView.setAdapter((RecyclerView.Adapter)adaptador);
        ponerAdaptador(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adaptador.stopListening();
    }

    public static void ponerAdaptador(Context context) {
        Preferencias pref = Preferencias.getInstance();
        pref.inicializa(context);
        Query query = FirebaseFirestore.getInstance().collection("lugares").limit(pref.maximoMostrar());

        switch (pref.criterioSeleccion()) {
            case SELECCION_MIOS:
                query = query.whereEqualTo("creador", FirebaseAuth.getInstance().getUid());
                break;
            case SELECCION_TIPO:
                query = query.whereEqualTo("tipo", pref.tipoSeleccion());
                break;
        }

        if (pref.criterioSeleccion() != SELECCION_TIPO ||
                !(pref.criterioOrdenacion().equalsIgnoreCase("tipo")))
            query = query.orderBy(pref.criterioOrdenacion(), Query.Direction.DESCENDING);


        if (adaptador != null)
            adaptador.stopListening();
        // FirestoreRecyclerOptions<Lugar>opciones=new FirestoreRecyclerOptions
        // .Builder<Lugar>().setQuery(query, Lugar.class).build();
        // adaptador = new AdaptadorLugaresFirestoreUI(opciones, context);
        if (pref.usarFirebaseUI()) {
            FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder < Lugar > ().setQuery(query, Lugar.class).build();
            adaptador = new AdaptadorLugaresFirestoreUI(opciones, context);
        } else {
            adaptador = new AdaptadorLugaresFirestore(context, query);
        }
        ((Aplicacion) context.getApplicationContext()).adaptador = adaptador;
        adaptador.startListening();
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) (v.getTag());
                usoLugar.mostrar(pos);
            }
        });
        if (recyclerView != null) recyclerView.setAdapter((RecyclerView.Adapter) adaptador);
    }
}
