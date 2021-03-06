package limonblaze.originsclasses.mixin.client;

import com.mojang.authlib.GameProfile;
import limonblaze.originsclasses.common.apoli.power.ModifySpeedOnItemUsePower;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import javax.annotation.Nonnull;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    
    @Shadow public abstract @Nonnull InteractionHand getUsedItemHand();
    
    public LocalPlayerMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }
    
    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue = 0.2F))
    private float originsClasses$modifyItemUseSlowdown(float originalSlowdown) {
        return ModifySpeedOnItemUsePower.modifySlowDown(this, originalSlowdown);
    }

}