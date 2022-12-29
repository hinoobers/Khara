package org.hinoob.khara.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<X, Y> {

    private X x;
    private Y y;


}
