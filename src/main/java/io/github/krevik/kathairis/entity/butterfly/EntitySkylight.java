package io.github.krevik.kathairis.entity.butterfly;

import io.github.krevik.kathairis.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class EntitySkylight extends EntityMothBase
{

    public EntitySkylight(World worldIn)
    {
        super(worldIn, ModEntities.SKYLIGHT);
        this.experienceValue=1;
    }

    public EntitySkylight(EntityType<EntitySkylight> type, World world) {
        super(type, world);
    }


    @Override
    public boolean canSpawn(IWorld p_205020_1_, SpawnReason p_205020_2_) {
        return super.canSpawn(p_205020_1_, p_205020_2_);
    }

    @Override
    public int getMaxSpawnedInChunk()
    {
        return 3;
    }

    @Override
    protected void collideWithEntity(Entity entityIn)
    {
    }

    @Override
    protected void collideWithNearbyEntities()
    {
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }
}