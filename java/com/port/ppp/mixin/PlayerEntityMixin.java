package com.port.ppp.mixin;

import com.port.ppp.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
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

    private boolean init = true;

    @Shadow public abstract NbtCompound getShoulderEntityLeft();
    @Shadow public abstract NbtCompound getShoulderEntityRight();

    @Inject(at = @At("TAIL"), method = "tickMovement")
    public void onInit(CallbackInfo ci) {
        if (init) {
            if (!getShoulderEntityRight().isEmpty()) {
                this.removeStatusEffect(StatusEffects.SLOW_FALLING);
                this.addStatusEffect(Main.TWO_PARROT_EFFECT);
            }
            else if (!getShoulderEntityLeft().isEmpty()) {
                this.removeStatusEffect(StatusEffects.SLOW_FALLING);
                this.addStatusEffect(Main.ONE_PARROT_EFFECT);
            } else this.removeStatusEffect(StatusEffects.SLOW_FALLING);
        }
        init = false;
    }

    @Inject(at = @At("TAIL"), method = "dropShoulderEntities")
    public void onParrotLoss(CallbackInfo ci) {
        this.removeStatusEffect(StatusEffects.SLOW_FALLING);
    }

}

@Mixin(LivingEntity.class)
abstract class LivingEntityAccessor {
    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);
}

