package com.port.ppp;

import com.port.ppp.callback.ParrotFeedCallback;
import net.fabricmc.api.ModInitializer;

import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

import java.util.Random;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
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