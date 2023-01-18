package org.hinoob.khara.tracker.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import lombok.Getter;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.tracker.Tracker;
import org.hinoob.khara.util.Pair;

import java.util.*;

public class TransactionTracker extends Tracker {

    public TransactionTracker(KharaUser user) {
        super(user);
    }

    @Getter private Deque<List<Runnable>> tasks = new LinkedList<>();

    @Override
    public void handle(PacketReceiveEvent event) {
        if(event.getPacketType() != PacketType.Play.Client.WINDOW_CONFIRMATION) return;
        WrapperPlayClientWindowConfirmation wrapper = new WrapperPlayClientWindowConfirmation(event);

        // We need to check if we send that transaction packet
        if(tasks.isEmpty()) return;

        tasks.poll().forEach(Runnable::run);
    }

    @Override
    public void handle(PacketSendEvent event) {
        if(event.getPacketType() != PacketType.Play.Server.WINDOW_CONFIRMATION) return;
        WrapperPlayServerWindowConfirmation wrapper = new WrapperPlayServerWindowConfirmation(event);

        tasks.add(new ArrayList<>());
    }

    @Override
    public void handlePost(PacketReceiveEvent event) {

    }

    @Override
    public void handlePost(PacketSendEvent event) {

    }

    public void add(Runnable runnable){
        if(tasks.isEmpty()){
            runnable.run();
            return;
        }
        tasks.peekLast().add(runnable);
    }

    public void sendTransaction(){
        if(user.getUser().getConnectionState() != ConnectionState.PLAY) return;

        user.getUser().sendPacket(new WrapperPlayServerWindowConfirmation(0, (short)69, false));
    }
}
