package com.p1nero.bountiful_npc.mixin;

import io.ejekta.bountiful.forge.BountifulModForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BountifulModForge.class)
public class BountifulModForgeMixin {

    /**
     * 改用lithostitched api插入村庄中
     */
    @Inject(method = "onServerStarting", at = @At("HEAD"), cancellable = true, remap = false)
    private void bountifulNpc$onServerStarting(ServerStartingEvent evt, CallbackInfo ci) {
        ci.cancel();
    }

}
