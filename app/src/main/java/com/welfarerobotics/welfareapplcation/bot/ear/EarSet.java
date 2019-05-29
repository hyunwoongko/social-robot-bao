package com.welfarerobotics.welfareapplcation.bot.ear;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.util.Pool;

/**
 * @author : Hyunwoong
 * @when : 5/29/2019 10:13 AM
 * @homepage : https://github.com/gusdnd852
 */
public class EarSet {
    private Ear leftEar = new Ear(); // 이름 디텍트
    private Ear rightEar = new Ear(); // 문장 디텍트
    private Attention attention = new Attention();
    private Activity activity;

    public EarSet(Activity activity) {
        this.activity = activity;
    }

    public void initEar() {
        initLeftEar();
        iniRightEar();
    }

    private void initLeftEar() {
        leftEar.ifHear(s -> { // 왼쪽 귀로 들으면
            attention.focus(s, activity, leftEar, speech -> { // 자신의 이름이 불렸는지 체크
                leftEar.block(); // 왼쪽 귀 비활성화
                rightEar.hear(); // 오른쪽 귀 활성화
            });
        });

        leftEar.ifNotHear(leftEar::hearAgain);
        // 왼쪽 귀 못들으면 무한 재반복
    }

    private void iniRightEar() {
        rightEar.ifHear(s -> {// 오른쪽 귀가 들리면
            Pool.threadPool.execute(() -> { // 쓰레드 전환
                Mouth.get().play(s);
//                Brain.think(s); // 대답 추론
//                Brain.speech(Mouth.get()); // 대답 말함
                Mouth.get().stop(() -> rightEar.hear()); // 오른쪽 귀 다시 듣기
            });
        });

        rightEar.ifNotHear(() -> { // 오른쪽 귀가 못 들으면
            blockHear();
            attention.block(activity); // 어텐션 비활성화
            leftEar.hearAgain();
        });
    }

    public void startHear() {
        leftEar.hear(); // 왼쪽 귀 확성화
    }

    public void blockHear() {
        leftEar.block(); // 왼쪽 귀 비활성화
        rightEar.block(); // 오른쪽 귀 비활성화
    }
}
