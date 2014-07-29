package de.sanandrew.mods.claysoldiers.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.util.SAPUtils;
import de.sanandrew.mods.claysoldiers.util.CSM_Main;
import de.sanandrew.mods.claysoldiers.util.IDisruptable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author SanAndreas
 * @version 1.0
 */
public class ItemDisruptor extends Item
{
    public int cooldown = 10;
    private boolean isHard_ = false;

    public ItemDisruptor(boolean hardened) {
        super();
        this.maxStackSize = 1;
        this.isHard_ = hardened;
        this.setMaxDamage((hardened ? 50 : 10) - 1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        disrupt(stack, world, player.posX, player.posY, player.posZ, player);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CSM_Main.MOD_ID + (this.isHard_ ? ":disruptor_cooked" : ":disruptor"));
    }

    @SuppressWarnings("unchecked")
    public static void disrupt(ItemStack stack, World world, double x, double y, double z, EntityPlayer player) {
        if( !world.isRemote ) {
            List<IDisruptable> disruptables = world.getEntitiesWithinAABB(IDisruptable.class, AxisAlignedBB.getBoundingBox(
                                                                                  x - 16D, y - 16D, z - 16D,
                                                                                  x + 16D, y + 16D, z + 16D
                                                                          ));
            for( IDisruptable disruptable : disruptables ) {
                disruptable.disrupt();
            }

            if( player != null ) {
                if( !player.capabilities.isCreativeMode ) {
                    stack.damageItem(1, player);
                }
            } else {
                if( stack.attemptDamageItem(1, SAPUtils.RNG) ) {
                    stack.stackSize--;
                }
            }
        }
    }
}