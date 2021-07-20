package com.port.ppp;

import com.port.ppp.callback.ParrotFeedCallback;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

import java.util.Random;

public class Main implements ModInitializer {

	public static final StatusEffectInstance ONE_PARROT_EFFECT = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 999999, 0, false, false, true);
	public static final StatusEffectInstance TWO_PARROT_EFFECT = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 999999, 1, false, false, true);

	@Override
	public void onInitialize() {

		ONE_PARROT_EFFECT.setPermanent(true);
		TWO_PARROT_EFFECT.setPermanent(true);

		ParrotFeedCallback.EVENT.register((player, parrot) -> {
			if (parrot.getOwner() == player && parrot.isTamed() && parrot.getMaxHealth() > parrot.getHealth()) {
				parrot.setSitting(!parrot.isSitting()); // don't change whether parrot is sitting
				Random r = new Random();
				parrot.heal(.5f);
				parrot.handleStatus((byte) 18);
				player.world.playSound(player, parrot.getX(), parrot.getY(), parrot.getZ(), SoundEvents.ENTITY_PARROT_EAT, parrot.getSoundCategory(), 1.0F, 1.0F + (r.nextFloat() - r.nextFloat()) * 0.2F);
				return ActionResult.CONSUME;
			}
			return ActionResult.FAIL;
		});
	}
}