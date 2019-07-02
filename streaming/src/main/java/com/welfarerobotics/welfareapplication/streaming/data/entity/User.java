package com.welfarerobotics.welfareapplication.streaming.data.entity;

import com.welfarerobotics.welfareapplication.streaming.base.marker.Entity;
import com.welfarerobotics.welfareapplication.streaming.data.cache.UserCache;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 2:35
 * @homepage : https://github.com/gusdnd852
 */

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Entity {
    private String uid;
    private String name;
}
