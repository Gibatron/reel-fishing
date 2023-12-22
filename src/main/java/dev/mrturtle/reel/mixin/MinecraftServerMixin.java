package dev.mrturtle.reel.mixin;

import dev.mrturtle.reel.ReelFishing;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;loadFavicon()Ljava/util/Optional;"))
    private void loadComponentsOnStart(CallbackInfo ci) {
        // Doing this to fix race condition with Polymer AutoHost, is there a better way to do this? Maybe.
        ReelFishing.load((MinecraftServer) (Object) this);
    }
}
