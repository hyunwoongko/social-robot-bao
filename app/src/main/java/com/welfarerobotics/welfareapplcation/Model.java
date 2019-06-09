package com.welfarerobotics.welfareapplcation;

import lombok.ToString;

/**
 * @author : Hyunwoong
 * @when : 6/9/2019 3:30 PM
 * @homepage : https://github.com/gusdnd852
 */
@ToString
public class Model {
    private String name;
    private Long age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
