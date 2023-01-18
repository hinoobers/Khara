package org.hinoob.khara.listener.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.Khara;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.util.PacketValidator;

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

        user.trackerContainer.getAll().forEach(tracker -> tracker.handle(event));
        user.checkContainer.getAll().forEach(check -> check.handle(event)); // CHECK CALLED HERE
        user.trackerContainer.getAll().forEach(tracker -> tracker.handlePost(event)); // TRACKKE cALLED HERE
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        KharaUser user = Khara.getInstance().getDataManager().get(event.getUser());
        if(user == null) return; // Very first login packets

        user.trackerContainer.getAll().forEach(tracker -> tracker.handle(event));
        user.checkContainer.getAll().forEach(check -> check.handle(event));
        user.trackerContainer.getAll().forEach(tracker -> tracker.handlePost(event));
    }
}
