package com.port.ppp.mixin;

import com.port.ppp.Main;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class BlockMixin {

    @Inject(at = @At("HEAD"), method = "onUse")
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (player.isSneaking()) {
            if (!world.isClient && !player.getShoulderEntityLeft().isEmpty()) {
                EntityType.getEntityFromNbt(player.getShoulderEntityLeft(), world).ifPresent((entity) -> {
                    if (entity instanceof TameableEntity) ((TameableEntity)entity).setOwnerUuid(player.getUuid());
                    assert entity instanceof ParrotEntity;
                    BlockPos pos2 = (state.getCollisionShape(world, pos).isEmpty()) ? pos : pos.offset(hit.getSide());
                    entity.setPosition((double) pos2.getX() + 0.5D, (double) (pos2.getY() + 1), (double) pos2.getZ() + 0.5D);
                    ((ServerWorld)world).tryLoadEntity(entity);
                    if (!player.getShoulderEntityRight().isEmpty()) {
                        ((PlayerEntityInvoker) player).invokeSetShoulderEntityLeft(player.getShoulderEntityRight());
                        ((PlayerEntityInvoker) player).invokeSetShoulderEntityRight(new NbtCompound());
                    } else ((PlayerEntityInvoker) player).invokeSetShoulderEntityLeft(new NbtCompound());
                    ((ParrotEntity) entity).setSitting(true);
                });
                if (!player.getShoulderEntityLeft().isEmpty()) {
                    player.removeStatusEffect(StatusEffects.SLOW_FALLING);
                    player.addStatusEffect(Main.ONE_PARROT_EFFECT);
                } else player.removeStatusEffect(StatusEffects.SLOW_FALLING);
            }
        }
    }
}
