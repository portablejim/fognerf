package portablejim.fognerf;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class FogNerfCoremod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{ "portablejim.fognerf.EntityRendererTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null; //"portablejim.fognerf.FogNerf";
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
