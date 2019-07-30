package io.github.krevik.kathairis.entity;

import io.github.krevik.kathairis.init.ModBlocks;
import io.github.krevik.kathairis.init.ModEntities;
import io.github.krevik.kathairis.util.KatharianLootTables;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EntityCactiSpore extends CreatureEntity
{
    private static final DataParameter<Boolean> canDespawn = EntityDataManager.createKey(EntityCactiSpore.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> spikeTimer = EntityDataManager.createKey(EntityCactiSpore.class, DataSerializers.VARINT);
    private ArrayList<Block> spawnableBlocks;

    public EntityCactiSpore(World worldIn)
    {
        super(ModEntities.CACTI_SPORE,worldIn);
        this.experienceValue=30;
        spawnableBlocks= new ArrayList<>();
        spawnableBlocks.add(ModBlocks.KATHAIRIS_GRASS);
    }

    public EntityCactiSpore(EntityType<? extends LivingEntity> type, World world) {
        super((EntityType<? extends AnimalEntity>) type, world);
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnReason spawnReason) {
        int lvt_3_1_ = MathHelper.floor(this.posX);
        int lvt_4_1_ = MathHelper.floor(this.getBoundingBox().minY);
        int lvt_5_1_ = MathHelper.floor(this.posZ);
        BlockPos lvt_6_1_ = new BlockPos(lvt_3_1_, lvt_4_1_, lvt_5_1_);
        return spawnableBlocks.contains(world.getBlockState(lvt_6_1_.down()).getBlock()) &&
                this.getBlockPathWeight(new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ), world) >= 0.0F && world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this.world,getPosition(),ModEntities.CACTI_SPORE);
    }

    public void deallowDespawning(){
        getDataManager().set(canDespawn,false);
    }

    @Override
    protected void registerGoals() {
            goalSelector.addGoal(2, new MeleeAttackGoal(this,1.0D,true));
            goalSelector.addGoal(2, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class,true));
    }

    public int getSpikeTimer()
    {
        return getDataManager().get(spikeTimer);
    }

    public void setSpikeTimer(int meta) {
        getDataManager().set(spikeTimer, meta);
    }

    @Override
    public boolean canDespawn(double p_213397_1_) {
        return getDataManager().get(canDespawn).booleanValue();
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
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.getDataManager().register(canDespawn, Boolean.valueOf(true));
        this.getDataManager().register(spikeTimer, 0);
    }

    @Override
    public void tick() {
        setMotion(new Vec3d(0,-1,0));
        this.posX=this.lastTickPosX;
        this.posZ=this.lastTickPosZ;
        this.rotationPitch=0;
        this.rotationYaw=0;
        this.rotationYawHead=0;
        super.tick();
        if (getAttackTarget() != null) {
            if (getDistance(getAttackTarget()) < 2.5f) {
                setSpikeTimer(MathHelper.clamp(getSpikeTimer() + 10, 0, 100));
            }
        }
            setSpikeTimer(MathHelper.clamp(getSpikeTimer() - 1, 0, 100));
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        this.setLastAttackedEntity(entityIn);
        if(entityIn instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) entityIn;
            if(getRNG().nextInt(4)==0) {
                target.addPotionEffect(new EffectInstance(Effects.POISON, 100, 3));
            }
        }
        return super.attackEntityAsMob(entityIn);
    }

    @Override
    public CreatureAttribute getCreatureAttribute()
    {
        return CreatureAttribute.UNDEFINED;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return KatharianLootTables.LOOT_CACTISPORE;
    }

    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("canDespawn",this.getDataManager().get(canDespawn));
    }

    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        getDataManager().set(canDespawn,compound.getBoolean("canDespawn"));
    }
}