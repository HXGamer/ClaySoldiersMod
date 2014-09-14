/*******************************************************************************************************************
 * Authors:   Azanor, Vazkii, SanAndreasP
 * Copyright: SanAndreasP, SilverChiren, CliffracerX, Vazkii and Azanor
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 * Notes:     This class was created by <Azanor>. It was distributed as
 *            part of the Botania Mod by <Vazkii>, now it's part of the CLay Soldiers Mod.
 *            Botania github: https://github.com/Vazkii/Botania
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.client.particle;

import de.sanandrew.mods.claysoldiers.client.util.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Queue;

// TODO: recode this for my improved rendering code!
public class EntityNexusFX
        extends EntityFX
{
    public static Queue<EntityNexusFX> queuedRenders = new ArrayDeque<>();
    public boolean tinkle = false;

    float f;
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;
    float moteParticleScale;
    int moteHalfLife;

    public EntityNexusFX(World world, double x, double y, double z, float size, float red, float green, float blue, boolean distanceLimit, float maxAgeMul) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleGravity = 0.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.particleScale *= size;
        this.moteParticleScale = this.particleScale;
        this.particleMaxAge = (int) (28D / (Math.random() * 0.3D + 0.7D) * maxAgeMul);

        this.moteHalfLife = this.particleMaxAge / 2;
        this.noClip = true;
        this.setSize(0.01F, 0.01F);
        EntityLivingBase renderentity = Minecraft.getMinecraft().renderViewEntity;

        if( distanceLimit ) {
            int visibleDistance = 50;
            if( !Minecraft.getMinecraft().gameSettings.fancyGraphics ) {
                visibleDistance = 25;
            }

            if( renderentity == null || renderentity.getDistance(this.posX, this.posY, this.posZ) > visibleDistance ) {
                this.particleMaxAge = 0;
            }
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public static void dispatchQueuedRenders(Tessellator tessellator) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.NEXUS_PARTICLE);

        tessellator.startDrawingQuads();
        for( EntityNexusFX wisp : queuedRenders ) {
            wisp.renderQueued(tessellator);
        }
        tessellator.draw();

        queuedRenders.clear();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if( this.particleAge++ >= this.particleMaxAge ) {
            setDead();
        }

        this.motionY -= 0.04D * this.particleGravity;
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
        this.f = f;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;

        queuedRenders.add(this);
    }

    private void renderQueued(Tessellator tessellator) {
        float agescale = (float) this.particleAge / (float) this.moteHalfLife;
        if( agescale > 1F ) {
            agescale = 2 - agescale;
        }

        this.particleScale = this.moteParticleScale * agescale;

        float f10 = 0.5F * this.particleScale;
        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * f - interpPosX);
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * f - interpPosY);
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * f - interpPosZ);

        tessellator.setBrightness(240);
        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 0.5F);
        tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, 0, 1);
        tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, 1, 1);
        tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, 1, 0);
        tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, 0, 0);
    }
}
