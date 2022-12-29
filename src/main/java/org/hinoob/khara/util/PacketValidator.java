package org.hinoob.khara.util;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;

// This class is designated to prevent obvious invalid packets, used to crash or harm the server
// I don't have plans to create checks related to BadPackets, if they pass this class, then the actual checks have to do the job of validating what's right and what's invalid
public class PacketValidator {

    public static boolean isValid(PacketReceiveEvent event){
        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())){
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

            if(wrapper.hasPositionChanged()){
                if(Double.isNaN(wrapper.getLocation().getX()) || Double.isNaN(wrapper.getLocation().getX()) || Double.isNaN(wrapper.getLocation().getZ())){
                    return false;
                }

                if(wrapper.getLocation().getX() >= Double.MAX_VALUE || wrapper.getLocation().getY() >= Double.MAX_VALUE || wrapper.getLocation().getZ() >= Double.MAX_VALUE){
                    return false;
                }
            }

            if(wrapper.hasRotationChanged()){
                if(Math.abs(wrapper.getLocation().getPitch()) > 90) {
                    return false;
                }
            }
        }

        return true;
    }
}
