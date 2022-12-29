package org.hinoob.khara.tracker.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import lombok.Getter;
import org.hinoob.khara.Khara;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.tracker.Tracker;
import org.hinoob.khara.util.TrackerOption;

public class FlyingTracker extends Tracker {

    public FlyingTracker(KharaUser user) {
        super(user);
    }

    @Override
    public void handle(PacketReceiveEvent event, TrackerOption option) {
        if(option == TrackerOption.AFTER_CHECKS) return;
        if(!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) return;

        WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);

        if(wrapper.hasRotationChanged()){
            user.setRotation(wrapper.getLocation().getYaw(), wrapper.getLocation().getPitch());
        }

        if(wrapper.hasPositionChanged()){
            user.setPosition(wrapper.getLocation().getX(), wrapper.getLocation().getY(), wrapper.getLocation().getZ());
        }
    }

    @Override
    public void handle(PacketSendEvent event, TrackerOption option) {

    }
}
