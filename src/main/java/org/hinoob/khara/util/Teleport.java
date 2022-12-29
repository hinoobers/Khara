package org.hinoob.khara.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.util.Vector;

@AllArgsConstructor
@Data
public class Teleport {

    private final KharaVector3d vector;
    private final int transactionID;
}
