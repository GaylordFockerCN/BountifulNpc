package com.p1nero.bountiful_npc.datagen;

import com.p1nero.bountiful_npc.BountifulNpcMod;
import com.p1nero.bountiful_npc.villager.BountifulVillagers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BNPoiTypeTagsProvider extends PoiTypeTagsProvider {
    public BNPoiTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, BountifulNpcMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .addOptional(BountifulVillagers.BOARD_POI.getId());
    }
}