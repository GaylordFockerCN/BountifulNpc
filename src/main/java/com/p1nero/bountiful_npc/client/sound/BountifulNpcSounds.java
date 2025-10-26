package com.p1nero.bountiful_npc.client.sound;

import com.p1nero.bountiful_npc.BountifulNpcMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class BountifulNpcSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BountifulNpcMod.MOD_ID);
    public static final RegistryObject<SoundEvent> ON_RECEPTIONIST_INTERACT = createEvent("on_receptionist_interact");
    private static RegistryObject<SoundEvent> createEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(BountifulNpcMod.MOD_ID, name.toLowerCase(Locale.ROOT))));
    }
}
