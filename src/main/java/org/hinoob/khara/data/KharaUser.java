package org.hinoob.khara.data;

import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.hinoob.khara.check.CheckContainer;
import org.hinoob.khara.check.impl.Hitbox;
import org.hinoob.khara.tracker.TrackerContainer;
import org.hinoob.khara.tracker.impl.EntityTracker;
import org.hinoob.khara.tracker.impl.FlyingTracker;
import org.hinoob.khara.tracker.impl.TransactionTracker;
import org.hinoob.khara.util.AABB;
import org.hinoob.khara.util.KharaVector3d;
import org.hinoob.khara.util.Rotation;
import org.hinoob.khara.util.Teleport;

public class KharaUser {

    @Getter private final User user;

    public final CheckContainer checkContainer = new CheckContainer();
    public final TrackerContainer trackerContainer = new TrackerContainer();

    private int TICK = 0;

    // Data
    @Getter private KharaVector3d position = KharaVector3d.ZERO, lastPosition = KharaVector3d.ZERO;
    @Getter private AABB boundingBox;
    @Getter private Rotation rotation, lastRotation;
    @Getter @Setter private Teleport lastAcceptedTeleport = null;
    @Getter private boolean clientGround, lastClientGround, lastLastClientGround;

    public KharaUser(User user){
        this.user = user;
    }

    public void onLoad(){
        trackerContainer.register(new FlyingTracker(this));
        trackerContainer.register(new TransactionTracker(this));
        trackerContainer.register(new EntityTracker(this));

        checkContainer.register(new Hitbox(this));
    }

    public void disconnect(String message){
        user.sendPacket(new WrapperPlayServerDisconnect(Component.text(message)));
        user.closeConnection();
    }

    public void sendMessage(String message){
        user.sendMessage(Component.text(message), ChatTypes.CHAT);
    }

    public void tick(){
        ++TICK;

        TransactionTracker transaction = trackerContainer.getByClass(TransactionTracker.class);
        transaction.sendTransaction();
    }

    public void setPosition(double x, double y, double z){
        this.lastPosition = position;
        this.position = new KharaVector3d(x, y, z);

        this.boundingBox = new AABB(lastPosition.getX() - 0.3F, lastPosition.getY(), lastPosition.getZ() - 0.3F, lastPosition.getX() + 0.3F, lastPosition.getY() + 1.8F, lastPosition.getZ() + 0.3F);
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
