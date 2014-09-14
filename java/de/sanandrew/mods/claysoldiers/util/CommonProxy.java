/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util;

import de.sanandrew.core.manpack.util.javatuples.Tuple;
import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.network.ServerPacketHandler;
import net.minecraft.nbt.NBTTagCompound;

public class CommonProxy
{
    public void modInit() {
        CSM_Main.channel.register(new ServerPacketHandler());
    }

    public void spawnParticles(byte particleId, Tuple data) {
    }

    public void applySoldierRenderFlags(int entityId, long upgFlags1, long upgFlags2, long effFlags1, long effFlags2) {
    }

    public void applyEffectNbt(int entityId, byte effectRenderId, NBTTagCompound nbt) {
    }

    public void applyUpgradeNbt(int entityId, byte upgradeRenderId, NBTTagCompound nbt) {
    }

    public void switchClayCam(boolean enable, EntityClayMan clayMan) {
    }
}
