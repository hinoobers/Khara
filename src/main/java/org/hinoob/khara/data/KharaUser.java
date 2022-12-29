package org.hinoob.khara.data;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.hinoob.khara.check.CheckContainer;
import org.hinoob.khara.tracker.TrackerContainer;
import org.hinoob.khara.tracker.impl.FlyingTracker;
import org.hinoob.khara.util.KharaVector3d;
import org.hinoob.khara.util.Rotation;

public class KharaUser {

    private final User user;

    public final CheckContainer checkContainer = new CheckContainer();
    public final TrackerContainer trackerContainer = new TrackerContainer();

    // Data
    @Getter private KharaVector3d position, lastPosition;
    @Getter private Rotation rotation, lastRotation;

    public KharaUser(User user){
        this.user = user;
    }

    public void onLoad(){
        trackerContainer.register(new FlyingTracker(this));
    }

    public void disconnect(String message){
        user.sendPacket(new WrapperPlayServerDisconnect(Component.text(message)));
        user.closeConnection();
    }

    public void setPosition(double x, double y, double z){
        this.lastPosition = position;
        this.position = new KharaVector3d(x, y, z);
    }

    public void setRotation(float yaw, float pitch){
        this.lastRotation = rotation;
        this.rotation = new Rotation(yaw, pitch);
    }
}
