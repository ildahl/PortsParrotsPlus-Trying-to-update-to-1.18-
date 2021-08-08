package com.port.ppp;

import com.port.ppp.callback.ParrotFeedCallback;
import com.port.ppp.commands.ToggleSlowFallingCommand;
import com.port.ppp.events.FeedParrotEvent;
import com.port.ppp.events.PlaceParrotEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class Main implements ModInitializer {

	public static final StatusEffectInstance ONE_PARROT_EFFECT = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 999999, 0, false, false, true);
	public static final StatusEffectInstance TWO_PARROT_EFFECT = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 999999, 1, false, false, true);

	@Override
	public void onInitialize() {
		ParrotConfig.register();

		ONE_PARROT_EFFECT.setPermanent(true);
		TWO_PARROT_EFFECT.setPermanent(true);

		CommandRegistrationCallback.EVENT.register(ToggleSlowFallingCommand::register);

		UseBlockCallback.EVENT.register((PlaceParrotEvent::run));
		ParrotFeedCallback.EVENT.register(FeedParrotEvent::run);
	}

	public static void reloadSlowFalling(PlayerEntity player) {
		player.removeStatusEffect(StatusEffects.SLOW_FALLING);
		if (ParrotConfig.INSTANCE.allowSlowFalling) {
			if (!player.getShoulderEntityRight().isEmpty()) player.addStatusEffect(TWO_PARROT_EFFECT);
			else if (!player.getShoulderEntityLeft().isEmpty()) player.addStatusEffect(ONE_PARROT_EFFECT);
		}
	}
}