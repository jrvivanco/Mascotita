package org.jrvivanco.mascotita.pojo;

/**
 * Created by jrvivanco on 26/01/2017.
 */
public class FollowInst {
    private String out_status;

    public FollowInst(String out_status) {
        this.out_status = out_status;
    }

    public FollowInst() {
    }

    public String getOut_status() {
        return out_status;
    }

    public void setOut_status(String out_status) {
        this.out_status = out_status;
    }
}
