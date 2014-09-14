/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util.soldier.effect;

import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.util.soldier.MethodState;

public class EffectBlindingRedstone
        extends ASoldierEffect
{
    @Override
    public void onConstruct(EntityClayMan clayMan, SoldierEffectInst effectInst) {
        effectInst.getNbtTag().setShort("ticksRemain", (short) 60);
    }

    @Override
    public boolean onUpdate(EntityClayMan clayMan, SoldierEffectInst effectInst) {
        short ticksRemain = (short) (effectInst.getNbtTag().getShort("ticksRemain") - 1);
        effectInst.getNbtTag().setShort("ticksRemain", ticksRemain);

        return ticksRemain == 0;
    }

    @Override
    public MethodState onTargeting(EntityClayMan clayMan, SoldierEffectInst effectInst, EntityClayMan target) {
        return MethodState.DENY;
    }

    @Override
    public void onClientUpdate(EntityClayMan clayMan, SoldierEffectInst effectInst) {
        clayMan.worldObj.spawnParticle("reddust", clayMan.posX, clayMan.posY, clayMan.posZ, 1.0F, 0.0F, 0.0F);
    }
}
