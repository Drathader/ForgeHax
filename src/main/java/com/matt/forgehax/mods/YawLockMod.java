package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.util.command.Setting;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.mod.Category;
import com.matt.forgehax.util.mod.ToggleMod;
import com.matt.forgehax.util.mod.loader.RegisterMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterMod
public class YawLockMod extends ToggleMod {
    public final Setting<Boolean> do_once = getCommandStub().builders().<Boolean>newSettingBuilder()
            .name("do_once")
            .description("Will only fire update once")
            .defaultTo(false)
            .build();

    public final Setting<Boolean> auto_angle = getCommandStub().builders().<Boolean>newSettingBuilder()
            .name("auto_angle")
            .description("Automatically finds angle to snap to based on the direction you're facing")
            .defaultTo(true)
            .build();

    public final Setting<Double> custom_angle = getCommandStub().builders().<Double>newSettingBuilder()
            .name("custom_angle")
            .description("Custom angle to snap to")
            .defaultTo(0.0D)
            .min(-180D)
            .max(180D)
            .build();

    public YawLockMod() {
        super(Category.PLAYER, "YawLock", false, "Locks yaw to prevent moving into walls");
    }

    public double getYawDirection() {
        return Math.round((LocalPlayerUtils.getViewAngles().getYaw() + 1.f) / 45.f) * 45.f;
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        double yaw = getYawDirection();
        if(!auto_angle.get())
            yaw = custom_angle.get();
        LocalPlayerUtils.setViewAngles(event.getEntityLiving().rotationPitch, yaw);
        // disable after first set if set to do once
        if(isEnabled() && do_once.get())
            disable();
    }
}
