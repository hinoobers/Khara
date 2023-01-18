package org.hinoob.khara.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import org.hinoob.khara.tracker.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CheckContainer {

    private final CopyOnWriteArrayList<Check> checks = new CopyOnWriteArrayList<>(); // Avoid CME

    public void register(Check check){
        this.checks.add(check);
    }

    public <T extends Check> T getByClass(Class<T> clazz){
        return (T) checks.stream().filter(p -> p.getClass().equals(clazz)).findAny().orElse(null);
    }

    public List<Check> getAll(){
        return checks;
    }
}
