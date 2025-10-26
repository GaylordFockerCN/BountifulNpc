package com.p1nero.bountiful_npc.datagen.sound;

import com.p1nero.bountiful_npc.client.sound.BountifulNpcSounds;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BNSoundGenerator extends BNSoundProvider {

    public BNSoundGenerator(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
    }

    @Override
    public void registerSounds() {
        generateNewSoundWithSubtitle(BountifulNpcSounds.ON_RECEPTIONIST_INTERACT, "on_interaction", 1);
    }
}
