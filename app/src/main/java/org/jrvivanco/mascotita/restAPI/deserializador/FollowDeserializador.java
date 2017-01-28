package org.jrvivanco.mascotita.restAPI.deserializador;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.jrvivanco.mascotita.pojo.FollowInst;
import org.jrvivanco.mascotita.restAPI.JSONKeys;
import org.jrvivanco.mascotita.restAPI.modelo.FollowResponse;

import java.lang.reflect.Type;

/**
 * Created by jrvivanco on 26/01/2017.
 */
public class FollowDeserializador implements JsonDeserializer<FollowResponse>{
    @Override
    public FollowResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gsonFollow = new Gson();
        FollowResponse followResponse = gsonFollow.fromJson(json, FollowResponse.class);
        JsonObject followObj = json.getAsJsonObject();
        JsonObject followDataObj = followObj.getAsJsonObject(JSONKeys.RESPONSE_ARRAY);
        String outgoing_status = followDataObj.get(JSONKeys.USER_OUTGOING_STATUS).getAsString();

        FollowInst followInst = new FollowInst();
        followInst.setOut_status(outgoing_status);

        followResponse.setFollowInst(followInst);

        return followResponse;
    }
}
