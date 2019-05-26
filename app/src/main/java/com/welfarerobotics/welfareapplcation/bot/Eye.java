package com.welfarerobotics.welfareapplcation.bot;

import com.welfarerobotics.welfareapplcation.entity.Coordinate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author : Hyunwoong
 * @when : 5/26/2019 6:16 PM
 * @homepage : https://github.com/gusdnd852
 */
@NoArgsConstructor
@AllArgsConstructor
public class Eye {
    private static String facialExpression = "neutral";
    private static Coordinate faceCoordinates;
    private int eyeX;
    private int eyeY;

    public static String getFacialExpression() {
        return facialExpression;
    }

    public static Coordinate getFaceCoordinates() {
        return faceCoordinates;
    }

    public static void see(String facialExpression, Coordinate faceCoordinates) {
        Eye.facialExpression = facialExpression;
        Eye.faceCoordinates = faceCoordinates;
    }

    public int getEyeX() {
        return eyeX;
    }

    public int getEyeY() {
        return eyeY;
    }

    public void setEyeX(int eyeX) {
        this.eyeX = eyeX;
    }

    public void setEyeY(int eyeY) {
        this.eyeY = eyeY;
    }
}
