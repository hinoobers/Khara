package org.hinoob.khara.util.entity;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.K;
import org.hinoob.khara.util.AABB;
import org.hinoob.khara.util.KharaVector3d;

public class TrackedEntity {

    @Getter private final EntityType type;
    @Getter private final int entityID;

    private float width, height;
    private int interpolationSteps;
    @Setter @Getter private KharaVector3d serverPos, otherPlayerPos, pos;
    private AABB boundingBox;

    public TrackedEntity(int entityID, EntityType type, KharaVector3d initialPos){
        this.entityID = entityID;
        this.type = type;

        this.width = EntityUtils.getWidth(type);
        this.height = EntityUtils.getHeight(type);
        setPosition(initialPos.getX(), initialPos.getY(), initialPos.getZ());
    }

    public void setPositionAndRotation2(double x, double y, double z) {
        this.otherPlayerPos = new KharaVector3d(x, y, z);
        this.interpolationSteps = 3;
    }

    public void onTick(){
        if(this.interpolationSteps > 0){
            double d0 = this.pos.getX() + (this.otherPlayerPos.getX() - this.pos.getX()) / (double) this.interpolationSteps;
            double d1 = this.pos.getY() + (this.otherPlayerPos.getY() - this.pos.getY()) / (double) this.interpolationSteps;
            double d2 = this.pos.getZ() + (this.otherPlayerPos.getZ() - this.pos.getZ()) / (double) this.interpolationSteps;

            --this.interpolationSteps;
            setPosition(d0, d1, d2);
        }
    }

    public void setPosition(double x, double y, double z) {
        this.pos = new KharaVector3d(x, y, z);
        float f = this.width / 2.0F;
        this.boundingBox = new AABB(x - f, y, z - f, x + f, y + this.height, z + f);
    }

    public AABB getBBCloned(){
        return boundingBox.copy();
    }
}
