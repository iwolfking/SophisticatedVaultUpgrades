//package xyz.iwolfking.sophisticatedvaultupgrades.mixin;
//
//import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
//import net.p3pp3rf1y.sophisticatedbackpacks.common.CommonEventHandler;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import xyz.iwolfking.sophisticatedvaultupgrades.blocks.tiles.DebagnetizerTileEntity;
//
//@Mixin(value = CommonEventHandler.class, remap = false)
//public class MixinSBCommonEventHandler {
//    @Inject(method = "onItemPickup", at = @At("HEAD"), cancellable = true)
//    private void cancelAroundDebag(EntityItemPickupEvent event, CallbackInfo ci) {
//        if(event.getEntity() != null) {
//            if(DebagnetizerTileEntity.hasDebagnetizerAround(event.getEntity())) {
//                ci.cancel();
//            }
//        }
//    }
//}
