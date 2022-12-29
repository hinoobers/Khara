package org.hinoob.khara.data;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import org.hinoob.khara.Khara;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final Map<User, KharaUser> userMap = new HashMap<>();

    public void create(User user){
        userMap.put(user, new KharaUser(user));
        get(user).onLoad();
    }

    public KharaUser get(User user){
        return userMap.getOrDefault(user, null);
    }

    public KharaUser get(UUID uuid){
        return userMap.get(PacketEvents.getAPI().getProtocolManager().getUser(PacketEvents.getAPI().getProtocolManager().getChannel(uuid)));
    }

    public Collection<KharaUser> getAll(){
        return userMap.values();
    }

    public void destroy(User user){
        userMap.remove(user);
    }
}
