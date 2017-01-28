package org.jrvivanco.mascotita.restAPI.modelo;

import org.jrvivanco.mascotita.pojo.FollowInst;

/**
 * Created by jrvivanco on 26/01/2017.
 */
public class FollowResponse {
  FollowInst followInst;

    public FollowInst getFollowInst() {
        return followInst;
    }

    public void setFollowInst(FollowInst followInst) {
        this.followInst = followInst;
    }
}
