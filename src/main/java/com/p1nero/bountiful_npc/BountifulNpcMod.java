package com.p1nero.bountiful_npc;

import com.mojang.logging.LogUtils;
import com.p1nero.bountiful_npc.client.sound.BountifulNpcSounds;
import com.p1nero.bountiful_npc.villager.BountifulVillagers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BountifulNpcMod.MOD_ID)
public class BountifulNpcMod {

    public static final String MOD_ID = "bountiful_npc";

    public static final Logger LOGGER = LogUtils.getLogger();

    public BountifulNpcMod(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        BountifulVillagers.POI_TYPES.register(bus);
        BountifulVillagers.VILLAGER_PROFESSIONS.register(bus);
        BountifulNpcSounds.SOUNDS.register(bus);
    }

}
