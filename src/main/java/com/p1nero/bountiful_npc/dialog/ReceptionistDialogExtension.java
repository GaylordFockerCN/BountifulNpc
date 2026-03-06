package com.p1nero.bountiful_npc.dialog;

import com.p1nero.bountiful_npc.BountifulNpcMod;
import com.p1nero.bountiful_npc.client.sound.BountifulNpcSounds;
import com.p1nero.bountiful_npc.villager.BountifulVillagers;
import com.p1nero.dialog_lib.api.entity.EntityDialogueExtension;
import com.p1nero.dialog_lib.api.entity.IEntityDialogueExtension;
import com.p1nero.dialog_lib.client.screen.DialogueScreen;
import com.p1nero.dialog_lib.client.screen.builder.StreamDialogueScreenBuilder;
import io.ejekta.bountiful.bounty.BountyData;
import io.ejekta.bountiful.content.BountifulContent;
import io.ejekta.bountiful.content.board.BoardBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@EntityDialogueExtension(modId = BountifulNpcMod.MOD_ID)
public class ReceptionistDialogExtension implements IEntityDialogueExtension<Villager> {

    @Override
    public EntityType<Villager> getEntityType() {
        return EntityType.VILLAGER;
    }

    /**
     * 仅特定职业才会对话
     */
    @Override
    public boolean canInteractWith(Player player, Villager currentTalking) {
        return currentTalking.getVillagerData().getProfession() == BountifulVillagers.RECEPTIONIST.get();
    }

    @Override
    public @Nullable InteractionResult shouldCancelInteract(Player player, Villager currentTalking, InteractionHand hand) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onPlayerInteract(Player player, Villager currentTalking, InteractionHand hand) {
        IEntityDialogueExtension.super.onPlayerInteract(player, currentTalking, hand);
        player.level().playSound(null, currentTalking.getOnPos(), BountifulNpcSounds.ON_RECEPTIONIST_INTERACT.get(),
                SoundSource.VOICE, 1.0F, 1.0F);
    }

    /**
     * 获取信息，用于客户端构造对话
     */
    @Override
    public CompoundTag getServerData(ServerPlayer player, Villager currentTalking, InteractionHand hand, CompoundTag senderData) {
        if(player.getMainHandItem().is(BountifulContent.INSTANCE.getBOUNTY_ITEM())) {
            senderData.putBoolean("isBounty", true);
        }
        if(player.getMainHandItem().is(BountifulContent.INSTANCE.getDECREE_ITEM())) {
            senderData.putBoolean("isDecree", true);
        }
        return senderData;
    }

    /**
     * 针对不同的情况给不同的对话
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public DialogueScreen getDialogScreen(StreamDialogueScreenBuilder dialogueScreenBuilder, LocalPlayer localPlayer, Villager villager, CompoundTag serverData) {
        dialogueScreenBuilder.start(Component.translatable("dialog.minecraft.villager.bountiful_npc.1"))
                .addFinalOption(Component.translatable("option.minecraft.villager.bountiful_npc.1"), 1);
        if(serverData.getBoolean("isBounty")) {
            dialogueScreenBuilder.addFinalOption(Component.translatable("option.minecraft.villager.bountiful_npc.2").withStyle(ChatFormatting.GREEN), 2);
        }
        if(serverData.getBoolean("isDecree")) {
            dialogueScreenBuilder.addFinalOption(Component.translatable("option.minecraft.villager.bountiful_npc.2").withStyle(ChatFormatting.GREEN), 1);
        }
        return dialogueScreenBuilder.build();
    }

    /**
     * 处理不同回调值
     */
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
        removeConservingPlayer(villager);
    }

}
