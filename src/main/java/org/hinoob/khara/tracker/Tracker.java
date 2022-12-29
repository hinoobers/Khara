package org.hinoob.khara.tracker;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.util.TrackerOption;

public abstract class Tracker {

    protected final KharaUser user;

    public Tracker(KharaUser user){
        this.user = user;
    }

    public abstract void handle(PacketReceiveEvent event, TrackerOption option);
    public abstract void handle(PacketSendEvent event, TrackerOption option);
}
