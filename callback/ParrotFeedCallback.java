package com.port.ppp.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface ParrotFeedCallback {

    Event<ParrotFeedCallback> EVENT = EventFactory.createArrayBacked(ParrotFeedCallback.class,
            (listeners) -> (player, parrot) -> {
                for (ParrotFeedCallback listener : listeners) {
                    ActionResult result = listener.interact(player, parrot);
                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, ParrotEntity parrot);
}