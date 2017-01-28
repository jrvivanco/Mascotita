package org.jrvivanco.mascotita.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by jrvivanco on 26/01/2017.
 * Para mostrar el perfil y timeline de cualquier usuario
 */
public class VerUsuario extends BroadcastReceiver {

    String nomUsuario;
    String usuarioID;
    String urlUsuario;
    String fromNomUsuario;
    String fromUsuarioID;
    String fromUrlUsuario;

    @Override
    public void onReceive(Context context, Intent intent) {
        String ACCION_VER_PERFIL="VER_USUARIO";
        String ACCION_VER_USUARIO="VER_USUARIO_CLICK";

        String accion = intent.getAction();
        //Toast.makeText(context, "accion: "+accion, Toast.LENGTH_LONG).show();
        if(ACCION_VER_PERFIL.equals(accion)){
            //para ver el usuario home
            nomUsuario=intent.getExtras().getString("usuario");
            usuarioID =intent.getExtras().getString("usuarioID");
            urlUsuario=intent.getExtras().getString("usuarioURL");
            //Toast.makeText(context, "Usuario que hizo click: "+fromNomUsuario, Toast.LENGTH_LONG).show();
            //Toast.makeText(context, "Usuario a mostrar: "+nomUsuario, Toast.LENGTH_LONG).show();

            reAbrirAplicacion(context);

        }
        if(ACCION_VER_USUARIO.equals(accion)){
            //para ver el usuario que hizo click
            nomUsuario=intent.getExtras().getString("fromUsuario");
            usuarioID =intent.getExtras().getString("fromUsuarioID");
            urlUsuario=intent.getExtras().getString("fromUsuarioURL");
            //Toast.makeText(context, "Usuario que hizo click: "+fromNomUsuario, Toast.LENGTH_LONG).show();
            //Toast.makeText(context, "Usuario a mostrar: "+nomUsuario, Toast.LENGTH_LONG).show();

            reAbrirAplicacion(context);

        }

    }

    public void reAbrirAplicacion(Context context) {
        //en las variables estará o el perfil del usuario local, o el del usuario que lanzó en raiteo
        Intent i = new Intent();
        i.putExtra("usuario", nomUsuario);               //ToDo: debería poner los nombres en Strings
        i.putExtra("usuarioID", usuarioID);                  //ToDo: debería poner los nombres en Strings
        i.putExtra("usuarioURL", urlUsuario);                //ToDo: debería poner los nombres en Strings

        i.setClassName("org.jrvivanco.mascotita", "org.jrvivanco.mascotita.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }


}
