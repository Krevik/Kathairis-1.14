package io.github.krevik.kathairis.entity.ai;

import io.github.krevik.kathairis.entity.EntityCloudOister;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class EntityAICloudOisterPanic extends Goal
{
    protected final EntityCloudOister creature;
    protected double speed;

    public EntityAICloudOisterPanic(EntityCloudOister creature, double speedIn)
    {
        this.creature = creature;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
    }

    @Override
    public boolean shouldExecute()
    {
        return this.creature.getRevengeTarget() != null || this.creature.isBurning();
    }

    @Override
    public void startExecuting()
    {
    	
    }

    @Override
    public boolean shouldContinueExecuting()
    {
    	return creature.panic();
    }
    
    int jumpTimer=0;
    @Override
    public void tick()
    {
    	jumpTimer++;
    	if(jumpTimer>12) {
    		jumpTimer=0;
			creature.motionY+=0.5;
            creature.spawnJumpParticles(creature.getEntityWorld());
			double destPosX=creature.posX-creature.getRNG().nextInt(6)+creature.getRNG().nextInt(6);
			double destPosZ=creature.posZ-creature.getRNG().nextInt(6)+creature.getRNG().nextInt(6);
			creature.getNavigator().setPath(creature.getNavigator().getPathToPos(new BlockPos(destPosX,creature.posY,destPosZ)), 1);
			creature.motionX=(destPosX-creature.posX)*0.15;
			creature.motionZ=(destPosZ-creature.posZ)*0.15;
    	}
    }
}