package k4unl.minecraft.Hydraulicraft.multipart;


import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

public class ItemPartValve extends JItemMultiPart
{
    public ItemPartValve(int id){
        super(id);
        setHasSubtypes(true);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setUnlocalizedName(Names.partValve[0].unlocalized);
    }
    /*

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (super.onItemUse(stack, player, w, x, y, z, side, hitX, hitY, hitZ))
        {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundGlassFootstep.getPlaceSound(), Block.soundGlassFootstep.getVolume() * 5.0F, Block.soundGlassFootstep.getPitch() * .9F);
            return true;
        }
        return false;
    }

    @Override
    public TMultiPart newPart(ItemStack item, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 vhit)
    {
    	PartValve w = (PartValve) MultiPartRegistry.createPart("tile." + Names.partValve[item.getItemDamage()].unlocalized, false);
        if (w != null)
            w.preparePlacement(item.getItemDamage());
        return w;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tab, List list){
    	for(int i = 0; i < 3; i++){
			list.add(new ItemStack(this, 1, i));
		}
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return "tile." + Names.partValve[stack.getItemDamage()].unlocalized;
    }

    @Override
    public void registerIcons(IconRegister reg){
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSpriteNumber(){
        return 1;
    }
    */
}
