package com.port.ppp.mixin;

import com.port.ppp.callback.ParrotFeedCallback;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParrotEntity.class)
public abstract class ParrotMixin {

	@Inject(at = @At("RETURN"), method = "interactMob", cancellable = true)
	private void onFeed(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (player.getStackInHand(hand) != null) {
			ItemStack itemInHand = player.getStackInHand(hand);
			if (itemInHand.toString().length() > 6) {
				if (itemInHand.toString().substring(itemInHand.toString().length() - 5).equalsIgnoreCase("seeds")) {
					ActionResult result = ParrotFeedCallback.EVENT.invoker().interact(player, (ParrotEntity) (Object) this);
					if (result == ActionResult.CONSUME) {
						if (!player.isCreative()) itemInHand.setCount(itemInHand.getCount() - 1);
						cir.cancel();
					}
				}
			}
		}
	}
}
