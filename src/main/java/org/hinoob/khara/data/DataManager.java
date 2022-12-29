package org.hinoob.khara.data;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final Map<User, KharaUser> userMap = new HashMap<>();

    public void create(User user){
        userMap.put(user, new KharaUser(user));
        get(user).onLoad();
    }

    public KharaUser get(User user){
        return userMap.getOrDefault(user, null);
    }

    public void destroy(User user){
        userMap.remove(user);
    }
}
