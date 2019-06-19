package com.welfarerobotics.welfareapplcation.bot.face;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;

import java.util.List;
import java.util.Map;


public class FaceHandler extends Handler {
    private ImageView eyes;
    private ImageView mouse;
    private TextView emotion;
    private float eyeX;
    private float eyeY;
    private int i = 0;
    private Activity activity;
    private Map<String, List<String>> map = ChatCache.getInstance().getEmotion();

    public FaceHandler(ImageView eyes, TextView emotion, ImageView mouse, Activity activity) {
        this.activity = activity;
        this.eyes = eyes;
        this.mouse = mouse;
        this.emotion = emotion;
        eyeX = eyes.getX();
        eyeY = eyes.getY();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String face = Eye.getEye(activity).getFacialExpression();
        face = face.substring(0, face.length() - 1);
        if (face.equals(FaceExpression.happiness.toString())) sayEmotion(FaceExpression.happiness);
        else if (face.equals(FaceExpression.anger.toString())) sayEmotion(FaceExpression.anger);
        else if (face.equals(FaceExpression.contempt.toString())) sayEmotion(FaceExpression.contempt);
        else if (face.equals(FaceExpression.fear.toString())) sayEmotion(FaceExpression.fear);
        else if (face.equals(FaceExpression.sadness.toString())) sayEmotion(FaceExpression.sadness);
        else if (face.equals(FaceExpression.disgust.toString())) sayEmotion(FaceExpression.disgust);
        else if (face.equals(FaceExpression.surprise.toString())) sayEmotion(FaceExpression.surprise);
    }

    void sayEmotion(FaceExpression expression) {
        List<String> strings = map.get(expression.toString());
        String txt = strings.get(Brain.random.nextInt(map.get(expression.toString()).size() - 1));
        Brain.hippocampus.decideToSay(txt);
        Mouth.get().say();
    }
}
