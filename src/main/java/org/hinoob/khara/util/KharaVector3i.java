package org.hinoob.khara.util;

import com.github.retrooper.packetevents.protocol.world.Location;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

public class KharaVector3i {

    public static KharaVector3i ZERO = new KharaVector3i(0, 0, 0);

    @Getter @Setter private int x, y, z;

    public KharaVector3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public KharaVector3i add(int x, int y, int z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public KharaVector3i subtract(int x, int y, int z){
        add(-x, -y, -z);
        return this;
    }

    public KharaVector3i multiply(int x, int y, int z){
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public KharaVector3i divide(int x, int y, int z){
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public KharaVector3i addCopied(int x, int y, int z){
        return copy().add(x, y, z);
    }

    public KharaVector3i subtractCopied(int x, int y, int z){
        return copy().subtract(x, y, z);
    }

    public KharaVector3i multiplyCopied(int x, int y, int z){
        return copy().multiply(x, y, z);
    }

    public KharaVector3i divideCopied(int x, int y, int z){
        return copy().divide(x, y, z);
    }

    public KharaVector3i nullify(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }

    // **
    // End of edit functions
    // **


    @Override
    public String toString() {
        return "KharaVector3i{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public KharaVector3i copy(){
        return new KharaVector3i(x, y, z);
    }
}
