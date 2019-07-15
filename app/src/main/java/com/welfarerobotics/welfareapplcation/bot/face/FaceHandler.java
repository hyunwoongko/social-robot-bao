package com.welfarerobotics.welfareapplcation.bot.face;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.entity.cache.ChatCache;
import com.welfarerobotics.welfareapplcation.util.Pool;

import java.util.List;


public class FaceHandler extends Handler {
    private ImageView eyes;
    private ImageView mouse;
    private TextView emotion;
    private float eyeX;
    private float eyeY;
    private int i = 0;
    private Activity activity;

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
        TextView textView = activity.findViewById(R.id.emotion);
        String face = Eye.getEye(activity).getFacialExpression();
        face = face.substring(0, face.length() - 1);

        if (!textView.getText().toString().equals(face)) {
            if (face.equals(FaceExpression.happiness.toString())) sayEmotion(FaceExpression.happiness);
            else if (face.equals(FaceExpression.anger.toString())) sayEmotion(FaceExpression.anger);
            else if (face.equals(FaceExpression.contempt.toString())) sayEmotion(FaceExpression.contempt);
            else if (face.equals(FaceExpression.fear.toString())) sayEmotion(FaceExpression.fear);
            else if (face.equals(FaceExpression.sadness.toString())) sayEmotion(FaceExpression.sadness);
            else if (face.equals(FaceExpression.disgust.toString())) sayEmotion(FaceExpression.disgust);
            else if (face.equals(FaceExpression.surprise.toString())) sayEmotion(FaceExpression.surprise);
//            textView.setText(face);
        }
    }

    void sayEmotion(FaceExpression expression) {
        System.out.println("DDDDDD : " + expression.toString());
        for (String s : ChatCache.getInstance().getEmotion().keySet()) {
            System.out.println("DDDDDDD EE : " + s);
        }
        List<String> strings = ChatCache.getInstance().getEmotion().get(expression.toString());
        String txt = strings.get(Brain.random.nextInt(ChatCache.getInstance().getEmotion().get(expression.toString()).size() - 1));
        Brain.hippocampus.decideToSay(txt);
        Pool.mouthThread.execute(() -> Mouth.get().say(activity));
    }
}
