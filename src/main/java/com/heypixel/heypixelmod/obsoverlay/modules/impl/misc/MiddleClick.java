package com.heypixel.heypixelmod.obsoverlay.modules.impl.misc;

import com.heypixel.heypixelmod.obsoverlay.Naven;
import com.heypixel.heypixelmod.obsoverlay.events.api.EventTarget;
import com.heypixel.heypixelmod.obsoverlay.events.api.types.EventType;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventMouseClick;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventRunTicks;
import com.heypixel.heypixelmod.obsoverlay.modules.Category;
import com.heypixel.heypixelmod.obsoverlay.modules.Module;
import com.heypixel.heypixelmod.obsoverlay.modules.ModuleInfo;
import com.heypixel.heypixelmod.obsoverlay.modules.impl.render.NotificationModule;
import com.heypixel.heypixelmod.obsoverlay.ui.notification.Notification;
import com.heypixel.heypixelmod.obsoverlay.ui.notification.NotificationLevel;
import com.heypixel.heypixelmod.obsoverlay.utils.FriendManager;
import com.heypixel.heypixelmod.obsoverlay.utils.rotation.Rotation;
import com.heypixel.heypixelmod.obsoverlay.utils.rotation.RotationManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;

/**
 * @Author：jiuxian_baka
 * @Date：2026/1/18 16:17
 */

@ModuleInfo(name = "Middle Click", cnName = "中键点击", description = "", category = Category.MISC)
public class MiddleClick extends Module {

    private boolean click;
    private boolean drop;
    private int slot;

    @EventTarget
    public void onPreTick(EventRunTicks event) {
        if (event.type() != EventType.PRE) return;
        if (click) {
            click = false;
            if (mc.hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof Player player) {

                if (FriendManager.isFriend(player)) {
                    Notification notification = new Notification(
                            NotificationLevel.ERROR, "Removed " + player.getName().getString() + " from friends!", 3000L
                    );
                    Naven.getInstance().getNotificationManager().addNotification(notification);
                    FriendManager.removeFriend(player);
                } else {
                    Notification notification = new Notification(NotificationLevel.SUCCESS, "Added " + player.getName().getString() + " as friends!", 3000L);
                    Naven.getInstance().getNotificationManager().addNotification(notification);
                    FriendManager.addFriend(player);
                }
            } else {
                slot = mc.player.getInventory().selected;
                int pearlSlot = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.getInventory().getItem(i);
                    if (stack.getItem() == Items.ENDER_PEARL) {
                        pearlSlot = i;
                        break;
                    }
                }
                if (pearlSlot != -1) {
                    mc.player.getInventory().selected = pearlSlot;
                    mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                    mc.player.swing(InteractionHand.MAIN_HAND);
                    drop = true;
                } else {
                    Notification notification = new Notification(NotificationLevel.ERROR, "No pearls found.", 3000L);
                    Naven.getInstance().getNotificationManager().addNotification(notification);
                }
            }
        }

        if (drop) {
            drop = false;
            mc.player.getInventory().selected = slot;
        }
    }

    @EventTarget
    public void onMouseKey(EventMouseClick e) {
        if (e.key() == 2 && !e.state()) {
            click = true;
        }
    }

}
