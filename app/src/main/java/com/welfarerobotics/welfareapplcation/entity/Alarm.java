package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 5/31/2019 3:07 AM
 * @homepage : https://github.com/gusdnd852
 */

@AllArgsConstructor
@NoArgsConstructor
public @Data class Alarm {
    private List<String> POST = new ArrayList<>();
    private List<String> DATE = new ArrayList<>();
    private List<String> MONTH = new ArrayList<>();
    private List<String> PRE = new ArrayList<>();
    private List<String> WEEK = new ArrayList<>();
    private List<String> HOUR = new ArrayList<>();
    private List<String> TIMEZONE = new ArrayList<>();
    private List<String> DAY = new ArrayList<>();
    private List<String> MIN = new ArrayList<>();
    private List<String> EVERY = new ArrayList<>();
    private List<String> HALF = new ArrayList<>();

    public void clear() {
        POST.clear();
        DATE.clear();
        MONTH.clear();
        PRE.clear();
        WEEK.clear();
        HOUR.clear();
        TIMEZONE.clear();
        DAY.clear();
        MIN.clear();
        EVERY.clear();
        HALF.clear();
    }
}
