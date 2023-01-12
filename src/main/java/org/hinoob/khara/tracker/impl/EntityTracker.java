package org.hinoob.khara.tracker.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
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

    private final Map<Integer, TrackedEntity> trackedEntity = new HashMap<>();

    @Override
    public void handle(PacketReceiveEvent event) {

    }

    @Override
    public void handle(PacketSendEvent event) {
        if(event.getPacketType() == PacketType.Play.Server.SPAWN_PLAYER) {
            WrapperPlayServerSpawnPlayer wrapper = new WrapperPlayServerSpawnPlayer(event);

            trackedEntity.put(wrapper.getEntityId(), new TrackedEntity(wrapper.getEntityId(), EntityTypes.PLAYER, new KharaVector3d(wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ())));
        }else if(event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE) {
            WrapperPlayServerEntityRelativeMove wrapper = new WrapperPlayServerEntityRelativeMove(event);
            if(!trackedEntity.containsKey(wrapper.getEntityId())) return;

            handleMovement(wrapper.getEntityId(), new KharaVector3d(wrapper.getDeltaX(), wrapper.getDeltaY(), wrapper.getDeltaZ()), true);
        }else if(event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            WrapperPlayServerEntityRelativeMoveAndRotation wrapper = new WrapperPlayServerEntityRelativeMoveAndRotation(event);
            if(!trackedEntity.containsKey(wrapper.getEntityId())) return;

            handleMovement(wrapper.getEntityId(), new KharaVector3d(wrapper.getDeltaX(), wrapper.getDeltaY(), wrapper.getDeltaZ()), true);
        }else if(event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT){
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event);
            if(!trackedEntity.containsKey(wrapper.getEntityId())) return;

            handleMovement(wrapper.getEntityId(), new KharaVector3d(wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ()), false);
        }else if(event.getPacketType() == PacketType.Play.Server.DESTROY_ENTITIES){
            WrapperPlayServerDestroyEntities wrapper = new WrapperPlayServerDestroyEntities(event);

            for(int eid : wrapper.getEntityIds()){
                trackedEntity.remove(eid);
            }
        }
    }

    @Override
    public void handlePost(PacketReceiveEvent event) {
        trackedEntity.values().forEach(TrackedEntity::onTick);
    }

    @Override
    public void handlePost(PacketSendEvent event) {

    }

    public void handleMovement(int entityId, KharaVector3d pos, boolean isRelative){
        TrackedEntity entity = getByID(entityId);
        if(entity == null) return;

        if(isRelative) {
            entity.getServerPos().add(pos.getX(), pos.getY(), pos.getZ());
            double d0 = entity.getServerPos().getX();
            double d1 = entity.getServerPos().getY();
            double d2 = entity.getServerPos().getZ();

            entity.setPositionAndRotation2(d0, d1, d2);
        } else {
            entity.setServerPos(pos);
            double d0 = entity.getServerPos().getX();
            double d1 = entity.getServerPos().getX();
            double d2 = entity.getServerPos().getX();

            if (Math.abs(entity.getPos().getX() - d0) < 0.03125D && Math.abs(entity.getPos().getY() - d1) < 0.015625D && Math.abs(entity.getPos().getZ() - d2) < 0.03125D)
            {
                entity.setPositionAndRotation2(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());
            }
            else
            {
                entity.setPositionAndRotation2(d0, d1, d2);
            }
        }
    }

    public TrackedEntity getByID(int entityID){
        return trackedEntity.getOrDefault(entityID, null);
    }
}
