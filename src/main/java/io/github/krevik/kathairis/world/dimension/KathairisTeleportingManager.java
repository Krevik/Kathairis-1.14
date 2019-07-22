package io.github.krevik.kathairis.world.dimension;

import io.github.krevik.kathairis.Kathairis;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Krevik
 */
public class KathairisTeleportingManager {

    public static void tele(Entity entity) {
        if (!entity.world.isRemote) {
            int type1 = Kathairis.KATH_DIM_TYPE.getId();
            DimensionType type = DimensionType.getById(type1);
            if (entity.getRidingEntity() == null && !entity.isBeingRidden()) {
                MinecraftServer mcServer = entity.getServer();
                entity.setPortal(new BlockPos(entity.posX, entity.posY, entity.posZ));
                if (entity.timeUntilPortal > 0) {
                    entity.timeUntilPortal = 10;
                } else if (entity.dimension.getId() != type1) {
                    entity.timeUntilPortal = 10;
                    entity.changeDimension(type, new TeleporterKathairis(mcServer.getWorld(type)));
                } else if (entity.dimension.getId() == type1) {
                    entity.timeUntilPortal = 10;
                    ServerWorld toWorld = mcServer.getWorld(DimensionType.OVERWORLD);
                    entity.changeDimension(DimensionType.OVERWORLD, new TeleporterKathairis(toWorld));
                }
            }
        }
    }
}