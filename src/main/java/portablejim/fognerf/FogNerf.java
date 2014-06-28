package portablejim.fognerf;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.block.Block;

import java.util.Map;

//@Mod(modid = FogNerf.MODID)
public class FogNerf implements IFMLLoadingPlugin {
    public static final String MODID = "fognerf";

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ "portablejim.fognerf.EntityRendererTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
        //return "portablejim.fognerf.FogNerf";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) { }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
