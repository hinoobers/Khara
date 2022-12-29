package org.hinoob.khara.tracker;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.check.Check;
import org.hinoob.khara.util.TrackerOption;

import java.util.ArrayList;
import java.util.List;

public class TrackerContainer {

    private final List<Tracker> trackers = new ArrayList<>();

    public void register(Tracker tracker){
        this.trackers.add(tracker);
    }

    public void execute(PacketReceiveEvent event, TrackerOption option){
        trackers.forEach(t -> t.handle(event, option));
    }

    public void execute(PacketSendEvent event, TrackerOption option){
        trackers.forEach(t -> t.handle(event, option));
    }

    public <T extends Tracker> T getByClass(Class<T> clazz){
        return (T) trackers.stream().filter(p -> p.getClass().equals(clazz)).findAny().orElse(null);
    }

    public List<Tracker> getAll(){
        return trackers;
    }
}
