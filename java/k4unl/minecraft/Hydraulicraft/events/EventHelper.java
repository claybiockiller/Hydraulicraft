package k4unl.minecraft.Hydraulicraft.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.items.ItemHydraulicDrill;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class EventHelper {
    private static boolean   hasShownUpdateInfo = false;
    private static ItemStack itemDust           = null;


    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHelper());
    }

    public static void postinit() {
        if (OreDictionary.doesOreNameExist("dustStone")) {
            itemDust = OreDictionary.getOres("dustStone").get(0).copy();
            itemDust.stackSize = 1;
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BreakEvent event) {
        if (event.block == HCBlocks.hydraulicPressureWall || event.block == HCBlocks.blockValve) {
            //check all directions for a hydraulic washer
            boolean breakAll = false;
            for (int horiz = -2; horiz <= 2; horiz++) {
                for (int vert = -2; vert <= 2; vert++) {
                    for (int depth = -2; depth <= 2; depth++) {
                        int x = event.x + horiz;
                        int y = event.y + vert;
                        int z = event.z + depth;
                        Block block = event.world.getBlock(x, y, z);
                        if (block instanceof BlockHydraulicWasher) {
                            TileHydraulicWasher washer = (TileHydraulicWasher) event.world.getTileEntity(x, y, z);
                            washer.invalidateMultiblock();
                            breakAll = true;
                            break;
                        }
                        //Log.info("Checking " + (event.x + horiz) + "," +(event.y + vert) + "," + (event.z + depth) + " = " + blockId);
                    }
                    if (breakAll) {
                        break;
                    }
                }
                if (breakAll) {
                    break;
                }
            }
        } else {
            Location vLocation = Hydraulicraft.tankList.isLocationInTank(event.x, event.y, event.z);
            if (vLocation != null) {
                ((TileInterfaceValve) vLocation.getTE(event.world)).breakTank();
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreakDrill(BlockEvent.HarvestDropsEvent event) {
        if (event.harvester == null)
            return;

        ItemStack heldItem = event.harvester.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemHydraulicDrill))
            return;

        ArrayList<ItemStack> drops = event.drops;
        ArrayList<ItemStack> newDrops = new ArrayList<ItemStack>();
        Iterator<ItemStack> iterator = drops.iterator();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            int[] ores = OreDictionary.getOreIDs(stack);
            for (int oreId : ores) {
                String oreName = OreDictionary.getOreName(oreId);
                if (oreName.startsWith("ore")) {
                    String oreType = oreName.substring(3);
                    ArrayList<ItemStack> oreDusts = OreDictionary.getOres("dust" + oreType);
                    if (oreDusts.size() != 0) {
                        iterator.remove();
                        ItemStack toAdd = oreDusts.get(0).copy();
                        toAdd.stackSize = 2;
                        newDrops.add(toAdd); // drop 2 dusts
                        if (itemDust != null)
                            newDrops.add(itemDust.copy());
                    }
                } else if (oreName.equals("cobblestone") && itemDust != null) {
                    newDrops.add(itemDust.copy());
                    iterator.remove();
                }
            }
        }

        drops.addAll(newDrops);
    }

    @SubscribeEvent
    public void onDeathEvent(LivingDeathEvent event) {
        if (event.entity instanceof EntityPig) {
            if (!event.entity.worldObj.isRemote) {
                //Chance for bacon to drop, config ofcourse
                if ((new Random()).nextDouble() < HCConfig.INSTANCE.getDouble("baconDropChance")) {
                    EntityItem ei = new EntityItem(event.entityLiving.worldObj);
                    ei.setEntityItemStack(new ItemStack(HCItems.itemBacon, 1));
                    ei.setPosition(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
                    event.entityLiving.worldObj.spawnEntityInWorld(ei);
                }
            }
        }
    }

	/*@SubscribeEvent
    public void onEntityJoinEvent(EntityJoinWorldEvent event){
		if(hasShownUpdateInfo || !HCConfig.INSTANCE.getBool("checkForUpdates")) return;
		if(event.entity instanceof EntityPlayer){
			if(event.world.isRemote){
				//If update available and the update message wasn't sent to Version Checker, tell em!
				if(UpdateChecker.isUpdateAvailable && !Loader.isModLoaded("VersionChecker")){
					UpdateInfo info = UpdateChecker.infoAboutUpdate;
					Functions.showMessageInChat(((EntityPlayer)event.entity), Localization.getString(Localization.GUI_UPDATEAVAILABLE, info.latestVersion));
					Functions.showMessageInChat(((EntityPlayer)event.entity), Localization.getString(Localization.GUI_RELEASEDAT, info.dateOfRelease));
					int i = 0;
					for(String cl : info.changelog){
						Functions.showMessageInChat(((EntityPlayer)event.entity), cl);						
						
						i++;
						if(i >= 3){
							break;
						}
					}
					hasShownUpdateInfo = true;
				}else{
					Functions.showMessageInChat(((EntityPlayer) event.entity), Localization.getString(Localization.GUI_UPTODATE) + " (" + ModInfo.VERSION + ")");
					hasShownUpdateInfo = true;
				}
			}
		}
	}*/
}
