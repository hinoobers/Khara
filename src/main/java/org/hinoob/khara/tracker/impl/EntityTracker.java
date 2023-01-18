package org.hinoob.khara.tracker.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.tracker.Tracker;
import org.hinoob.khara.util.KharaVector3d;
import org.hinoob.khara.util.entity.TrackedEntity;

import java.util.HashMap;
import java.util.Map;

public class EntityTracker extends Tracker {

    public EntityTracker(KharaUser user) {
        super(user);
    }

    public final Map<Integer, TrackedEntity> trackedEntities = new HashMap<>();

    @Override
    public void handle(PacketReceiveEvent event) {

    }

    @Override
    public void handle(PacketSendEvent event) {
        if(event.getPacketType() == PacketType.Play.Server.SPAWN_PLAYER) {
            WrapperPlayServerSpawnPlayer wrapper = new WrapperPlayServerSpawnPlayer(event);

            trackedEntities.put(wrapper.getEntityId(), new TrackedEntity(wrapper.getEntityId(), EntityTypes.PLAYER, new KharaVector3d(wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ())));
        }else if(event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE) {
            WrapperPlayServerEntityRelativeMove wrapper = new WrapperPlayServerEntityRelativeMove(event);

            handleMovement(wrapper.getEntityId(), new KharaVector3d(wrapper.getDeltaX(), wrapper.getDeltaY(), wrapper.getDeltaZ()), true);
        }else if(event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            WrapperPlayServerEntityRelativeMoveAndRotation wrapper = new WrapperPlayServerEntityRelativeMoveAndRotation(event);

            handleMovement(wrapper.getEntityId(), new KharaVector3d(wrapper.getDeltaX(), wrapper.getDeltaY(), wrapper.getDeltaZ()), true);
        }else if(event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT){
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event);

            handleMovement(wrapper.getEntityId(), new KharaVector3d(wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ()), false);
        }else if(event.getPacketType() == PacketType.Play.Server.DESTROY_ENTITIES){
            WrapperPlayServerDestroyEntities wrapper = new WrapperPlayServerDestroyEntities(event);

            for(int eid : wrapper.getEntityIds()){
                trackedEntities.remove(eid);
            }
        }
    }

    @Override
    public void handlePost(PacketReceiveEvent event) {
        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())){
            trackedEntities.values().forEach(TrackedEntity::onLivingUpdate);
        }
    }

    @Override
    public void handlePost(PacketSendEvent event) {

    }

    public void handleMovement(int entityId, KharaVector3d pos, boolean isRelative){
        TrackedEntity entity = getByID(entityId);
        if(entity == null) return;

        TransactionTracker tracker = user.trackerContainer.getByClass(TransactionTracker.class);

        if(isRelative) {
            tracker.add(() -> {
                entity.serverPosX += pos.getX();
                entity.serverPosY += pos.getY();
                entity.serverPosZ += pos.getZ();
                entity.setPositionAndRotation2(entity.serverPosX, entity.serverPosY, entity.serverPosZ);
            });
        } else {
            tracker.add(() -> {
                entity.serverPosX = pos.getX();
                entity.serverPosY = pos.getY();
                entity.serverPosZ = pos.getZ();

                double d0 = entity.serverPosX;
                double d1 = entity.serverPosY;
                double d2 = entity.serverPosZ;

                if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D)
                {
                    entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ);
                }
                else
                {
                    entity.setPositionAndRotation2(d0, d1, d2);
                }
            });

        }
    }

    public TrackedEntity getByID(int entityID){
        return trackedEntities.getOrDefault(entityID, null);
    }
}
