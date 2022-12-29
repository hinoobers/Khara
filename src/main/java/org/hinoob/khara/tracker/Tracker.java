package org.hinoob.khara.tracker;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.data.KharaUser;

public abstract class Tracker {

    protected final KharaUser user;

    public Tracker(KharaUser user){
        this.user = user;
    }

    public abstract void handle(PacketReceiveEvent event);
    public abstract void handle(PacketSendEvent event);
    public abstract void handlePost(PacketReceiveEvent event);
    public abstract void handlePost(PacketSendEvent event);
}
