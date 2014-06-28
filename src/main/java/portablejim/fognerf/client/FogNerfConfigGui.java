package portablejim.fognerf.client;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import portablejim.fognerf.FogNerf;

public class FogNerfConfigGui extends GuiConfig {
    public FogNerfConfigGui(GuiScreen parent) {
        super(parent,
                new ConfigElement(FogNerf.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                "fognerf", false, false, GuiConfig.getAbridgedConfigPath(FogNerf.configFile.toString()));
    }
}
