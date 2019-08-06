package io.github.krevik.kathairis.world.dimension;

import io.github.krevik.kathairis.Kathairis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

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
                    changeDim(entity, entity.getPosition(), type);
                } else if (entity.dimension.getId() == type1) {
                    entity.timeUntilPortal = 10;
                    ServerWorld toWorld = mcServer.getWorld(DimensionType.OVERWORLD);
                    changeDim(entity, entity.getPosition(), DimensionType.OVERWORLD);
                }
            }
        }
    }

    public static void changeDim(Entity entity, BlockPos pos, DimensionType type) {
        if (!ForgeHooks.onTravelToDimension(entity, type)) return;
        DimensionType dimensiontype = entity.dimension;
        ServerWorld serverworld = entity.getServer().getWorld(dimensiontype);
        entity.dimension = type;
        ServerWorld serverworld1 = entity.getServer().getWorld(type);
        WorldInfo worldinfo = entity.world.getWorldInfo();
        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).connection.sendPacket(new SRespawnPacket(type, worldinfo.getGenerator(), ((ServerPlayerEntity) entity).interactionManager.getGameType()));
            ((ServerPlayerEntity) entity).connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
            PlayerList playerlist = entity.getServer().getPlayerList();
            playerlist.updatePermissionLevel((ServerPlayerEntity) entity);
            serverworld.removeEntity(entity, true);
            entity.revive();
            float f = entity.rotationPitch;
            float f1 = entity.rotationYaw;

            entity.setLocationAndAngles(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);
            serverworld.getProfiler().endSection();
            serverworld.getProfiler().startSection("placing");
            entity.setLocationAndAngles(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);

            serverworld.getProfiler().endSection();
            entity.setWorld(serverworld1);
            serverworld1.func_217447_b(((ServerPlayerEntity) entity));
            TeleporterKathairis teleporter = new TeleporterKathairis(serverworld1);
            teleporter.placeEntity(serverworld1, entity, f1);
            teleporter.func_222268_a(entity, f1);
            ((ServerPlayerEntity) entity).interactionManager.setWorld(serverworld1);
            ((ServerPlayerEntity) entity).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) entity).abilities));
            playerlist.sendWorldInfo(((ServerPlayerEntity) entity), serverworld1);
            playerlist.sendInventory(((ServerPlayerEntity) entity));

            for(EffectInstance effectinstance : ((LivingEntity) entity).getActivePotionEffects()) {
                ((ServerPlayerEntity) entity).connection.sendPacket(new SPlayEntityEffectPacket(entity.getEntityId(), effectinstance));
            }

            ((ServerPlayerEntity) entity).connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
            BasicEventHooks.firePlayerChangedDimensionEvent(((ServerPlayerEntity) entity), dimensiontype, type);
        }
    }
}