/*******************************************************************************************************************
 * Author:    SanAndreasP
 * Copyright: SanAndreasP and SilverChiren
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported
 *            (http://creativecommons.org/licenses/by-nc-sa/3.0/)
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.entity.mount;

import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.util.IDisruptable;
import de.sanandrew.mods.claysoldiers.util.mount.EnumTurtleType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityTurtleMount
        extends EntityCreature
        implements IMount, IDisruptable
{
    protected static final int DW_TYPE = 20;
    protected static final int DW_TEXTURE = 21;

    public boolean spawnedFromNexus = false;
    public boolean specialDeath = false;
    public boolean shouldDropDoll = true;

    protected float moveSpeed;

    public EntityTurtleMount(World world) {
        super(world);

        this.stepHeight = 0.1F;
        this.moveSpeed = 0.6F;
        this.renderDistanceWeight = 5D;

        this.setSize(0.35F, 0.7F);
    }

    public EntityTurtleMount(World world, EnumTurtleType turtleType) {
        this(world);

        if( rand.nextInt(8192) == 0 ) {
            this.setSpecial();
        } else {
            this.setType(turtleType);
        }

        this.worldObj.playSoundAtEntity(this, "step.stone", 0.8F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.9F);
    }

    @Override
    public void updateEntityActionState() {
        if( this.riddenByEntity == null || !(this.riddenByEntity instanceof EntityClayMan) ) {
            super.updateEntityActionState();
        } else {
            EntityClayMan rider = (EntityClayMan) this.riddenByEntity;
            this.isJumping = rider.isJumping() || this.handleWaterMovement();
            this.moveForward = rider.moveForward;
            this.moveStrafing = rider.moveStrafing;
            this.rotationYaw = this.prevRotationYaw = rider.rotationYaw;
            this.rotationPitch = this.prevRotationPitch = rider.rotationPitch;
            rider.renderYawOffset = this.renderYawOffset;
            rider.fallDistance = 0.0F;

            if( rider.isDead || rider.getHealth() <= 0 ) {
                rider.mountEntity(null);
            }
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        if( this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this) ) {
            this.fallDistance = 0.0F;
            this.inWater = true;
            this.extinguish();
        } else {
            this.inWater = false;
        }

        return this.inWater;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setBoolean("fromNexus", spawnedFromNexus);
        nbt.setShort("turtleType", (short) this.getType());
        nbt.setShort("texture", this.dataWatcher.getWatchableObjectShort(DW_TEXTURE));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.spawnedFromNexus = nbt.getBoolean("fromNexus");
        this.setType(EnumTurtleType.values[nbt.getShort("turtleType")]);
        this.dataWatcher.updateObject(DW_TEXTURE, nbt.getShort("texture"));
    }

    @Override
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2) {
        return this.spawnedFromNexus || this.specialDeath ? null : super.entityDropItem(par1ItemStack, par2);
    }

    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.3D;
    }

    @Override
    public void mountEntity(Entity entity) {
        if( !(entity != null && entity instanceof EntityMinecart) ) {
            super.mountEntity(entity);
        }
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if( source == IDisruptable.disruptDamage ) {
            return super.attackEntityFrom(source, damage);
        }

        boolean shouldSpawnSpecial = rand.nextInt(16) == 0;

        this.specialDeath = source.isFireDamage() && !this.isSpecial() && shouldSpawnSpecial;

        Entity entity = source.getSourceOfDamage();
        if( !(entity instanceof EntityClayMan) && !source.isFireDamage() ) {
            damage = 999;
        } else if( source.isFireDamage() && this.getType() == EnumTurtleType.NETHERRACK.ordinal() ) {
            return false;
        }

        //TODO: readd ranged projectile check!
        if( this.riddenByEntity instanceof EntityClayMan ) {
//			if (e instanceof EntityGravelChunk) {
//				if (((EntityGravelChunk)e).getClayTeam() == ((EntityClayMan)riddenByEntity).getClayTeam())
//					return false;
//				else
//					i = origDmg;
//			}
//			if (e instanceof EntityFireball) {
//				if (((EntityFireball)e).getClayTeam() == ((EntityClayMan)riddenByEntity).getClayTeam())
//					return false;
//				else
//					i = origDmg;
//			}
//			if (e instanceof EntitySnowball) {
//				if (((EntitySnowball)e).getClayTeam() == ((EntityClayMan)riddenByEntity).getClayTeam())
//					return false;
//				else
//					i = origDmg;
//			}
        }

        boolean damageSuccess = super.attackEntityFrom(source, damage);

        if( damageSuccess && this.getHealth() <= 0 ) {
//				Item item1 = CSM_ModRegistry.horseDoll;
            //TODO: readd particles!
//				for( int i = 0; i < 4; i++ ) {
//					double a = posX + ((rand.nextFloat() - rand.nextFloat()) * 0.125D);
//					double b = boundingBox.minY + 0.125D + ((rand.nextFloat() - rand.nextFloat()) * 0.25D);
//					double c = posZ + ((rand.nextFloat() - rand.nextFloat()) * 0.125D);
//
////					CSMModRegistry.proxy.showEffect(this.worldObj, this, 13);
////					if (FMLCommonHandler.instance().getSide().isClient())
////						CSM_ModRegistry.proxy.showEffect((new EntityDiggingFX(CSM_ModRegistry.proxy.getClientWorld(), a, b, c, 0.0D, 0.0D, 0.0D, Block.dirt, 0, 0)));
//				}
            if( source.isFireDamage() && !this.isSpecial() && shouldSpawnSpecial ) {
                EntityTurtleMount kawako = new EntityTurtleMount(this.worldObj, EnumTurtleType.values[this.getType()]);
                kawako.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                kawako.setSpecial();
                kawako.chooseTexture();
                kawako.setTurtleSpecs();
                this.worldObj.spawnEntityInWorld(kawako);
            }
        }
        return damageSuccess;
    }

    @Override
    public void knockBack(Entity entity, float i, double d, double d1) {
        super.knockBack(entity, i, d, d1);
        if( entity instanceof EntityClayMan ) {
            motionX *= 0.6D;
            motionY *= 0.75D;
            motionZ *= 0.6D;
        }
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if( this.handleWaterMovement() ) {
            this.setJumping(false);
            if( this.isCollidedHorizontally ) {
                this.motionY = 0.3D;
            } else {
                this.motionY = 0.01D;
            }
        }

        super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
    }

    @Override
    public float getAIMoveSpeed() {
        return this.moveSpeed;
    }

    @Override
    public EntityTurtleMount setSpawnedFromNexus() {
        this.spawnedFromNexus = true;
        return this;
    }

    @Override
    public int getType() {
        return this.dataWatcher.getWatchableObjectShort(DW_TYPE);
    }

    @Override
    public void setSpecial() {
        this.setType(EnumTurtleType.KAWAKO);
    }

    @Override
    public boolean isSpecial() {
        return this.getType() == EnumTurtleType.KAWAKO.ordinal();
    }

    @Override
    public void disrupt() {
        this.attackEntityFrom(IDisruptable.disruptDamage, 99999);
    }

    @Override
    public void onUpdate() {
        this.jumpMovementFactor = this.getAIMoveSpeed() * (0.16277136F / (0.91F * 0.91F * 0.91F));

        super.onUpdate();

        double velocityDelta = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        for( double d = 0; velocityDelta > 0.05D && this.inWater && d < 1.0D + velocityDelta * 120.0D; d++ ) {
            double randX = (double) (this.rand.nextFloat() * 0.5F - 0.25F);
            double randZ = (double) (this.rand.nextFloat() * 0.5F - 0.25F);

            this.worldObj.spawnParticle("splash", this.posX + randX, this.posY + 0.125D, this.posZ + randZ, this.motionX, this.motionY, this.motionZ);
        }
    }

    @Override
    protected void onDeathUpdate() {
        this.deathTime = 20;
        this.setDead();
        //TODO spawn particles!
//        ParticlePacketSender.sendHorseDeathFx(this.posX, this.posY, this.posZ, this.dimension, (byte) this.getType());
    }

    @Override
    protected String getHurtSound() {
        return "step.stone";
    }

    @Override
    protected String getDeathSound() {
        return "step.stone";
    }

    @Override
    protected void jump() {
        this.motionY = 0.4D;
        this.isAirBorne = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        this.dataWatcher.addObject(DW_TYPE, (short) 0);
        this.dataWatcher.addObject(DW_TEXTURE, (short) 0);
    }

    //TODO: drop doll!
    @Override
    protected void dropFewItems(boolean flag, int i) {
//		Item item1 = CSMModRegistry.horseDoll;
//		dropItem(item1.itemID, 1, this.dataWatcher.getWatchableObjectShort(19));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean interact(EntityPlayer e) {
        return false;
    }

    public void setType(EnumTurtleType type) {
        this.dataWatcher.updateObject(DW_TYPE, (short) type.ordinal());
        this.chooseTexture();
        this.setTurtleSpecs();
    }

    public ResourceLocation getTurtleTexture() {
        return EnumTurtleType.values[this.getType()].textures[this.dataWatcher.getWatchableObjectShort(DW_TEXTURE)];
    }

    public void setTurtleSpecs() {
        EnumTurtleType type = EnumTurtleType.values[this.getType()];
        this.updateHealth(type.health);
        this.moveSpeed = type.moveSpeed;
    }

    protected void chooseTexture() {
        int textureId = (this.rand.nextInt(EnumTurtleType.values[this.getType()].textures.length));
        this.dataWatcher.updateObject(DW_TEXTURE, (short) textureId);
    }

    //TODO: drop doll!
    protected void dropItem(int itemID, int i, int j) {
//		entityDropItem(new ItemStack(itemID, i, j), 0.0F);
    }

    private void updateHealth(float health) {
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(health);
        this.setHealth(health);
    }
}
