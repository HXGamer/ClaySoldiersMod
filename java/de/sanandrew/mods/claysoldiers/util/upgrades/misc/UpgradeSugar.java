package de.sanandrew.mods.claysoldiers.util.upgrades.misc;

import de.sanandrew.core.manpack.util.javatuples.Quintet;
import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.network.PacketProcessor;
import de.sanandrew.mods.claysoldiers.network.packet.PacketParticleFX;
import de.sanandrew.mods.claysoldiers.util.upgrades.SoldierUpgradeInst;
import de.sanandrew.mods.claysoldiers.util.upgrades.SoldierUpgrades;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author SanAndreas
 * @version 1.0
 */
public class UpgradeSugar
    extends AUpgradeMisc
{
    @Override
    public void onConstruct(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        NBTTagCompound nbt = upgradeInst.getNbtTag();
        //nbt.setInteger("timeLeft", 20*60);
        clayMan.speed+=1;
    }

    @Override
    public boolean onUpdate(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
    	/*upgradeInst.getNbtTag().setInteger("timeLeft", upgradeInst.getNbtTag().getInteger("timeLeft") - 1);
    	if( upgradeInst.getNbtTag().getInteger("timeLeft") <= 0 ) {
            clayMan.playSound("random.break", 1.0F, 1.0F);
            clayMan.speed-=1;
            PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, clayMan.dimension, clayMan.posX, clayMan.posY, clayMan.posZ, 64.0D,
                    Quintet.with(PacketParticleFX.FX_BREAK, clayMan.posX, clayMan.posY, clayMan.posZ, Item.itemRegistry.getNameForObject(Items.stick))
            );
            return true;
        }*/
        return false;
    }

    @Override
    public void onPickup(EntityClayMan clayMan, ItemStack stack) {
        stack.stackSize--;
        clayMan.playSound("random.pop", 1.0F, 1.0F);
    }
}