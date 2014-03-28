package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class HandlerLavaPump extends MachineTieredBlockHandler {
	
	public HandlerLavaPump(Block block) {
		super(block, Names.blockHydraulicLavaPump);
	}
	
	
	@Override
	public void registerIcons(IIconRegister iconRegistry){
		itemIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return itemIcon;
	}
}
