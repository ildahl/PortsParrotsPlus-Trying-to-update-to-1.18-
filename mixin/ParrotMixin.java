package com.port.ppp.mixin;

import com.port.ppp.callback.ParrotFeedCallback;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParrotEntity.class)
public abstract class ParrotMixin {

	private final ArrayList<Item> SEEDS = new ArrayList<>(List.of(Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS));

	@Inject(at = @At("RETURN"), method = "interactMob", cancellable = true)
	private void onFeed(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (player.getStackInHand(hand) != null) {
			ItemStack itemInHand = player.getStackInHand(hand);
			if (itemInHand.toString().length() > 6) {
				if (itemInHand.toString().toLowerCase().contains("seeds") || SEEDS.contains(itemInHand.getItem())) {
					ActionResult result = ParrotFeedCallback.EVENT.invoker().interact(player, (ParrotEntity) (Object) this);
					if (result == ActionResult.CONSUME) {
						if (!player.isCreative()) itemInHand.decrement(1);
						cir.cancel();
					}
				}
			}
		}
	}
}