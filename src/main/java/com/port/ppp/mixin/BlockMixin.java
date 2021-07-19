package com.port.ppp.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(AbstractBlock.class)
public class BlockMixin {
    @Inject(at = @At("HEAD"), method = "onUse")
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (player.isSneaking()) {
            if (!player.getShoulderEntityLeft().isEmpty()) {
                player.damage(DamageSource.MAGIC, 0); // Remove parrot from player
                BlockPos pos2 = (state.getCollisionShape(world, pos).isEmpty()) ? pos : pos.offset(hit.getSide());
                ParrotEntity parrot = getParrot(player);
                parrot.setPosition((double) pos2.getX() + 0.5D, (double) (pos2.getY() + 1), (double) pos2.getZ() + 0.5D);
                parrot.setSitting(true);
            }
        }
    }

    private ParrotEntity getParrot(PlayerEntity player) {
        List<ParrotEntity> list = player.getEntityWorld().getEntitiesByType(EntityType.PARROT, player.getBoundingBox(), Objects::nonNull);
        if (!list.isEmpty()) {
            for (ParrotEntity parrotEntity : list) {
                if (parrotEntity.getOwner() == player) return parrotEntity;
            }
        }
        return new ParrotEntity(EntityType.PARROT, player.world);
    }
}
