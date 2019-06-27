package com.welfarerobotics.welfareapplcation.core.initial;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

/**
 * @author : Hyunwoong
 * @when : 2019-06-26 오전 11:08
 * @homepage : https://github.com/gusdnd852
 */
@Data
@RequiredArgsConstructor
public class States {
    private final String statesName;
    private ArrayList<String> cities;
}
