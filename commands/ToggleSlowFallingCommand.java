package com.port.ppp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.port.ppp.ParrotConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static com.port.ppp.Main.reloadSlowFalling;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.*;

public class ToggleSlowFallingCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal("ppp")
                .then(literal("allowSlowFalling").then(argument("toggle", StringArgumentType.word()).requires(source -> source.hasPermissionLevel(4))
                    .suggests((context, sBuilder) -> suggestMatching(new String[]{"true", "false"}, sBuilder)).executes(ToggleSlowFallingCommand::setAllowSlowFalling)))
                .then(literal("reload").requires(source -> source.hasPermissionLevel(4)).executes(ToggleSlowFallingCommand::reload));
        dispatcher.register(builder);
    }

    public static int setAllowSlowFalling(CommandContext<ServerCommandSource> context) {
        ParrotConfig.INSTANCE.allowSlowFalling = StringArgumentType.getString(context, "toggle").equals("true");
        for (ServerPlayerEntity player : context.getSource().getWorld().getPlayers()) {
            reloadSlowFalling(player);
        }
        context.getSource().sendFeedback(new LiteralText(context.getSource().getName() + " set allowSlowFalling to " + ParrotConfig.INSTANCE.allowSlowFalling), true);
        return Command.SINGLE_SUCCESS;
    }

    public static int reload(CommandContext<ServerCommandSource> context) {
        for (ServerPlayerEntity player : context.getSource().getWorld().getPlayers()) {
            reloadSlowFalling(player);
        }
        context.getSource().sendFeedback(new LiteralText("Reloaded Port's Parrots Plus Slow Falling"), true);
        return Command.SINGLE_SUCCESS;
    }

}
