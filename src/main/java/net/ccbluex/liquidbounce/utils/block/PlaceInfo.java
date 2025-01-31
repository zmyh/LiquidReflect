package net.ccbluex.liquidbounce.utils.block;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @author CCBlueX
 * @game Minecraft
 */
public class PlaceInfo {
    private final BlockPos blockPos;
    private final EnumFacing enumFacing;
    private Vec3 vec3;

    public PlaceInfo(BlockPos blockPos, EnumFacing enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public EnumFacing getEnumFacing() {
        return enumFacing;
    }

    public Vec3 getVec3() {
        return vec3;
    }

    public void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public static PlaceInfo get(BlockPos blockPos) {
        if (BlockUtils.canBeClicked(blockPos.add(0, -1, 0))) {
            return new PlaceInfo(blockPos.add(0, -1, 0), EnumFacing.UP);
        } else if (BlockUtils.canBeClicked(blockPos.add(0, 0, 1))) {
            return new PlaceInfo(blockPos.add(0, 0, 1), EnumFacing.NORTH);
        } else if (BlockUtils.canBeClicked(blockPos.add(-1, 0, 0))) {
            return new PlaceInfo(blockPos.add(-1, 0, 0), EnumFacing.EAST);
        } else if (BlockUtils.canBeClicked(blockPos.add(0, 0, -1))) {
            return new PlaceInfo(blockPos.add(0, 0, -1), EnumFacing.SOUTH);
        } else if (BlockUtils.canBeClicked(blockPos.add(1, 0, 0))) {
            return new PlaceInfo(blockPos.add(1, 0, 0), EnumFacing.WEST);
        } else {
            return null;
        }
    }
}
