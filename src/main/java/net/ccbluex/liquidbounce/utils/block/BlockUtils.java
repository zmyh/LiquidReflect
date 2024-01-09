package net.ccbluex.liquidbounce.utils.block;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @author CCBlueX
 * @game Minecraft
 */
public class BlockUtils extends MinecraftInstance {

    /**
     * Get block from [blockPos]
     */
    public static Block getBlock(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    /**
     * Get material from [blockPos]
     */
    public static Material getMaterial(BlockPos blockPos) {
        return getBlock(blockPos).getMaterial();
    }

    /**
     * Check [blockPos] is replaceable
     */
    public static boolean isReplaceable(BlockPos blockPos) {
        return getMaterial(blockPos).isReplaceable();
    }

    /**
     * Get state from [blockPos]
     */
    public static IBlockState getState(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos);
    }

    /**
     * Check if [blockPos] is clickable
     */
    public static boolean canBeClicked(BlockPos blockPos) {
        Block block = getBlock(blockPos);
        return block != null && block.canCollideCheck(getState(blockPos), false) && mc.theWorld.getWorldBorder().contains(blockPos);
    }

    /**
     * Get block name by [id]
     */
    public static String getBlockName(int id) {
        return Block.getBlockById(id).getLocalizedName();
    }

    /**
     * Check if block is full block
     */
    public static boolean isFullBlock(BlockPos blockPos) {
        Block block = getBlock(blockPos);
        AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos));
        if (axisAlignedBB == null) return false;
        return axisAlignedBB.maxX - axisAlignedBB.minX == 1.0 && axisAlignedBB.maxY - axisAlignedBB.minY == 1.0 && axisAlignedBB.maxZ - axisAlignedBB.minZ == 1.0;
    }

    /**
     * Get distance to center of [blockPos]
     */
    public static double getCenterDistance(BlockPos blockPos) {
        return mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    /**
     * Search blocks around the player in a specific [radius]
     */
    public static Map<BlockPos, Block> searchBlocks(int radius) {
        Map<BlockPos, Block> blocks = new HashMap<>();

        for (int x = radius; x >= -radius + 1; x--) {
            for (int y = radius; y >= -radius + 1; y--) {
                for (int z = radius; z >= -radius + 1; z--) {
                    BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                    Block block = getBlock(blockPos);
                    if (block != null) {
                        blocks.put(blockPos, block);
                    }
                }
            }
        }

        return blocks;
    }

    /**
     * Check if [axisAlignedBB] has collidable blocks using custom [collide] check
     */
    public static boolean collideBlock(AxisAlignedBB axisAlignedBB, Collidable collide) {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                BlockPos blockPos = new BlockPos(x, axisAlignedBB.minY, z);
                Block block = getBlock(blockPos);
                if (!collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if [axisAlignedBB] has collidable blocks using custom [collide] check
     */
    public static boolean collideBlockIntersects(AxisAlignedBB axisAlignedBB, Collidable collide) {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                BlockPos blockPos = new BlockPos(x, axisAlignedBB.minY, z);
                Block block = getBlock(blockPos);
                if (collide.collideBlock(block)) {
                    AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos));
                    if (boundingBox != null && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public interface Collidable {

        /**
         * Check if [block] is collidable
         */
        boolean collideBlock(Block block);
    }
}
