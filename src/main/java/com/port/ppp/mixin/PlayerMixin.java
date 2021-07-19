package com.port.ppp.mixin;

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
public abstract class PlayerMixin extends LivingEntityAccessor {

    private final StatusEffectInstance ONE_PARROT_EFFECT = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1000000000, 0, false, false, true);
    private final StatusEffectInstance TWO_PARROT_EFFECT = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1000000000, 1, false, false, true);

    private boolean init = true;
    private boolean hasOne = true;
    private boolean hasTwo = true;
    private boolean hasNone = true;

    @Shadow public abstract NbtCompound getShoulderEntityLeft();
    @Shadow public abstract NbtCompound getShoulderEntityRight();

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        if (hasOne || (hasNone && init)) {
            if (!this.getShoulderEntityLeft().isEmpty() && !this.getShoulderEntityRight().isEmpty()) {
                hasOne = false;
                hasTwo = true;
                hasNone = false;
                this.addStatusEffect(TWO_PARROT_EFFECT);
            }
        }
        if (hasNone) {
            if (!this.getShoulderEntityLeft().isEmpty() && this.getShoulderEntityRight().isEmpty()) {
                hasOne = true;
                hasTwo = false;
                hasNone = false;
                this.addStatusEffect(ONE_PARROT_EFFECT);
            }
        }
        if (hasTwo || hasOne) {
            if (this.getShoulderEntityLeft().isEmpty() && this.getShoulderEntityRight().isEmpty()) {
                hasOne = false;
                hasTwo = false;
                hasNone = true;
                this.removeStatusEffect(StatusEffects.SLOW_FALLING);
            }
        }
        if (init) init = false;
    }
}

@Mixin(LivingEntity.class)
abstract class LivingEntityAccessor {
    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);
}
