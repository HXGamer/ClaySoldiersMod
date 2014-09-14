/*******************************************************************************************************************
 * Authors:   SanAndreasP, CliffracerX
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util.soldier.upgrade.misc;

import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.SoldierUpgradeInst;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.righthand.AUpgradeRightHanded;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

public class UpgradeWoodButton
        extends AUpgradeMisc
{
    @Override
    public void onSoldierAttack(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst, EntityClayMan target, MutableFloat damage) {
        if( !clayMan.hasUpgrade(AUpgradeRightHanded.class) ) {
            damage.add(1.0F + clayMan.getRNG().nextFloat());
        }
    }

    @Override
    public void onPickup(EntityClayMan clayMan, SoldierUpgradeInst upgInst, ItemStack stack) {
        this.consumeItem(stack, upgInst);
        clayMan.playSound("random.pop", 1.0F, 1.0F);
    }
}
