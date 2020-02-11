package com.example.mislugares.presentacion;

import android.app.Activity;
import android.app.AuthenticationRequiredException;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.mislugares.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FederatedAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UsuarioFragment extends Fragment {
    EditText nombre;
    TextView email;
    TextView phone;
    TextView password;
    FirebaseUser usuario;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario, contenedor, false);
        usuario = FirebaseAuth.getInstance().getCurrentUser();

        // Inicialización Volley  (Hacer solo una vezen Singletono Applicaction)
        RequestQueue colaPeticiones = Volley.newRequestQueue(getActivity().getApplicationContext());
        ImageLoader lectorImagenes = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });
        // Foto de usuario
        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView fotoUsuario = (NetworkImageView) vista.findViewById(R.id.imagen);
            fotoUsuario.setImageUrl(urlImagen.toString(), lectorImagenes);
        }

        nombre = (EditText) vista.findViewById(R.id.nombre);
        nombre.setText(usuario.getDisplayName());

        password = (TextView) vista.findViewById(R.id.password);

        email = (TextView) vista.findViewById(R.id.email);
        email.setText(usuario.getEmail());

        TextView provider = (TextView) vista.findViewById(R.id.proveedor);
        provider.setText(usuario.getProviderId());

        phone = (TextView) vista.findViewById(R.id.movilenumber);
        phone.setText(usuario.getPhoneNumber());

        TextView uid = (TextView) vista.findViewById(R.id.iduser);
        uid.setText(usuario.getUid());

        Button editProfile = (Button) vista.findViewById(R.id.btn_edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nombre.getText().toString()).build();

                usuario.updateProfile(perfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) Toast.makeText(view.getContext(), "No se pudo modificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(view.getContext(), "Nombre de usuario cambiado correctamente", Toast.LENGTH_SHORT).show();
                    }
                });
                //TODO: implement change mail and password
//                if ((email.getText().toString().equals(usuario.getEmail())) || (!password.getText().toString().equals(""))) {
//                    //FirebaseAuth.getInstance().signOut();
//                    //Intent i = new Intent(getActivity(), LoginActivity.class);
//                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    //startActivity(i);
//                    //getActivity().finish();
//                    Toast.makeText(view.getContext(), "No está permitido cambiar la contraseña, correo o teléfono", Toast.LENGTH_LONG).show();
//                }
            }
        });

        Button cerrarSesion = (Button) vista.findViewById(R.id.btn_cerrar_sesion);
        cerrarSesion.setOnClickListener(SignOutAndReload);
        return vista;
    }

    private View.OnClickListener SignOutAndReload = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent i = new Intent(getActivity(), CustomMailLoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    getActivity().finish();
                }
            });
        }
    };
}
