package com.port.ppp.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerEntityInvoker {

    @Invoker("setShoulderEntityLeft")
    void pInvokeSetShoulderEntityLeft(NbtCompound nbtCompound);

    @Invoker("setShoulderEntityRight")
    void pInvokeSetShoulderEntityRight(NbtCompound nbtCompound);
}
