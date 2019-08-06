package io.github.krevik.kathairis.world.dimension.biome.gen.layers;

import io.github.krevik.kathairis.init.ModBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum GenLayerDeleteRiverNearDesert implements ICastleTransformer {
    INSTANCE;

    private static final int KATHARIAN_DESERT_ID = Registry.BIOME.getId(ModBiomes.KATHARIAN_DESERT);
    private static final int KATHARIAN_RIVER_ID = Registry.BIOME.getId(ModBiomes.KATHAIRIS_RIVER);

    private GenLayerDeleteRiverNearDesert() {
    }

    @Override
    public int apply(INoiseRandom iNoiseRandom, int biome1ID, int biome2ID, int biome3ID, int biome4ID, int biomeCenter) {
        if(biomeCenter==KATHARIAN_RIVER_ID){
            if(KatharianLayerUtil.isKatharianDesertBiome(biome1ID)||KatharianLayerUtil.isKatharianDesertBiome(biome2ID)||KatharianLayerUtil.isKatharianDesertBiome(biome3ID)||
                    KatharianLayerUtil.isKatharianDesertBiome(biome4ID)){
                return KATHARIAN_DESERT_ID;
            }
        }
        return biomeCenter;
    }
}
