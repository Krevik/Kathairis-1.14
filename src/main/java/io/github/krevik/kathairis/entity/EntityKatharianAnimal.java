package io.github.krevik.kathairis.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EntityKatharianAnimal extends AnimalEntity {

    public EntityKatharianAnimal(EntityType<? extends AnimalEntity> type, World world){
        super(type,world);
    }

    public EntityKatharianAnimal(World world){
        super(null,world);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity entityAgeable) {
        return null;
    }
}
