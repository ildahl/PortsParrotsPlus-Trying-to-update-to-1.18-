package com.port.ppp;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@Config(name = "ppp")
public class ParrotConfig implements ConfigData {
    // follow this link:
    // https://shedaniel.gitbook.io/cloth-config/using-cloth-config/creating-a-config-option
    @ConfigEntry.Gui.Excluded
    public static ParrotConfig INSTANCE;

    @Comment("""
            §eUSE "/ppp reload" to apply mid-game§f
            When enabled, players will experience an infinite slow falling
            effect, so long as they have a parrot on their shoulder.
            """)
    public boolean allowSlowFalling = true;

    public static void register() {
        AutoConfig.register(ParrotConfig.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ParrotConfig.class).getConfig();
    }
}
