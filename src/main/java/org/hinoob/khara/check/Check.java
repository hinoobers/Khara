package org.hinoob.khara.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.data.KharaUser;

public abstract class Check {

    protected final KharaUser user;

    public Check(KharaUser user) {
        this.user = user;
    }


    public abstract void handle(PacketReceiveEvent event);
    public abstract void handle(PacketSendEvent event);
}
