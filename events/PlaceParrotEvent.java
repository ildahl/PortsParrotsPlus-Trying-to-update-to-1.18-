package com.port.ppp.events;

import com.port.ppp.Main;
import com.port.ppp.ParrotConfig;
import com.port.ppp.mixin.invokers.PlayerEntityInvoker;
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

import static com.port.ppp.Main.reloadSlowFalling;

public class PlaceParrotEvent {

    public static ActionResult run(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.isSneaking() && player.getStackInHand(hand).isEmpty() && !player.getShoulderEntityLeft().isEmpty()) {
                EntityType.getEntityFromNbt(player.getShoulderEntityLeft(), world).ifPresent((entity) -> {
                    if (entity instanceof TameableEntity) ((TameableEntity) entity).setOwnerUuid(player.getUuid());
                    assert entity instanceof ParrotEntity;
                    BlockPos pos = hit.getBlockPos();
                    BlockState state = world.getBlockState(pos);
                    BlockPos pos2 = (state.getCollisionShape(world, pos).isEmpty()) ? pos : pos.offset(hit.getSide());
                    entity.setPosition((double) pos2.getX() + 0.5D, (double) (pos2.getY() + 1), (double) pos2.getZ() + 0.5D);
                    ((ServerWorld) world).tryLoadEntity(entity);
                    if (!player.getShoulderEntityRight().isEmpty()) {
                        ((PlayerEntityInvoker) player).pInvokeSetShoulderEntityLeft(player.getShoulderEntityRight());
                        ((PlayerEntityInvoker) player).pInvokeSetShoulderEntityRight(new NbtCompound());
                    } else ((PlayerEntityInvoker) player).pInvokeSetShoulderEntityLeft(new NbtCompound());
                    ((ParrotEntity) entity).setSitting(true);
                });
                reloadSlowFalling(player);
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }
}
