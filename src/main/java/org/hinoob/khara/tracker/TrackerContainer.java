package org.hinoob.khara.tracker;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

import java.util.ArrayList;
import java.util.List;

public class TrackerContainer {

    private final List<Tracker> trackers = new ArrayList<>();

    public void register(Tracker tracker){
        this.trackers.add(tracker);
    }
    public <T extends Tracker> T getByClass(Class<T> clazz){
        return (T) trackers.stream().filter(p -> p.getClass().equals(clazz)).findAny().orElse(null);
    }

    public List<Tracker> getAll(){
        return trackers;
    }
}
