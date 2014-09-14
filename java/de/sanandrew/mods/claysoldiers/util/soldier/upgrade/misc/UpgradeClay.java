/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util.soldier.upgrade.misc;

import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.item.ItemClayManDoll;
import de.sanandrew.mods.claysoldiers.network.ParticlePacketSender;
import de.sanandrew.mods.claysoldiers.util.RegistryItems;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.SoldierUpgradeInst;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class UpgradeClay
        extends AUpgradeMisc
{
    @Override
    public void onConstruct(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        upgradeInst.getNbtTag().setShort(NBT_USES, (short) 4);
    }

    @Override
    public boolean onUpdate(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        short uses = upgradeInst.getNbtTag().getShort(NBT_USES);

        if( !clayMan.hasPath() ) {
            Collection<EntityItem> items = clayMan.getItemsInRange();
            for( EntityItem item : items ) {
                if( item.getEntityItem() != null
                    && item.getEntityItem().getItem() == RegistryItems.dollSoldier
                    && ItemClayManDoll.getTeam(item.getEntityItem()).getTeamName().equals(clayMan.getClayTeam()) )
                {
                    clayMan.setTargetFollowing(item);
                    break;
                }
            }
        } else if( clayMan.getTargetFollowing() instanceof EntityItem ) {
            EntityItem item = (EntityItem) clayMan.getTargetFollowing();

            if( item.getEntityItem() != null && item.getEntityItem().getItem() == RegistryItems.dollSoldier
                && ItemClayManDoll.getTeam(item.getEntityItem()).getTeamName().equals(clayMan.getClayTeam())
                && item.getDistanceSqToEntity(clayMan) < 1.0D
                && item.getEntityItem().stackSize > 0 )
            {
                EntityClayMan awakened = ItemClayManDoll.spawnClayMan(clayMan.worldObj, clayMan.getClayTeam(), item.posX, item.posY, item.posZ);

                awakened.playSound("dig.gravel", 1.0F, 1.0F);
                ParticlePacketSender.sendSoldierDeathFx(awakened.posX, awakened.posY, awakened.posZ, awakened.dimension, awakened.getClayTeam());
                awakened.dollItem = item.getEntityItem().splitStack(1);

                if( item.getEntityItem().stackSize == 0 ) {
                    item.setDead();
                }

                upgradeInst.getNbtTag().setShort(NBT_USES, --uses);

                if( uses == 0 ) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onPickup(EntityClayMan clayMan, SoldierUpgradeInst upgInst, ItemStack stack) {
        this.consumeItem(stack, upgInst);
        clayMan.playSound("random.pop", 1.0F, 1.0F);
    }
}
