package org.hinoob.khara.util;

import com.github.retrooper.packetevents.protocol.world.BlockFace;
import org.checkerframework.checker.units.qual.A;

public class AABB {

    public double minX, minY, minZ, maxX, maxY, maxZ;

    public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AABB(KharaVector3d min, KharaVector3d max){
        this.minX = min.getX();
        this.minY = min.getY();
        this.minZ = min.getZ();

        this.maxX = max.getX();
        this.maxY = max.getY();
        this.maxZ = max.getZ();
    }

    public AABB addCoord(double x, double y, double z)
    {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (x < 0.0D)
        {
            d0 += x;
        }
        else if (x > 0.0D)
        {
            d3 += x;
        }

        if (y < 0.0D)
        {
            d1 += y;
        }
        else if (y > 0.0D)
        {
            d4 += y;
        }

        if (z < 0.0D)
        {
            d2 += z;
        }
        else if (z > 0.0D)
        {
            d5 += z;
        }

        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB expand(double x, double y, double z){
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB expand(double value){
        return expand(value, value, value);
    }

    public AABB union(AABB other)
    {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new AABB(d0, d1, d2, d3, d4, d5);
    }

    public AABB offset(double x, double y, double z)
    {
        return new AABB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public boolean isVecInside(KharaVector3d vec)
    {
        return vec.getX() > this.minX && vec.getX() < this.maxX ? (vec.getY() > this.minY && vec.getY() < this.maxY ? vec.getZ() > this.minZ && vec.getZ() < this.maxZ : false) : false;
    }

    public double calculateXOffset(AABB other, double offsetX)
    {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ)
        {
            if (offsetX > 0.0D && other.maxX <= this.minX)
            {
                double d1 = this.minX - other.maxX;

                if (d1 < offsetX)
                {
                    offsetX = d1;
                }
            }
            else if (offsetX < 0.0D && other.minX >= this.maxX)
            {
                double d0 = this.maxX - other.minX;

                if (d0 > offsetX)
                {
                    offsetX = d0;
                }
            }

            return offsetX;
        }
        else
        {
            return offsetX;
        }
    }

    public double calculateYOffset(AABB other, double offsetY)
    {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ)
        {
            if (offsetY > 0.0D && other.maxY <= this.minY)
            {
                double d1 = this.minY - other.maxY;

                if (d1 < offsetY)
                {
                    offsetY = d1;
                }
            }
            else if (offsetY < 0.0D && other.minY >= this.maxY)
            {
                double d0 = this.maxY - other.minY;

                if (d0 > offsetY)
                {
                    offsetY = d0;
                }
            }

            return offsetY;
        }
        else
        {
            return offsetY;
        }
    }

    public double calculateZOffset(AABB other, double offsetZ)
    {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY)
        {
            if (offsetZ > 0.0D && other.maxZ <= this.minZ)
            {
                double d1 = this.minZ - other.maxZ;

                if (d1 < offsetZ)
                {
                    offsetZ = d1;
                }
            }
            else if (offsetZ < 0.0D && other.minZ >= this.maxZ)
            {
                double d0 = this.maxZ - other.minZ;

                if (d0 > offsetZ)
                {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        }
        else
        {
            return offsetZ;
        }
    }

    public Pair<KharaVector3d, BlockFace> calculateIntercept(KharaVector3d vecA, KharaVector3d vecB)
    {
        KharaVector3d vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
        KharaVector3d vec31 = vecA.getIntermediateWithXValue(vecB, this.maxX);
        KharaVector3d vec32 = vecA.getIntermediateWithYValue(vecB, this.minY);
        KharaVector3d vec33 = vecA.getIntermediateWithYValue(vecB, this.maxY);
        KharaVector3d vec34 = vecA.getIntermediateWithZValue(vecB, this.minZ);
        KharaVector3d vec35 = vecA.getIntermediateWithZValue(vecB, this.maxZ);

        if (!this.isVecInYZ(vec3))
        {
            vec3 = null;
        }

        if (!this.isVecInYZ(vec31))
        {
            vec31 = null;
        }

        if (!this.isVecInXZ(vec32))
        {
            vec32 = null;
        }

        if (!this.isVecInXZ(vec33))
        {
            vec33 = null;
        }

        if (!this.isVecInXY(vec34))
        {
            vec34 = null;
        }

        if (!this.isVecInXY(vec35))
        {
            vec35 = null;
        }

        KharaVector3d vec36 = null;

        if (vec3 != null)
        {
            vec36 = vec3;
        }

        if (vec31 != null && (vec36 == null || vecA.distanceSquared(vec31) < vecA.distanceSquared(vec36)))
        {
            vec36 = vec31;
        }

        if (vec32 != null && (vec36 == null || vecA.distanceSquared(vec32) < vecA.distanceSquared(vec36)))
        {
            vec36 = vec32;
        }

        if (vec33 != null && (vec36 == null || vecA.distanceSquared(vec33) < vecA.distanceSquared(vec36)))
        {
            vec36 = vec33;
        }

        if (vec34 != null && (vec36 == null || vecA.distanceSquared(vec34) < vecA.distanceSquared(vec36)))
        {
            vec36 = vec34;
        }

        if (vec35 != null && (vec36 == null || vecA.distanceSquared(vec35) < vecA.distanceSquared(vec36)))
        {
            vec36 = vec35;
        }

        if (vec36 == null)
        {
            return null;
        }
        else
        {
            BlockFace enumfacing;

            if (vec36 == vec3)
            {
                enumfacing = BlockFace.WEST;
            }
            else if (vec36 == vec31)
            {
                enumfacing = BlockFace.EAST;
            }
            else if (vec36 == vec32)
            {
                enumfacing = BlockFace.DOWN;
            }
            else if (vec36 == vec33)
            {
                enumfacing = BlockFace.UP;
            }
            else if (vec36 == vec34)
            {
                enumfacing = BlockFace.NORTH;
            }
            else
            {
                enumfacing = BlockFace.SOUTH;
            }

            return new Pair<>(vec36, enumfacing);
        }
    }

    private boolean isVecInYZ(KharaVector3d vec)
    {
        return vec == null ? false : vec.getY() >= this.minY && vec.getY() <= this.maxY && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ;
    }

    private boolean isVecInXZ(KharaVector3d vec)
    {
        return vec == null ? false : vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ;
    }

    private boolean isVecInXY(KharaVector3d vec)
    {
        return vec == null ? false : vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getY() >= this.minY && vec.getY() <= this.maxY;
    }

    public AABB copy() {
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }


}
