package org.hinoob.khara.data;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.hinoob.khara.check.CheckContainer;
import org.hinoob.khara.tracker.TrackerContainer;
import org.hinoob.khara.tracker.impl.FlyingTracker;
import org.hinoob.khara.tracker.impl.TransactionTracker;
import org.hinoob.khara.util.KharaVector3d;
import org.hinoob.khara.util.Rotation;
import org.hinoob.khara.util.Teleport;

public class KharaUser {

    @Getter private final User user;

    public final CheckContainer checkContainer = new CheckContainer();
    public final TrackerContainer trackerContainer = new TrackerContainer();

    private int TICK = 0;

    // Data
    @Getter private KharaVector3d position, lastPosition;
    @Getter private Rotation rotation, lastRotation;
    @Getter @Setter private Teleport lastAcceptedTeleport = null;
    @Getter private boolean clientGround, lastClientGround, lastLastClientGround;

    public KharaUser(User user){
        this.user = user;
    }

    public void onLoad(){
        trackerContainer.register(new FlyingTracker(this));
        trackerContainer.register(new TransactionTracker(this));
    }

    public void disconnect(String message){
        user.sendPacket(new WrapperPlayServerDisconnect(Component.text(message)));
        user.closeConnection();
    }

    public void tick(){
        ++TICK;

        if(TICK % 10 == 0){
            TransactionTracker transaction = trackerContainer.getByClass(TransactionTracker.class);
            transaction.sendTransaction();
        }
    }

    public void setPosition(double x, double y, double z){
        this.lastPosition = position;
        this.position = new KharaVector3d(x, y, z);
    }

    public void setRotation(float yaw, float pitch){
        this.lastRotation = rotation;
        this.rotation = new Rotation(yaw, pitch);
    }

    public void setOnGround(boolean clientGround){
        this.lastLastClientGround = lastClientGround;
        this.lastClientGround = this.clientGround;
        this.clientGround = clientGround;
    }
}
