package io.github.krevik.kathairis.entity.butterfly;

import io.github.krevik.kathairis.block.BlockButterflyFlower;
import io.github.krevik.kathairis.block.BlockKathairisPlant;
import io.github.krevik.kathairis.init.ModBlocks;
import io.github.krevik.kathairis.init.ModEntities;
import io.github.krevik.kathairis.util.KatharianLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;

public class EntityButterfly1 extends EntityBasicButterfly
{

    private BlockPos spawnPosition;
    public BlockPos butterflyFlowerPos=null;

    public EntityButterfly1(World worldIn)
    {
        super(worldIn, ModEntities.COMMON_BUTTERFLY2);
        this.setIsBirdSitting(true);
        this.experienceValue=1;
    }

    public EntityButterfly1(EntityType<EntityButterfly1> entityType, World world) {
        super(world);
    }


    @Override
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

    @Override
    protected void registerData()
    {
        super.registerData();
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

    int ticker=0;
    @Override
    protected void updateAITasks()
    {
        ticker++;
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.down();
        if(butterflyFlowerPos!=null) {
            if(this.getIsBirdSitting()) {
                if(this.getPosition()!=butterflyFlowerPos.up()) {
                    this.setIsBirdSitting(false);
                }
            }
        }
        if(butterflyFlowerPos!=null) {
            if(this.world.getBlockState(butterflyFlowerPos)== ModBlocks.BUTTERFLY_FLOWER.getDefaultState().with(BlockButterflyFlower.VARIANT, BlockButterflyFlower.EnumType.WITH)) {
                this.getMoveHelper().setMoveTo(butterflyFlowerPos.getX()+rand.nextFloat(), butterflyFlowerPos.getY()+5, butterflyFlowerPos.getZ()+rand.nextFloat(), 0.5);
                setMotionMultiplier(Blocks.AIR.getDefaultState(),new Vec3d(1,1.6,1));
                if(this.getPosition()==butterflyFlowerPos.up()) {
                    this.setIsBirdSitting(true);
                }
            }else {
                butterflyFlowerPos=null;
                this.setIsBirdSitting(false);
            }

        }else {
            if (this.getIsBirdSitting())
            {
                if (this.world.getBlockState(blockpos1).isNormalCube(world,blockpos1)||this.world.getBlockState(blockpos1)==ModBlocks.BUTTERFLY_FLOWER.getDefaultState().with(BlockButterflyFlower.VARIANT, BlockButterflyFlower.EnumType.WITH))
                {

                    if (this.world.getClosestPlayer(this, 4.0D) != null)
                    {
                        this.setIsBirdSitting(false);
                        this.world.playEvent(null, 1025, blockpos, 0);
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
                double mX = getMotion().getX() +(Math.signum(d0) * 0.5D - this.getMotion().getX()) * 0.10000000149011612D;
                double mY = getMotion().getY() + (Math.signum(d1) * 0.699999988079071D - this.getMotion().getY()) * 0.10000000149011612D;
                double mZ = getMotion().getZ() + (Math.signum(d2) * 0.5D - this.getMotion().getZ()) * 0.10000000149011612D;
                setMotion(new Vec3d(mX,mY,mZ));
                float f = (float)(MathHelper.atan2(this.getMotion().getZ(), this.getMotion().getX()) * (180D / Math.PI)) - 90.0F;
                float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
                this.moveForward = 0.5F;
                this.rotationYaw += f1;

                if (this.rand.nextInt(100) == 0 && (this.world.getBlockState(blockpos1).isNormalCube(world,blockpos1)||this.world.getBlockState(blockpos1).getBlock()instanceof BlockKathairisPlant)||this.world.getBlockState(blockpos1)==ModBlocks.BUTTERFLY_FLOWER.getDefaultState().with(BlockButterflyFlower.VARIANT, BlockButterflyFlower.EnumType.WITH))
                {
                    this.setIsBirdSitting(true);
                }

                if(ticker>=30){
                    PlayerEntity ep = world.getClosestPlayer(this,15D);
                    if(ep!=null){
                        d0 = ep.posX + getRNG().nextFloat()-getRNG().nextFloat() + 0.5D - this.posX;
                        d1 = ep.posY + getRNG().nextFloat() + 0.1D - this.posY;
                        d2 = ep.posZ + getRNG().nextFloat()-getRNG().nextFloat() + 0.5D - this.posZ;

                        double mX1 = getMotion().getX() + (Math.signum(d0) * 0.5D - this.getMotion().getX()) * 0.10000000149011612D;
                        double mY1 = getMotion().getY() + (Math.signum(d1) * 0.699999988079071D - this.getMotion().getZ()) * 0.10000000149011612D;
                        double mZ1 = getMotion().getZ() + (Math.signum(d2) * 0.5D - this.getMotion().getZ()) * 0.10000000149011612D;
                        setMotion(new Vec3d(mX1,mY1,mZ1));
                        f = (float)(MathHelper.atan2(this.getMotion().getZ(), this.getMotion().getX()) * (180D / Math.PI)) - 90.0F;
                        f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
                        this.moveForward = 0.5F;
                        this.rotationYaw += f1;
                    }
                }

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

    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return KatharianLootTables.LOOT_BUTTERFLY;
    }
}