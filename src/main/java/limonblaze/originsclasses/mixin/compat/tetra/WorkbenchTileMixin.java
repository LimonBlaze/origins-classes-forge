package limonblaze.originsclasses.mixin.compat.tetra;


import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;

import java.util.Map;

@Mixin(WorkbenchTile.class)
public class WorkbenchTileMixin {
    @Inject(
            method = "Lse/mickelus/tetra/blocks/workbench/WorkbenchTile;applyCraftingBonusEffects(Lnet/minecraft/world/item/ItemStack;Ljava/lang/String;ZLnet/minecraft/world/entity/player/Player;[Lnet/minecraft/world/item/ItemStack;[Lnet/minecraft/world/item/ItemStack;Ljava/util/Map;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void modifyResult(ItemStack upgradedStack, String slot, boolean isReplacing, Player player, ItemStack[] preMaterials, ItemStack[] postMaterials, Map<ToolAction, Integer> tools, Level world, BlockPos pos, BlockState blockState, boolean consumeResources, CallbackInfoReturnable<ItemStack> cir){
        Mutable<ItemStack> mutable = new MutableObject();
        mutable.setValue(upgradedStack);
        IPowerContainer.getPowers(player, ApoliPowers.MODIFY_CRAFTING.get()).stream()
                .filter(cp -> ConfiguredItemCondition.check(cp.getConfiguration().itemCondition(),player.level,upgradedStack))
                .forEach( cp -> {
                    ConfiguredItemAction.execute(cp.getConfiguration().itemAction(), player.level, mutable);
                });
        cir.setReturnValue(mutable.getValue());
    }
}
