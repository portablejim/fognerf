package portablejim.fognerf.client;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class FogNerfGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraft) { }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return FogNerfConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement runtimeOptionCategoryElement) {
        return null;
    }
}
