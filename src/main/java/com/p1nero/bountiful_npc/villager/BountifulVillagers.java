package com.p1nero.bountiful_npc.villager;

import com.google.common.collect.ImmutableSet;
import com.p1nero.bountiful_npc.BountifulNpcMod;
import io.ejekta.bountiful.content.BountifulContent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BountifulVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, BountifulNpcMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, BountifulNpcMod.MOD_ID);

    public static final RegistryObject<PoiType> BOARD_POI = POI_TYPES.register("board_poi",
            () -> new PoiType(ImmutableSet.copyOf(BountifulContent.INSTANCE.getBOARD().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> RECEPTIONIST =
            VILLAGER_PROFESSIONS.register("receptionist", () -> new VillagerProfession("receptionist",
                    holder -> holder.get() == BOARD_POI.get(), holder -> holder.get() == BOARD_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

}