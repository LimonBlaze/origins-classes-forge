package limonblaze.originsclasses.mixin;

import com.mojang.datafixers.util.Pair;
import limonblaze.originsclasses.common.OriginsClassesCommon;
import limonblaze.originsclasses.common.apoli.power.MultiMinePower;
import limonblaze.originsclasses.common.duck.SneakingStateSavingGameMode;
import limonblaze.originsclasses.common.network.S2CMultiMining;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin implements SneakingStateSavingGameMode {

    @Shadow protected ServerLevel level;
    @Final @Shadow protected ServerPlayer player;
    @Shadow public abstract void destroyAndAck(BlockPos pos, ServerboundPlayerActionPacket.Action action, String reason);

    @Unique private BlockState justMinedState;
    @Unique private boolean performingMultiMine = false;
    @Unique private boolean wasSneakingWhenBlockBreakStarted = false;

    @Inject(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", ordinal = 0))
    private void originsClasses$saveSneakingState(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        wasSneakingWhenBlockBreakStarted = player.isShiftKeyDown();
        OriginsClassesCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CMultiMining(!wasSneakingWhenBlockBreakStarted));
    }

    @Inject(method = "destroyAndAck", at = @At("HEAD"))
    private void originsClasses$saveBlockStateForMultiMine(BlockPos pos, ServerboundPlayerActionPacket.Action action, String reason, CallbackInfo ci) {
        justMinedState = level.getBlockState(pos);
    }

    @Inject(method = "destroyAndAck", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", ordinal = 0))
    private void originsClasses$multiMinePower(BlockPos pos, ServerboundPlayerActionPacket.Action action, String reason, CallbackInfo ci) {
        if(!wasSneakingWhenBlockBreakStarted && !performingMultiMine) {
            performingMultiMine = true;
            Optional<Pair<List<BlockPos>, Float>> result = MultiMinePower.getResult(player, pos, justMinedState);
            result.ifPresent(pair -> {
                ItemStack tool = player.getMainHandItem().copy();
                for(BlockPos bp : pair.getFirst()) {
                    destroyAndAck(bp, action, reason);
                    if(!player.getMainHandItem().sameItem(tool)) {
                        break;
                    }
                }
            });
            performingMultiMine = false;
        }
    }

    public boolean wasSneakingWhenBlockBreakingStarted() {
        return wasSneakingWhenBlockBreakStarted;
    }

}
