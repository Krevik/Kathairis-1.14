package io.github.krevik.kathairis.world.dimension;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

import java.util.function.BiFunction;

/**
 * @author Krevik, Cadiboo
 */
public class ModDimensionKathairis extends ModDimension {

	// Registry name in constructor is ew but apparently necessary.
	public ModDimensionKathairis(final ResourceLocation registryName) {
		this.setRegistryName(registryName);

	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
			return DimensionKathairis::new;
	}

}
