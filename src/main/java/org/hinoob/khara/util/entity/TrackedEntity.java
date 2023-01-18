package org.hinoob.khara.util.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lombok.Getter;
import org.hinoob.khara.util.AABB;
import org.hinoob.khara.util.KharaVector3d;

public class TrackedEntity {

    @Getter private int entityId;
    private EntityType type;

    public double posX, posY, posZ;
    public double serverPosX, serverPosY, serverPosZ;
    private double otherPlayerMPX, otherPlayerMPY, otherPlayerMPZ, otherPlayerMPYaw, otherPlayerMPPitch;
    private float rotationYaw, rotationPitch;

    private int interpolationSteps;
    private float width, height;
    private AABB boundingBox;

    public TrackedEntity(int entityId, EntityType type, KharaVector3d initialPos) {
        this.entityId = entityId;
        this.type = type;

        this.width = 0.6f;
        this.height = 1.8f;

        setPosition(initialPos.getX(), initialPos.getY(), initialPos.getZ());

        if(PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8)){
            this.interpolationSteps = 3;
        }
    }

    public void setPositionAndRotation2(double x, double y, double z)
    {
        this.otherPlayerMPX = x;
        this.otherPlayerMPY = y;
        this.otherPlayerMPZ = z;
        this.interpolationSteps = 3;
    }

    public void onLivingUpdate(){
        if (this.interpolationSteps > 0)
        {
            double d0 = this.posX + (this.otherPlayerMPX - this.posX) / (double)this.interpolationSteps;
            double d1 = this.posY + (this.otherPlayerMPY - this.posY) / (double)this.interpolationSteps;
            double d2 = this.posZ + (this.otherPlayerMPZ - this.posZ) / (double)this.interpolationSteps;

            --this.interpolationSteps;
            this.setPosition(d0, d1, d2);
        }
    }

    private void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.boundingBox = new AABB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f);
    }

    public AABB getBoundingBox(){
        return boundingBox;
    }
}