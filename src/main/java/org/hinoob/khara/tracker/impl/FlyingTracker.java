package org.hinoob.khara.tracker.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.tracker.Tracker;
import org.hinoob.khara.util.KharaVector3d;
import org.hinoob.khara.util.Teleport;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class FlyingTracker extends Tracker {

    public FlyingTracker(KharaUser user) {
        super(user);
    }

    private final Queue<Teleport> teleports = new ArrayDeque<>();

    @Override
    public void handle(PacketReceiveEvent event) {
        if(!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) return;
        user.setLastAcceptedTeleport(null);

        WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(event);
        user.setOnGround(wrapper.isOnGround());

        if(wrapper.hasPositionChanged() && wrapper.hasRotationChanged() && !wrapper.isOnGround()){
            checkTeleport(wrapper);
        }

        if(wrapper.hasRotationChanged()){
            user.setRotation(wrapper.getLocation().getYaw(), wrapper.getLocation().getPitch());
        }

        if(wrapper.hasPositionChanged()){
            user.setPosition(wrapper.getLocation().getX(), wrapper.getLocation().getY(), wrapper.getLocation().getZ());
        }
    }

    @Override
    public void handle(PacketSendEvent event) {
        if(event.getPacketType() != PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) return;

        TransactionTracker transaction = user.trackerContainer.getByClass(TransactionTracker.class);
        WrapperPlayServerPlayerPositionAndLook wrapper = new WrapperPlayServerPlayerPositionAndLook(event);

        transaction.sendTransaction();
        int transactionID = transaction.getSendCounter();
        transaction.add(() -> {
            Teleport teleport = new Teleport(new KharaVector3d(wrapper.getX(), wrapper.getY(), wrapper.getZ()), transactionID);
            // Convert to relative if possible

            if(wrapper.isRelativeFlag(RelativeFlag.X)){
                teleport.getVector().add(user.getPosition().getX(), 0, 0);
            }

            if(wrapper.isRelativeFlag(RelativeFlag.Y)){
                teleport.getVector().add(0, user.getPosition().getY(), 0);
            }

            if(wrapper.isRelativeFlag(RelativeFlag.Z)){
                teleport.getVector().add(0, 0, user.getPosition().getY());
            }

            this.teleports.add(teleport);
        });
    }

    @Override
    public void handlePost(PacketReceiveEvent event) {

    }

    @Override
    public void handlePost(PacketSendEvent event) {

    }

    private void checkTeleport(WrapperPlayClientPlayerFlying wrapper){
        TransactionTracker transaction = user.trackerContainer.getByClass(TransactionTracker.class);
        KharaVector3d playerPos = new KharaVector3d(wrapper.getLocation().getX(), wrapper.getLocation().getY(), wrapper.getLocation().getZ());

        Iterator<Teleport> iterator = this.teleports.iterator();
        while(iterator.hasNext()){
            Teleport teleport = iterator.next();

            if(teleport.getTransactionID() == transaction.getReceiveCounter() && teleport.getVector().distance(playerPos) == 0){
                user.setLastAcceptedTeleport(teleport);
                iterator.remove();
            } else if (transaction.getReceiveCounter() > teleport.getTransactionID()) {
                // ignored the teleport, bad
                user.disconnect("Ignored teleport");
            }
        }
    }
}
