package com.example.mislugares.datos;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ValoracionesFirestore {
    public static void guardarValoracion(String lugar, String usuario, Double valor) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> datos = new HashMap<>();
        datos.put("valoracion", valor);
        db.collection("lugares").document(lugar).collection("valoraciones").document(usuario).set(datos);
    }
}