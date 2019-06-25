package com.welfarerobotics.welfareapplcation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author : Hyunwoong
 * @When : 4/3/2019 7:39 AM
 * @Homepage : https://github.com/gusdnd852
 */
@NoArgsConstructor
@AllArgsConstructor
public @Builder @Data class User {
    private String id;
    private String name;
    private String location;
    private ArrayList<Conversation> dict;
}
