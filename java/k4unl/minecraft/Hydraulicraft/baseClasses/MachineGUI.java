package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

public class MachineGUI extends GuiContainer {
	private ResourceLocation resLoc;
	IHydraulicMachine mEnt;
	
	public class ToolTip{
		int x;
		int y;
		int w;
		int h;
		String title;
		String unit;
		float value;
		float max;
		
		public ToolTip(int _x, int _y, int _w, int _h, String _title, String _unit, float _value, float _max){
			x = _x;
			y = _y;
			w = _w;
			h = _h;
			
			title = _title;
			unit = _unit;;
			
			value = _value;
			max = _max;
		}
		
		public List<String> getText(){
			List<String> text = new ArrayList<String>();
			text.add(title);
			text.add((int)value + "/" + (int)max + " " + unit);
			return text;
		}
	}
	protected List<ToolTip> tooltipList = new ArrayList<ToolTip>();
	
	public MachineGUI(IHydraulicMachine Entity, Container mainContainer, ResourceLocation _resLoc) {
		super(mainContainer);
		mEnt = Entity;
		resLoc = _resLoc;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resLoc);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	
	@Override
    public List<String> handleTooltip(int mouseX, int mouseY, List<String> currenttip){
		for (ToolTip tip : tooltipList) {
			if(shouldRenderToolTip(mouseX, mouseY, tip)){
				currenttip.addAll(tip.getText());
			}
		}
        return currenttip;
    }
	
	private boolean shouldRenderToolTip(int mouseX, int mouseY, ToolTip theTip){
		return isPointInRegion(theTip.x, theTip.y, theTip.w, theTip.h, mouseX, mouseY);
	}
	
	public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow){
		int stringWidth = fontRenderer.getStringWidth(text);
		int newX = xOffset;
		if(stringWidth < w){
			newX = (w / 2) - (stringWidth / 2) + xOffset;
		}
		fontRenderer.drawStringWithShadow(text, newX, yOffset, Constants.COLOR_TEXT);
	}
	
	protected void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color, String toolTipTitle, String toolTipUnit){
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		float perc = (float)value / (float)max;
		int height = (int)(h * perc);
		//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
		drawRect(xOffset, yOffset + (h-height), xOffset + w, yOffset + h, color);
		
		tooltipList.add(new ToolTip(xOffset, yOffset, w, h, toolTipTitle, toolTipUnit, value, max));
	}
	
	protected void drawFluidAndPressure(){
		int color = 0xFFFFFFFF;
		String fluidName = "";
		if(!mEnt.getHandler().isOilStored()){
			color = Constants.COLOR_WATER;
			fluidName = FluidRegistry.WATER.getLocalizedName();
		}else{
			color = Constants.COLOR_OIL;
			fluidName = Fluids.fluidOil.getLocalizedName();
		}
		drawVerticalProgressBar(8, 16, 54, 16, mEnt.getHandler().getStored(), mEnt.getMaxStorage(), color, fluidName, "mB");
	
		color = Constants.COLOR_PRESSURE;
		drawVerticalProgressBar(152, 16, 54, 16, mEnt.getHandler().getPressure(), mEnt.getMaxPressure(mEnt.getHandler().isOilStored()), color, "Pressure", "mBar");
	}
	
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		tooltipList.clear();
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-94 + 2, Constants.COLOR_TEXT);
	}

}
