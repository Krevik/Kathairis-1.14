package io.github.krevik.kathairis.item;

import net.minecraft.block.Blocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EnumActionResult;

/**
 * @author Krevik
 */
public class ItemPotWithLivingFlower extends Item {

	public ItemPotWithLivingFlower(ItemGroup group) {
		super(new Item.Properties().group(group));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		//TODO REMEMBER ABOUT THIS ITEM USAGE!
		if (!context.getWorld().isRemote) {
			//EntityLivingFlower elv = new EntityLivingFlower(worldIn);
			//elv.setPosition(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
			//elv.deallowDespawning();
			//worldIn.spawnEntity(elv);
			ItemStack itemstack = context.getItem();
			itemstack.shrink(1);
			context.getPlayer().addItemStackToInventory(new ItemStack(Blocks.FLOWER_POT, 1));
		}
		return ActionResultType.SUCCESS;
	}

}
