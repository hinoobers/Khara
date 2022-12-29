package org.hinoob.khara.util;

import lombok.Getter;
import lombok.Setter;

public class Rotation {

    @Getter @Setter
    private float yaw, pitch;

    public Rotation(float yaw, float pitch){
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation setYawCopied(float yaw){
        Rotation copy = copy();
        copy.setYaw(yaw);
        return copy;
    }

    public Rotation setPitchCopied(float pitch){
        Rotation copy = copy();
        copy.setPitch(pitch);
        return copy;
    }

    public Rotation copy(){
        return new Rotation(yaw, pitch);
    }
}
