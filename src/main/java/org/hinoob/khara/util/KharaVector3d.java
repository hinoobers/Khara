package org.hinoob.khara.util;

import com.github.retrooper.packetevents.protocol.world.Location;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

public class KharaVector3d {

    public static KharaVector3d ZERO = new KharaVector3d(0, 0, 0);

    @Getter @Setter private double x, y, z;

    public KharaVector3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public KharaVector3d(Vector vector){
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public KharaVector3d(Location vector){
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public KharaVector3d add(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public KharaVector3d subtract(double x, double y, double z){
        add(-x, -y, -z);
        return this;
    }

    public KharaVector3d multiply(double x, double y, double z){
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public KharaVector3d divide(double x, double y, double z){
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public KharaVector3d addCopied(double x, double y, double z){
        return copy().add(x, y, z);
    }

    public KharaVector3d subtractCopied(double x, double y, double z){
        return copy().subtract(x, y, z);
    }

    public KharaVector3d multiplyCopied(double x, double y, double z){
        return copy().multiply(x, y, z);
    }

    public KharaVector3d divideCopied(double x, double y, double z){
        return copy().divide(x, y, z);
    }

    public KharaVector3d nullify(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }

    // **
    // End of edit functions
    // **

    public double distance(KharaVector3d o) {
        return Math.sqrt(NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z));
    }

    public double distanceSquared(KharaVector3d o) {
        return NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z);
    }

    // **
    // Start of MCP functions
    // **

    public KharaVector3d getIntermediateWithXValue(KharaVector3d vec, double x)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;

        if (d0 * d0 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (x - this.x) / d0;
            return d3 >= 0.0D && d3 <= 1.0D ? new KharaVector3d(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
        }
    }

    public KharaVector3d getIntermediateWithYValue(KharaVector3d vec, double y)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;

        if (d1 * d1 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (y - this.y) / d1;
            return d3 >= 0.0D && d3 <= 1.0D ? new KharaVector3d(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
        }
    }

    public KharaVector3d getIntermediateWithZValue(KharaVector3d vec, double z)
    {
        double d0 = vec.x - this.x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - this.z;

        if (d2 * d2 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (z - this.z) / d2;
            return d3 >= 0.0D && d3 <= 1.0D ? new KharaVector3d(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
        }
    }

    @Override
    public String toString() {
        return "KharaVector3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public KharaVector3d copy(){
        return new KharaVector3d(x, y, z);
    }
}
