package com.welfarerobotics.welfareapplication.streaming.base.marker;

import lombok.Data;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 2:37
 * @homepage : https://github.com/gusdnd852
 * <p>
 * Marker 인터페이스
 */
@Data
public class Cache<T> {
    protected T data;
}