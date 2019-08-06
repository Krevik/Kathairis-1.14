package io.github.krevik.kathairis.world.dimension.feature;

import io.github.krevik.kathairis.world.dimension.feature.desert.*;
import io.github.krevik.kathairis.world.dimension.feature.floating_islands.FeatureHugeFloatingIsland;
import io.github.krevik.kathairis.world.dimension.feature.forest.FeatureForestCandleBush;
import io.github.krevik.kathairis.world.dimension.feature.forest.FeatureSteppedSucculent;
import io.github.krevik.kathairis.world.dimension.feature.plainfields.FeaturePlainFields;
import io.github.krevik.kathairis.world.dimension.feature.strctures.CloudMiniTemple;
import io.github.krevik.kathairis.world.dimension.feature.swamp.FeatureBasicSwamp;
import io.github.krevik.kathairis.world.dimension.feature.tree.*;
import io.github.krevik.kathairis.world.dimension.surface.builder.KathairisSwampSurfaceBuilder;
import io.github.krevik.kathairis.world.dimension.surface.builder.KatharianDesertEdgeSurfaceBuilder;
import io.github.krevik.kathairis.world.dimension.surface.builder.KatharianSoftSandLakesSurfaceBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SwampSurfaceBuilder;

public class KatharianFeatureList {

    public static final int mini_Cloud_Temple_Chance = 350;

    public static final WorldCarver<ProbabilityConfig> KATHARIAN_CAVE_WORLD_CARVER = new KatharianWorldCaveCarver();

    public static final AbstractTreeFeature<NoFeatureConfig> KATHARIAN_TREE = new KatharianTreeFeature(true);
    public static final AbstractTreeFeature<NoFeatureConfig> BASIC_STANDARD_TREE = new FeatureBasicKatharianTree(true, false);
    public static final AbstractTreeFeature<NoFeatureConfig> KATHARIAN_TREE_1 = new FeatureKatharianTallTree1();
    public static final AbstractTreeFeature<NoFeatureConfig> KATHARIAN_TREE_2 = new FeatureKatharianTallTree2();
    public static final AbstractTreeFeature<NoFeatureConfig> KATHARIAN_TREE_3 = new FeatureKatharianTallTree3();
    public static final AbstractTreeFeature<NoFeatureConfig> KATHARIAN_HUGE_TREE_1 = new FeatureKatharianTreeHuge1();
    public static final Feature<NoFeatureConfig> KATHARIAN_CLOUD = new FeatureKatharianCloud();
    public static final Feature<NoFeatureConfig> KATHARIAN_CACTUS = new FeatureKatharianCactus();
    public static final Feature<NoFeatureConfig> KATHARIAN_DEAD_BUSH = new FeatureKatharianDeadBush();
    public static final Feature<NoFeatureConfig> KATHARIAN_ROCK_MUSHROOM = new FeatureRockMushrooms();
    public static final Feature<NoFeatureConfig> KATHARIAN_PLAIN_FIELDS = new FeaturePlainFields();
    public static final Feature<NoFeatureConfig> KATHARIAN_CLOUD_MINI_ISLAND = new FeatureKatharianFloatingMiniIsland();
    public static final Feature<NoFeatureConfig> KATHARIAN_CRYSTAL_CHAMBER = new FeatureCrystalChamber();
    public static final Feature<NoFeatureConfig> KATHARIAN_ROCKTUS = new FeatureKatharianRocktus();
    public static final Feature<NoFeatureConfig> SAND_LAYERS = new FeatureDesertSandLayers();
    public static final Feature<NoFeatureConfig> STEPPED_SUCCULENT = new FeatureSteppedSucculent();
    public static final AbstractTreeFeature<NoFeatureConfig> KATHARIAN_SWAMP_TALL_TREE_1 = new FeatureKatharianSwampTallTree1();
    public static final Feature<NoFeatureConfig> BASIC_SWAMP_FEATURE = new FeatureBasicSwamp();
    public static final Feature<NoFeatureConfig> SMALL_ROCK = new FeatureDesertSmallRocks();
    public static final Feature<NoFeatureConfig> KATHARIAN_FOREST_BUSH = new FeatureKatharianForestBush();
    public static final Feature<BushConfig> KATHARIAN_FOREST_CANDLE_BUSH = new FeatureForestCandleBush();
    public static final Feature<NoFeatureConfig> KATHARIAN_HUGE_FLOATING_ISLAND = new FeatureHugeFloatingIsland();
    public static final Feature<NoFeatureConfig> CLOUD_MINI_TEMPLE = new CloudMiniTemple();
    public static final Feature<KatharianMinableConfig> ORE = registerFeature("ore", new BasicKatharianOreFeature(KatharianMinableConfig::deserialize));

    public static final SurfaceBuilder<SurfaceBuilderConfig> KATHARIAN_SWAMP_SURFACE_BUILDER = registerSurfaceBuilder("swamp", new KathairisSwampSurfaceBuilder(SurfaceBuilderConfig::deserialize));
    public static final SurfaceBuilder<SurfaceBuilderConfig> KATHARIAN_DESERT_EDGE_SURFACE_BUILDER = registerSurfaceBuilder("desert_edge", new KatharianDesertEdgeSurfaceBuilder(SurfaceBuilderConfig::deserialize));
    public static final SurfaceBuilder<SurfaceBuilderConfig> KATHARIAN_SOFT_SAND_LAKES_SURFACE_BUILDER = registerSurfaceBuilder("soft_sand_lakes", new KatharianSoftSandLakesSurfaceBuilder(SurfaceBuilderConfig::deserialize));


    private static <C extends IFeatureConfig, F extends Feature<C>> F registerFeature(String key, F value) {
        return (F)(Registry.<Feature<?>>register(Registry.FEATURE, "kathairis: " + key, value));
    }

    private static <C extends ISurfaceBuilderConfig, F extends SurfaceBuilder<C>> F registerSurfaceBuilder(String key, F builderIn) {
        return (F)(Registry.<SurfaceBuilder<?>>register(Registry.SURFACE_BUILDER, "kathairis: " + key, builderIn));
    }

}
