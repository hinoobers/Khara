package org.hinoob.khara.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

import java.util.ArrayList;
import java.util.List;

public class CheckContainer {

    private final List<Check> checks = new ArrayList<>();

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
