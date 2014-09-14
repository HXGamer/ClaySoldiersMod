/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util.soldier.upgrade.misc;

import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.network.ParticlePacketSender;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.SoldierUpgradeInst;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class UpgradeBrownMushroom
        extends UpgradeFood
{
    @Override
    public void onConstruct(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        upgradeInst.getNbtTag().setShort(NBT_USES, (short) 2);
    }

    @Override
    public void onPickup(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst, ItemStack stack) {
        this.consumeItem(stack, upgradeInst);
        clayMan.playSound("random.pop", 1.0F, 1.0F);

        upgradeInst.getNbtTag().setFloat("healAmount", 10.0F);
    }

    @Override
    protected void spawnParticles(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        ParticlePacketSender.sendDiggingFx(clayMan.posX, clayMan.posY, clayMan.posZ, clayMan.dimension, Blocks.brown_mushroom);
    }
}
