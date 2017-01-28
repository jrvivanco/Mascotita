package org.jrvivanco.mascotita.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import org.jrvivanco.mascotita.pojo.FollowInst;
import org.jrvivanco.mascotita.restAPI.ConstantesRestAPI;
import org.jrvivanco.mascotita.restAPI.EndPointsAPI;
import org.jrvivanco.mascotita.restAPI.adapter.RestAPIAdapter;
import org.jrvivanco.mascotita.restAPI.modelo.FollowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jrvivanco on 26/01/2017.
 */
 
public class DarFollowUnfollow extends BroadcastReceiver {
    String fromNomUsuario;
    String fromUsuarioID;
    //String fromUrlUsuario;

    String mensaje="";

    @Override
    public void onReceive(Context context, Intent intent) {
        String ACCION_FOLLOW_UNFOLLOW="FOLLOW_UNFOLLOW";
        String accion = intent.getAction();
        if(accion.equals(ACCION_FOLLOW_UNFOLLOW)){
            fromNomUsuario=intent.getExtras().getString("fromUsuario");
            fromUsuarioID =intent.getExtras().getString("fromUsuarioID");
            //fromUrlUsuario=intent.getExtras().getString("fromUsuarioURL");
            //Toast.makeText(context, "El usuario "+fromNomUsuario+"("+fromUsuarioID+") ha hecho click en follow/unfollow", Toast.LENGTH_SHORT).show();
            //comprobar si lo sigo:
            mensaje="El usuario "+fromNomUsuario+" originó la notificación.";
            getUserFollow(context,fromUsuarioID);
            //si lo sigo hacer unfollow, ni no lo sigo hacer follow
        }

    }

    public void getUserFollow(final Context ctxt, final String userID){

        final String ACCION_SEGUIR="follow";
        final String ACCION_NOSEGUIR="unfollow";

        RestAPIAdapter restAPIAdapter = new RestAPIAdapter();
        Gson gsonFollow = restAPIAdapter.construyeGsonDeserializadorFollow();
        final EndPointsAPI endPointsAPI = restAPIAdapter.establecerConexionRestAPIInstagram(gsonFollow);
        Call<FollowResponse> followResponseCall = endPointsAPI.getUserFollow(userID);
        followResponseCall.enqueue(new Callback<FollowResponse>() {
            @Override
            public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                FollowResponse followResponse=response.body();
                FollowInst followInst = followResponse.getFollowInst();

                //Toast.makeText(ctxt, "sigue/no_sigue: "+followInst.getOut_status(), Toast.LENGTH_SHORT).show();
                String accion_seguir="";
                if(ConstantesRestAPI.TXT_FOLLOW.equals(followInst.getOut_status())){
                    //si es "follows", se deja de seguir al usuario; si no, se le sigue
                    accion_seguir=ACCION_NOSEGUIR;
                }else{
                    accion_seguir=ACCION_SEGUIR;
                }

                Call<FollowResponse> followResponseCallSet = endPointsAPI.setUserFollow(userID,accion_seguir,ConstantesRestAPI.ACCESS_TOKEN);
                followResponseCallSet.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                        FollowResponse followResponseSet=response.body();
                        FollowInst followInstSet = followResponseSet.getFollowInst();

                        if(ConstantesRestAPI.TXT_FOLLOW.equals(followInstSet.getOut_status())){
                            mensaje=mensaje+"\nHas comenzado a seguirle.";
                        }else{
                            mensaje=mensaje+"\nHas dejado de seguirle.";
                        }

                        Toast.makeText(ctxt, mensaje, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<FollowResponse> call, Throwable t) {
                        Log.e("ERROR_SET_FOLLOW",t.toString());
                        Toast toast=Toast.makeText(ctxt, "¡Error al hacer follow!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                });
            }

            @Override
            public void onFailure(Call<FollowResponse> call, Throwable t) {
                Log.e("ERROR_FOLLOW",t.toString());
                Toast toast=Toast.makeText(ctxt, "¡Error al buscar follow!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }
}
