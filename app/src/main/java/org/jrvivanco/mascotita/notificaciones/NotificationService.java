package org.jrvivanco.mascotita.notificaciones;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import org.jrvivanco.mascotita.MainActivity;
import org.jrvivanco.mascotita.R;
import org.jrvivanco.mascotita.menu_opciones.PedirUsuarioInstagram;

import java.util.Map;

/**
 * Created by jrvivanco on 21/01/2017.
 */
public class NotificationService extends FirebaseMessagingService {

    public static final String TAG ="Notificacion Firebase";
    public static final int NOTIFICACION_ID = 001;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Map datos = remoteMessage.getData();
        String nomUsuario=datos.get("nomUsu").toString();
        String usuarioID=datos.get("idUsu").toString();
        String urlUsuario=datos.get("urlUsu").toString();

        //2016-08-12 quién hace click
        String fromNomUsuario=datos.get("fromNomUsr").toString();
        String fromUsuarioID=datos.get("fromIdUsr").toString();
        String fromUrlUsuario=datos.get("fromUrlUsr").toString();

        Log.e(TAG, "From:  " + remoteMessage.getFrom());
        //Log.e(TAG, "To: " + remoteMessage.getTo());
        Log.e(TAG, "Title: " + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "Msg:   " + remoteMessage.getNotification().getBody());

        Log.e(TAG, "nomUsu:  " + nomUsuario);
        Log.e(TAG, "idUsu:  " + usuarioID);
        Log.e(TAG, "urlUsu:  " + urlUsuario);

        Log.e(TAG, "fromNomUsu:  " + fromNomUsuario);
        Log.e(TAG, "fromIdUsu:  " + fromUsuarioID);
        Log.e(TAG, "fromUrlUsu:  " + fromUrlUsuario);


        Intent intentVerPerfilLocal = new Intent();
        //Intent i = new Intent(this,MainActivity.class);
        intentVerPerfilLocal.putExtra("usuario", nomUsuario);
        intentVerPerfilLocal.putExtra("usuarioID", usuarioID);
        intentVerPerfilLocal.putExtra("usuarioURL", urlUsuario);
        intentVerPerfilLocal.putExtra("fromUsuario", fromNomUsuario);
        intentVerPerfilLocal.putExtra("fromUsuarioID", fromUsuarioID);
        intentVerPerfilLocal.putExtra("fromUsuarioURL", fromUrlUsuario);
        intentVerPerfilLocal.setAction("VER_USUARIO");

        Intent intentPerfilUsuario = new Intent();
        //Intent i = new Intent(this,MainActivity.class);
        intentPerfilUsuario.putExtra("usuario", nomUsuario);
        intentPerfilUsuario.putExtra("usuarioID", usuarioID);
        intentPerfilUsuario.putExtra("usuarioURL", urlUsuario);
        intentPerfilUsuario.putExtra("fromUsuario", fromNomUsuario);
        intentPerfilUsuario.putExtra("fromUsuarioID", fromUsuarioID);
        intentPerfilUsuario.putExtra("fromUsuarioURL", fromUrlUsuario);
        intentPerfilUsuario.setAction("VER_USUARIO_CLICK");

        Intent intentFollowUnfollow = new Intent();
        //intentFollowUnfollow.putExtra("usuario", nomUsuario);
        //intentFollowUnfollow.putExtra("usuarioID", usuarioID);
        //intentFollowUnfollow.putExtra("usuarioURL", urlUsuario);
        intentFollowUnfollow.putExtra("fromUsuario", fromNomUsuario);
        intentFollowUnfollow.putExtra("fromUsuarioID", fromUsuarioID);
        //intentFollowUnfollow.putExtra("fromUsuarioURL", fromUrlUsuario);
        intentFollowUnfollow.setAction("FOLLOW_UNFOLLOW");

        //PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentPerfil = PendingIntent.getBroadcast(this,0,intentVerPerfilLocal,PendingIntent.FLAG_UPDATE_CURRENT); //del usuario local
        PendingIntent pendingIntentUsuario = PendingIntent.getBroadcast(this,0,intentPerfilUsuario,PendingIntent.FLAG_UPDATE_CURRENT); //del usuario que hizo click
        PendingIntent pendingIntentFollowUnfollow = PendingIntent.getBroadcast(this,0,intentFollowUnfollow,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //2016-08-11 para que la acción sólo se vea en Wear:
        NotificationCompat.Action accionVerPerfilLocal =
                new NotificationCompat.Action.Builder(R.drawable.ic_full_user, getString(R.string.txt_ver_profile), pendingIntentPerfil)
                        .build();
        NotificationCompat.Action accionVerPerfilUsuario =
                new NotificationCompat.Action.Builder(R.drawable.ic_full_pics, getString(R.string.txt_ver_usuario), pendingIntentUsuario)
                        .build();
        NotificationCompat.Action accionFollowUnfollow =
                new NotificationCompat.Action.Builder(R.drawable.ic_full_follow,getString(R.string.txt_follow_unfollow), pendingIntentFollowUnfollow)
                        .build();

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.bk_androidwear_stones))
                        .setGravity(Gravity.CENTER_VERTICAL)
                ;


        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.dog)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(sonido)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .extend(wearableExtender.addAction(accionVerPerfilLocal))
                .extend(wearableExtender.addAction(accionFollowUnfollow))
                .extend(wearableExtender.addAction(accionVerPerfilUsuario))
                //.addAction(R.drawable.ic_full_user,getString(R.string.ver_profile),pendingIntent) //esto lo saca también en la notificación del tlfno
                ;
//        notificacion.setExtras(parametros);

        /* sustituido al poner notificaciones Wear
        NotificationManager notificacionManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        */
        NotificationManagerCompat notificacionManager = NotificationManagerCompat.from(this);
        notificacionManager.notify(NOTIFICACION_ID, notificacion.build());
    }
}
