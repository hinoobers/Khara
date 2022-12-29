package org.hinoob.khara.util;

import lombok.Getter;
import org.hinoob.khara.tracker.impl.TransactionTracker;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Transaction {

    private final int id;
    private final List<Runnable> tasks = new ArrayList<>();
    private final long time;

    public Transaction(int id, long time){
        this.id = id;
        this.time = time;
    }
}
