package com.p1nero.bountiful_npc.dialog;

import com.p1nero.bountiful_npc.villager.BountifulVillagers;
import com.p1nero.dialog_lib.api.EntityDialogueExtension;
import com.p1nero.dialog_lib.api.IEntityDialogueExtension;
import com.p1nero.dialog_lib.client.screen.DialogueScreenBuilder;
import io.ejekta.bountiful.bounty.BountyData;
import io.ejekta.bountiful.content.BountifulContent;
import io.ejekta.bountiful.content.board.BoardBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@EntityDialogueExtension
public class ReceptionistDialogExtension implements IEntityDialogueExtension<Villager> {
    @Override
    public EntityType<Villager> getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public boolean canInteract(Player player, Villager currentTalking) {
        return currentTalking.getVillagerData().getProfession() == BountifulVillagers.RECEPTIONIST.get();
    }

    @Override
    public CompoundTag getServerData(ServerPlayer player, Villager currentTalking, InteractionHand hand, CompoundTag senderData) {
        if(player.getItemInHand(hand).is(BountifulContent.INSTANCE.getBOUNTY_ITEM())) {
            senderData.putBoolean("isBounty", true);
        }
        if(player.getItemInHand(hand).is(BountifulContent.INSTANCE.getDECREE_ITEM())) {
            senderData.putBoolean("isDecree", true);
        }
        return senderData;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public DialogueScreenBuilder getDialogBuilder(DialogueScreenBuilder dialogueScreenBuilder, LocalPlayer localPlayer, Villager villager, CompoundTag serverData) {
        dialogueScreenBuilder.start(Component.translatable("dialog.minecraft.villager.bountiful_npc.1"))
                .addFinalChoice(Component.translatable("option.minecraft.villager.bountiful_npc.1"), 1);
        if(serverData.getBoolean("isBounty")) {
            dialogueScreenBuilder.addFinalChoice(Component.translatable("option.minecraft.villager.bountiful_npc.2").withStyle(ChatFormatting.GREEN), 2);
        }
        if(serverData.getBoolean("isDecree")) {
            dialogueScreenBuilder.addFinalChoice(Component.translatable("option.minecraft.villager.bountiful_npc.2").withStyle(ChatFormatting.GREEN), 1);
        }
        return dialogueScreenBuilder;
    }

    @Override
    public void handleNpcInteraction(Villager villager, ServerPlayer serverPlayer, int i) {

        villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).ifPresent(globalPos -> {
            if(globalPos.dimension() != villager.level().dimension()) {
                return;
            }
            Level level = villager.level();
            BlockPos jobSitePos = globalPos.pos();
            if(i == 1) {
                MenuProvider screenHandlerFactory = serverPlayer.serverLevel().getBlockState(jobSitePos).getMenuProvider(level, jobSitePos);
                if (screenHandlerFactory != null) {
                    serverPlayer.openMenu(screenHandlerFactory);
                }
            }

            if(i == 2) {
                ItemStack holding = serverPlayer.getMainHandItem();
                if(!holding.is(BountifulContent.INSTANCE.getBOUNTY_ITEM())) {
                    holding = serverPlayer.getOffhandItem();
                }
                if(!holding.is(BountifulContent.INSTANCE.getBOUNTY_ITEM())) {
                    return;
                }
                BountyData data = BountyData.Companion.get(holding);
                if (data.tryCashIn(serverPlayer, holding)) {
                    if(level.getBlockEntity(jobSitePos) instanceof BoardBlockEntity bountyEntity) {
                        bountyEntity.updateCompletedBounties(serverPlayer);
                        bountyEntity.setChanged();
                    }
                }
            }

        });
        removeCurrentTalkingEntity(serverPlayer);
    }

}
