package org.hinoob.khara.check.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.bukkit.util.Vector;
import org.hinoob.khara.check.Check;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.tracker.impl.EntityTracker;
import org.hinoob.khara.util.AABB;
import org.hinoob.khara.util.KharaVector3d;
import org.hinoob.khara.util.MathHelper;
import org.hinoob.khara.util.Pair;
import org.hinoob.khara.util.entity.TrackedEntity;

public class Hitbox extends Check {
    public Hitbox(KharaUser user) {
        super(user);
    }

    private TrackedEntity trackedEntity;

    @Override
    public void handle(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY){
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);

            EntityTracker tracker = user.trackerContainer.getByClass(EntityTracker.class);
            if(tracker.getByID(wrapper.getEntityId()) != null){
                trackedEntity = tracker.getByID(wrapper.getEntityId());
            }
        }else if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            if(trackedEntity != null){
                double closestDistance = Double.MAX_VALUE;

                for(float eyeHeight : new float[] {1.62F, 1.54F}) {
                    for(float yaw : new float[] {user.getRotation().getYaw(), user.getLastRotation().getYaw()}) {
                        double distance = getDistance(eyeHeight, yaw);
                        if(distance > 0 && distance < closestDistance){
                            closestDistance = distance;
                        }
                    }
                }

                if(closestDistance == Double.MAX_VALUE){
                    user.sendMessage("invalid hitbox");
                } else {
                    user.sendMessage(closestDistance + "");
                }


                trackedEntity = null;
            }
        }
    }

    @Override
    public void handle(PacketSendEvent event) {

    }

    public double getDistance(float eyeHeight, float yaw) {
        AABB bb = trackedEntity.getBBCloned().expand(0.1F);

        KharaVector3d eyePos = new KharaVector3d(user.getLastPosition().getX(), user.getLastPosition().getY() + eyeHeight, user.getLastPosition().getZ());
        KharaVector3d lookPos = getVectorForRotation(user.getRotation().getPitch(), yaw);
        KharaVector3d scaledEyeDir = eyePos.add(lookPos.getX() * 6, lookPos.getY() * 6, lookPos.getZ() * 6);

        Pair<KharaVector3d, BlockFace> intercept = bb.calculateIntercept(eyePos, scaledEyeDir);
        if(intercept == null) return -1;
        return intercept.getX().distance(eyePos);
    }

    protected final KharaVector3d getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new KharaVector3d((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
}
