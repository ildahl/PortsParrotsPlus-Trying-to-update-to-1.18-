package com.port.ppp.mixin;

import com.port.ppp.Main;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TameableShoulderEntity.class)
public class TameableShoulderEntityMixin {

    @Inject(at = @At("RETURN"), method = "mountOnto")
    public void startLooking(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!player.getShoulderEntityRight().isEmpty()) {
            player.removeStatusEffect(StatusEffects.SLOW_FALLING);
            player.addStatusEffect(Main.TWO_PARROT_EFFECT);
        } else {
            player.removeStatusEffect(StatusEffects.SLOW_FALLING);
            player.addStatusEffect(Main.ONE_PARROT_EFFECT);
        }
    }
}
