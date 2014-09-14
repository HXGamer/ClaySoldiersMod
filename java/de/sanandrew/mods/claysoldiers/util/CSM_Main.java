/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import de.sanandrew.mods.claysoldiers.dispenser.BehaviorDisruptorDispenseItem;
import de.sanandrew.mods.claysoldiers.dispenser.BehaviorSoldierDispenseItem;
import de.sanandrew.mods.claysoldiers.util.soldier.ClaymanTeam;
import de.sanandrew.mods.claysoldiers.util.soldier.effect.SoldierEffects;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.SoldierUpgrades;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.misc.UpgradeFood;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = CSM_Main.MOD_ID, version = CSM_Main.VERSION, name = "Clay Soldiers Mod", guiFactory = CSM_Main.MOD_GUI_FACTORY,
     dependencies = "required-after:sapmanpack@[2.0.0,)")
public final class CSM_Main
{
    public static final String MOD_ID = "claysoldiers";
    public static final String VERSION = "2.0.0";
    public static final String MOD_LOG = "ClaySoldiers";
    public static final String MOD_CHANNEL = "ClaySoldiersNWCH";
    public static final String MOD_GUI_FACTORY = "de.sanandrew.mods.claysoldiers.client.gui.ModGuiFactory";
    public static final EventBus EVENT_BUS = new EventBus();

    private static final String MOD_PROXY_CLIENT = "de.sanandrew.mods.claysoldiers.client.util.ClientProxy";
    private static final String MOD_PROXY_COMMON = "de.sanandrew.mods.claysoldiers.util.CommonProxy";

    @Mod.Instance(CSM_Main.MOD_ID)
    public static CSM_Main instance;
    @SidedProxy(modId = CSM_Main.MOD_ID, clientSide = CSM_Main.MOD_PROXY_CLIENT, serverSide = CSM_Main.MOD_PROXY_COMMON)
    public static CommonProxy proxy;
    public static FMLEventChannel channel;

    public static CreativeTabs clayTab = new CreativeTabClaySoldiers();

    @Mod.EventHandler
    public void modPreInit(FMLPreInitializationEvent event) {
        event.getModMetadata().autogenerated = false;

        ModConfig.config = new Configuration(event.getSuggestedConfigurationFile());
        ModConfig.syncConfig();

        RegistryItems.initialize();
        RegistryBlocks.initialize();
        ClaymanTeam.initialize();

        UpgradeFood.excludeFood((ItemFood) Items.potato);
        UpgradeFood.excludeFood((ItemFood) Items.carrot);
    }

    @Mod.EventHandler
    public void modInit(FMLInitializationEvent event) {
        SoldierEffects.initialize();
        SoldierUpgrades.initialize();
        RegistryRecipes.initialize();

        FMLCommonHandler.instance().bus().register(this);

        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MOD_CHANNEL);

        proxy.modInit();

        RegistryEntities.registerEntities(this);

        BlockDispenser.dispenseBehaviorRegistry.putObject(RegistryItems.dollSoldier, new BehaviorSoldierDispenseItem());
        BlockDispenser.dispenseBehaviorRegistry.putObject(RegistryItems.disruptor, new BehaviorDisruptorDispenseItem());
        BlockDispenser.dispenseBehaviorRegistry.putObject(RegistryItems.disruptorHardened, new BehaviorDisruptorDispenseItem());
    }

    @Mod.EventHandler
    public void modIMC(IMCEvent event) {
        //TODO here comes the code for registering stuff recieved from external mods
    }

    @Mod.EventHandler
    public void modPostInit(FMLPostInitializationEvent event) {
        SoldierUpgrades.logUpgradeCount();
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent eventArgs) {
        if( eventArgs.modID.equals(MOD_ID) ) {
            ModConfig.syncConfig();
        }
    }
}
