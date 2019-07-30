package io.github.krevik.kathairis.entity;

import io.github.krevik.kathairis.entity.ai.EntityAIAvoidMovingSandsAndCactus;
import io.github.krevik.kathairis.init.ModBlocks;
import io.github.krevik.kathairis.init.ModEntities;
import io.github.krevik.kathairis.init.ModSounds;
import io.github.krevik.kathairis.util.KatharianLootTables;
import net.minecraft.block.Block;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityBigTurtle extends AnimalEntity
{
    private ArrayList<Block> spawnableBlocks;
    public EntityBigTurtle(World worldIn)
    {
        super(ModEntities.BIG_TURTLE,worldIn);
        this.setSize(0.9F, 1.2F);
        this.experienceValue=10;
        spawnableBlocks= new ArrayList<>();
        spawnableBlocks.add(ModBlocks.KATHAIRIS_SAND);
    }


    @Override
    public boolean canSpawn(IWorld p_205020_1_, SpawnReason sth) {
        int lvt_3_1_ = MathHelper.floor(this.posX);
        int lvt_4_1_ = MathHelper.floor(this.getBoundingBox().minY);
        int lvt_5_1_ = MathHelper.floor(this.posZ);
        BlockPos lvt_6_1_ = new BlockPos(lvt_3_1_, lvt_4_1_, lvt_5_1_);
        return spawnableBlocks.contains(p_205020_1_.getBlockState(lvt_6_1_.down()).getBlock()) && p_205020_1_.getLightSubtracted(lvt_6_1_, 0) > 8 &&
                this.getBlockPathWeight(new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ), p_205020_1_) >= 0.0F && p_205020_1_.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this);
    }


    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new EntityAIAvoidMovingSandsAndCactus(this,1.0D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this,1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this,1.0D));
        this.goalSelector.addGoal(3, new FollowParentGoal(this,1.1D));
        this.goalSelector.addGoal(4, new RandomWalkingGoal(this,1.0D));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    @Override
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1000000417232513D);
    }

    @Override
    protected ResourceLocation getLootTable()
    {
    	return KatharianLootTables.LOOT_BIGTURTLE;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return ModSounds.MOB_TURTLE_DEAD;
    }

    @Override
    public EntityBigTurtle createChild(AgeableEntity ageable)
    {
        EntityBigTurtle entitysheep1 = new EntityBigTurtle(this.world);
        return entitysheep1;
    }

}