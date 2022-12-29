package org.hinoob.khara.listener.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.Khara;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.util.PacketValidator;
import org.hinoob.khara.util.TrackerOption;

public class PacketListener extends PacketListenerAbstract {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        KharaUser user = Khara.getInstance().getDataManager().get(event.getUser());
        if(user == null) return; // Very first login packets

        if(!PacketValidator.isValid(event)){
            user.disconnect("Invalid packet received");
            event.setCancelled(true);
            return;
        }

        user.trackerContainer.execute(event, TrackerOption.BEFORE_CHECKS);
        user.checkContainer.execute(event);
        user.trackerContainer.execute(event, TrackerOption.AFTER_CHECKS);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        KharaUser user = Khara.getInstance().getDataManager().get(event.getUser());
        if(user == null) return; // Very first login packets

        user.trackerContainer.execute(event, TrackerOption.BEFORE_CHECKS);
        user.checkContainer.execute(event);
        user.trackerContainer.execute(event, TrackerOption.AFTER_CHECKS);
    }
}
