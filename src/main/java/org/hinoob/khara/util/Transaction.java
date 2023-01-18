package org.hinoob.khara.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class Transaction {

    private long sentStamp;
    private List<Runnable> tasks = new ArrayList<>();
}
