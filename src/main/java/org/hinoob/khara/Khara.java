package org.hinoob.khara;

import com.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import lombok.Setter;
import org.hinoob.khara.data.DataManager;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.listener.packet.PacketListener;
import org.hinoob.khara.listener.packet.UserListener;
import org.hinoob.khara.tracker.impl.TransactionTracker;

import java.util.logging.Logger;

public class Khara {

    @Getter @Setter private static Khara instance;

    @Getter private final KharaPlugin plugin;

    @Getter private final DataManager dataManager = new DataManager();

    public Khara(KharaPlugin plugin){
        this.plugin = plugin;
    }

    public void load(){
        PacketEvents.getAPI().getEventManager().registerListener(new UserListener());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListener());

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for(KharaUser user : dataManager.getAll()){
                user.tick();
            }
        }, 0L, 1L);

        getLogger().info("Enabled!");
    }

    public void terminate(){
        getLogger().info("Disabled!");
    }

    public static Logger getLogger(){
        return Khara.getInstance().getPlugin().getLogger();
    }
}
