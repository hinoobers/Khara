package org.hinoob.khara.listener.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import org.hinoob.khara.Khara;

public class UserListener extends PacketListenerAbstract {

    @Override
    public void onUserLogin(UserLoginEvent event) {
        Khara.getInstance().getDataManager().create(event.getUser());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        Khara.getInstance().getDataManager().destroy(event.getUser());
    }
}
