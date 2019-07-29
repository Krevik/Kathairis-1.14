package io.github.krevik.kathairis.item;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author Cadiboo
 */
public class ItemKathairisFood extends Food {

	//TODO COMPLETE
	public ItemKathairisFood(int healAmountIn, float saturation, boolean meat, boolean alwaysEdible, boolean instantEat, List<Pair<EffectInstance, Float>> effects, ItemGroup group) {
		super(healAmountIn, saturation, meat, alwaysEdible, instantEat, effects);
	}

}
