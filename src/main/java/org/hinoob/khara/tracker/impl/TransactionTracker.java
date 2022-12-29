package org.hinoob.khara.tracker.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import lombok.Getter;
import org.hinoob.khara.data.KharaUser;
import org.hinoob.khara.tracker.Tracker;
import org.hinoob.khara.util.Pair;
import org.hinoob.khara.util.Transaction;

import java.util.*;

public class TransactionTracker extends Tracker {

    public TransactionTracker(KharaUser user) {
        super(user);
    }

    private final Deque<Transaction> transactionQueue = new LinkedList<>();
    private final List<Short> sentByUs = new ArrayList<>();
    private int idCounter = 0;
    @Getter private int sendCounter, receiveCounter;
    private long ping;

    @Override
    public void handle(PacketReceiveEvent event) {
        if(event.getPacketType() != PacketType.Play.Client.WINDOW_CONFIRMATION) return;
        WrapperPlayClientWindowConfirmation wrapper = new WrapperPlayClientWindowConfirmation(event);
        if(wrapper.getActionId() > 0 || transactionQueue.isEmpty()) return;

        Transaction transaction = transactionQueue.getFirst();
        if(transaction.getId() != wrapper.getActionId()){
            // player modifying ids / other anticheat
            user.disconnect("Invalid transaction");
            return;
        }

        this.ping = (System.nanoTime() - transaction.getTime());
        this.receiveCounter++;
        transaction.getTasks().forEach(Runnable::run);

        transactionQueue.removeFirst();
    }

    @Override
    public void handle(PacketSendEvent event) {
        if(event.getPacketType() != PacketType.Play.Server.WINDOW_CONFIRMATION) return;
        WrapperPlayServerWindowConfirmation wrapper = new WrapperPlayServerWindowConfirmation(event);

        if(sentByUs.remove((Short) wrapper.getActionId())){
            transactionQueue.add(new Transaction(wrapper.getActionId(), System.nanoTime()));
            this.sendCounter++;
        }
    }

    @Override
    public void handlePost(PacketReceiveEvent event) {

    }

    @Override
    public void handlePost(PacketSendEvent event) {

    }

    public void add(Runnable runnable){
        if(transactionQueue.isEmpty()){
            runnable.run();
        }else {
            transactionQueue.peekLast().getTasks().add(runnable);
        }
    }

    public void sendTransaction(){
        if(user.getUser().getConnectionState() != ConnectionState.PLAY) return;

        sentByUs.add((short)idCounter);
        user.getUser().sendPacket(new WrapperPlayServerWindowConfirmation(0, (short)idCounter, false));
        if(--idCounter == Short.MIN_VALUE) idCounter = 0;
    }
}
