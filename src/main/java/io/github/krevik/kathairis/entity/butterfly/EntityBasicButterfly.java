package io.github.krevik.kathairis.entity.butterfly;

import io.github.krevik.kathairis.block.BlockKathairisPlant;
import io.github.krevik.kathairis.init.ModEntities;
import io.github.krevik.kathairis.util.KatharianLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;

public class EntityBasicButterfly extends AmbientEntity
{
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityBasicButterfly.class, DataSerializers.VARINT);

    private static final DataParameter<Byte> SITTING = EntityDataManager.createKey(EntityBasicButterfly.class, DataSerializers.BYTE);
    private BlockPos spawnPosition;


    public EntityBasicButterfly(World worldIn)
    {
        super(ModEntities.COMMON_BUTTERFLY1, worldIn);
        this.setIsBirdSitting(true);
        this.experienceValue=1;
    }

    public EntityBasicButterfly(World worldIn, EntityType<? extends EntityBasicButterfly> commonButterfly1) {
        super(commonButterfly1, worldIn);
    }

    public EntityBasicButterfly(EntityType<? extends EntityBasicButterfly> type, World world) {
        super(type,world);
    }

    @Override
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
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

    public boolean getIsBirdSitting()
    {
        return (this.getDataManager().get(SITTING).byteValue() & 1) != 0;
    }

    public void setIsBirdSitting(boolean isHanging)
    {
        byte b0 = this.getDataManager().get(SITTING).byteValue();

        if (isHanging)
        {
            this.getDataManager().set(SITTING, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.getDataManager().set(SITTING, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.getIsBirdSitting())
        {
            setMotion(new Vec3d(0,0,0));
            this.posY=this.world.getHeight(Heightmap.Type.MOTION_BLOCKING,this.getPosition()).getY();
            if(this.rand.nextInt(1000)==0) {
                this.setIsBirdSitting(false);
            }
        }
        else
        {
            setMotionMultiplier(Blocks.AIR.getDefaultState(),new Vec3d(1,0.6000000238418579D,1));
        }
    }

    @Override
    protected void updateAITasks()
    {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.down();

        if (this.getIsBirdSitting())
        {
            if (this.world.getBlockState(blockpos1).isNormalCube(world,blockpos1))
            {

                if (this.world.getClosestPlayer(this, 4.0D) != null)
                {
                    if(!this.world.getClosestPlayer(this, 4.0D).isSneaking()) {
                        this.setIsBirdSitting(false);
                        this.world.playEvent(null, 1025, blockpos, 0);
                    }
                }
            }
            else
            {
                this.setIsBirdSitting(false);
                this.world.playEvent(null, 1025, blockpos, 0);
            }
        }
        else
        {
            if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
            {
                this.spawnPosition = null;
            }

            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double)((int)this.posX), (double)((int)this.posY), (double)((int)this.posZ),true) < 4.0D)
            {
                this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double d0 = (double)this.spawnPosition.getX() + 0.5D - this.posX;
            double d1 = (double)this.spawnPosition.getY() + 0.1D - this.posY;
            double d2 = (double)this.spawnPosition.getZ() + 0.5D - this.posZ;
            double mX = getMotion().getX() + (Math.signum(d0) * 0.5D - this.getMotion().getX()) * 0.10000000149011612D;
            double mY = getMotion().getY() + (Math.signum(d1) * 0.699999988079071D - this.getMotion().getY()) * 0.10000000149011612D;
            double mZ = getMotion().getZ() + (Math.signum(d2) * 0.5D - this.getMotion().getZ()) * 0.10000000149011612D;
            setMotion(new Vec3d(mX,mY,mZ));
            float f = (float)(MathHelper.atan2(this.getMotion().getZ(), this.getMotion().getX()) * (180D / Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += f1;

            if (this.rand.nextInt(100) == 0 && (this.world.getBlockState(blockpos1).isNormalCube(world,blockpos1)||this.world.getBlockState(blockpos1).getBlock()instanceof BlockKathairisPlant))
            {
                this.setIsBirdSitting(true);
            }
        }
    }


    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            if (!this.world.isRemote && this.getIsBirdSitting())
            {
                this.setIsBirdSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }


    public int getVariant()
    {
        return MathHelper.clamp(this.getDataManager().get(VARIANT).intValue(), 0, ButterflyType.META_LOOKUP.length);
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.getDataManager().register(VARIANT, Integer.valueOf(0));
        this.getDataManager().register(SITTING, Byte.valueOf((byte)1));
    }


    public void setVariant(int p_191997_1_)
    {
        this.getDataManager().set(VARIANT, Integer.valueOf(p_191997_1_));
    }

    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        this.getDataManager().set(SITTING, Byte.valueOf(compound.getByte("BatFlags")));
        this.setVariant(compound.getInt("Variant"));

    }

    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putByte("BatFlags", this.getDataManager().get(SITTING).byteValue());
        compound.putInt("Variant", this.getVariant());

    }
    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return KatharianLootTables.LOOT_BUTTERFLY;
    }

    public enum ButterflyType implements IStringSerializable
    {
        BASIC1(0,"basic1"),
        BASIC2(1,"basic2"),
        CLOUDSHIMMER(2,"cloud_shimmer"),
        ILLUKINI(3,"illukini"),
        RUBYSILE(4,"ruby_sile"),
        COMMONMOTH(5,"common_moth");

        private static final EntityBasicButterfly.ButterflyType[] META_LOOKUP = new EntityBasicButterfly.ButterflyType[values().length];
        private final int meta;
        private final String name;

        ButterflyType(int p_i46384_3_, String Name)
        {
            this.meta = p_i46384_3_;
            name=Name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public static EntityBasicButterfly.ButterflyType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        static
        {
            for (EntityBasicButterfly.ButterflyType blockstone$enumtype : values())
            {
                META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
            }
        }

        @Override
        public String getName() {
            return name;
        }
    }
}