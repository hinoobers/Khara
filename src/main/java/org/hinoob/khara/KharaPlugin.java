package org.hinoob.khara;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.K;

// Takes care of loading the dependencies
// Events are registered in Khara
public class KharaPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.buildNoCache(this));
        PacketEvents.getAPI().getSettings().bStats(true).debug(true);
        PacketEvents.getAPI().load();

        // Safe place to set our instance, before onEnable
        Khara.setInstance(new Khara(this));
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();

        Khara.getInstance().load();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();

        Khara.getInstance().terminate();
    }
}
