package io.github.krevik.kathairis.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author Krevik
 */
public class ItemMagnethiumSword extends ItemKathairisSword {

	public ItemMagnethiumSword(IItemTier tier, ItemGroup group) {
		super(tier);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity entityLivingBase) {
		stack.damageItem(1, entityLivingBase);
		// FIXME TODO STOPSHIP: 2019-03-27 DON'T use hardcoded IDs
		target.addPotionEffect(new PotionEffect(Potion.getPotionById(25), 25, 1));
		return true;
	}

}
