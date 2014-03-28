package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.tools.IToolWrench;

public class BlockMovingPane extends MachineBlockContainer {

	public BlockMovingPane() {
		super(Names.blockMovingPane);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileMovingPane();
	}

	// This block is called when block is broken and destroys the primary block.
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		
		TileMovingPane tileEntity = (TileMovingPane) world.getTileEntity(x, y,
				z);
		if (tileEntity != null) {			
			Location l = new Location(x,y,z);
			if(tileEntity.getIsPane()){
				l = tileEntity.getParentLocation();
			}else{
				l = tileEntity.getChildLocation();
			}
			if(l != null){
				world.setBlockToAir(l.getX(), l.getY(), l.getZ());
			}
		}
		
		super.breakBlock(world, x, y, z, block, meta);
	}

	// This method checks if primary block exists.
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);

	}
    @Override
    public int getRenderType(){
        return -1;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.isSneaking())
			return false;
		
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IToolWrench){
			return false;
		}
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileRFPump)){
			return false;
		}
		//TileRFPump compressor = (TileRFPump) entity;
		//player.openGui(Hydraulicraft.instance, GuiIDs.GUIRFPump, world, x, y, z);
		
		return true;
	}
    
	@Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side){
		if(world.isRemote)return false; 
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileMovingPane){
			TileMovingPane p = (TileMovingPane)te;
			if(p.getIsPane()) return false;
			if(!side.equals(p.getFacing())){
				ForgeDirection facing = p.getFacing();
				if(!side.equals(facing.getOpposite())){
					int i = 0;
					//Check if it is empty:
					while(!world.getBlock(x + facing.offsetX, y + facing.offsetY, z + facing.offsetZ).equals(Blocks.air)){
						facing = facing.getRotation(side);
						i++;
						if(i == 4){
							return false;
						}
					}
					//Remove our former child.
					Location child = p.getChildLocation();
					((TileMovingPane)world.getTileEntity(child.getX(), child.getY(), child.getZ())).setParentLocation(null);
					world.setBlock(child.getX(), child.getY(), child.getZ(), Blocks.air);
					world.setBlock(x + facing.offsetX, y + facing.offsetY, z + facing.offsetZ, HCBlocks.movingPane);
					TileMovingPane tilePane = (TileMovingPane) world.getTileEntity(x + facing.offsetX, y + facing.offsetY, z + facing.offsetZ);
					if(tilePane != null){
						tilePane.setParentLocation(new Location(x,y,z));
						tilePane.setIsPane(true);
						tilePane.setPaneFacing(p.getPaneFacing());
						tilePane.setFacing(facing);						
					}
					
					p.setChildLocation(new Location(x + facing.offsetX, y + facing.offsetY, z + facing.offsetZ));
					p.setFacing(facing);
					p.getHandler().updateBlock();
					world.notifyBlocksOfNeighborChange(x, y, z, this);
				}else{
					//Rotate the pane itself.
					Location child = p.getChildLocation();
					ForgeDirection paneFacing = p.getPaneFacing();
					paneFacing = paneFacing.getRotation(ForgeDirection.UP);
					((TileMovingPane)world.getTileEntity(child.getX(), child.getY(), child.getZ())).setPaneFacing(paneFacing);
					((TileMovingPane)world.getTileEntity(child.getX(), child.getY(), child.getZ())).getHandler().updateBlock();
					p.setPaneFacing(paneFacing);
					p.getHandler().updateBlock();
					world.notifyBlocksOfNeighborChange(x, y, z, this);
				}
			}
		}
		
		return true;
    }
}
