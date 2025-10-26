package com.p1nero.bountiful_npc.events;

import com.p1nero.bountiful_npc.BountifulNpcMod;
import com.p1nero.bountiful_npc.villager.BountifulVillagers;
import io.ejekta.bountiful.bounty.BountyData;
import io.ejekta.bountiful.content.BountyItem;
import io.ejekta.bountiful.content.board.BoardBlockEntity;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BountifulNpcMod.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onVillagerInteract(PlayerInteractEvent.EntityInteract event) {
        //给对话联动让路
        if(ModList.get().isLoaded("p1nero_dl")) {
            return;
        }
        Player player = event.getEntity();
        Level level = event.getLevel();
        if(event.getTarget() instanceof Villager villager && villager.getVillagerData().getProfession() == BountifulVillagers.RECEPTIONIST.get()) {
            villager.getLookControl().setLookAt(event.getEntity());
            villager.getNavigation().stop();
            villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).ifPresent(globalPos -> {
                if(!event.getLevel().isClientSide && event.getLevel().dimension() == globalPos.dimension()) {
                    if(!event.getEntity().isShiftKeyDown()) {
                        ItemStack holding = player.getItemInHand(event.getHand());
                        BlockPos jobSitePos = globalPos.pos();

                        if (holding.getItem() instanceof BountyItem) {
                            BountyData data = BountyData.Companion.get(holding);
                            if (data.tryCashIn(player, holding)) {
                                if(level.getBlockEntity(jobSitePos) instanceof BoardBlockEntity bountyEntity) {
                                    bountyEntity.updateCompletedBounties(player);
                                    bountyEntity.setChanged();
                                    event.setCancellationResult(InteractionResult.CONSUME);
                                    event.setCanceled(true);
                                }
                            }
                        } else {
                            MenuProvider screenHandlerFactory = level.getBlockState(jobSitePos).getMenuProvider(level, jobSitePos);
                            if (screenHandlerFactory != null) {
                                player.openMenu(screenHandlerFactory);
                                event.setCancellationResult(InteractionResult.SUCCESS);
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            });
        }
    }

}
