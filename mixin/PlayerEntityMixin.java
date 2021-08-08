package com.port.ppp.mixin;

import com.port.ppp.Main;
import com.port.ppp.ParrotConfig;
import com.port.ppp.mixin.accessors.EntityAccessor;
import com.port.ppp.mixin.accessors.LivingEntityAccessor;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityAccessor {

    private int first20 = 0;

    @Shadow public abstract NbtCompound getShoulderEntityLeft();
    @Shadow public abstract NbtCompound getShoulderEntityRight();

    @Shadow protected boolean isSubmergedInWater;

    @Inject(at = @At("TAIL"), method = "tickMovement")
    public void onInit(CallbackInfo ci) {
        if (first20 <= 20) { // only runs first 20 ticks of instance (accounts for lag)
            first20++;
            removeStatusEffect(StatusEffects.SLOW_FALLING);
            if (ParrotConfig.INSTANCE.allowSlowFalling) {
                if (!getShoulderEntityRight().isEmpty()) addStatusEffect(Main.TWO_PARROT_EFFECT);
                else if (!getShoulderEntityLeft().isEmpty()) addStatusEffect(Main.ONE_PARROT_EFFECT);
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "dropShoulderEntities")
    public void onParrotLoss(CallbackInfo ci) {
        if (ParrotConfig.INSTANCE.allowSlowFalling) this.removeStatusEffect(StatusEffects.SLOW_FALLING);
    }

    @Inject(at = @At("INVOKE"), method = "dropShoulderEntities", cancellable = true)
    public void beforeParrotLoss(CallbackInfo ci) {
        if (((EntityAccessor) this).getFallDistance() > .5f && !((EntityAccessor)this).isInPowderSnow() && !isSubmergedInWater) ci.cancel();
    }
}

