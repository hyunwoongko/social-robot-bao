package com.welfarerobotics.welfareapplication.streaming.base.extension;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.kinda.alert.KAlertDialog;
import es.dmoral.toasty.Toasty;
import java8.util.function.Consumer;

/**
 * @author : Hyunwoong
 * @when : 2019-07-03 오전 12:54
 * @homepage : https://github.com/gusdnd852
 * <p>
 * UI Util Wrapper 메소드 정의
 */
public abstract class UIExtension extends ActivityExtension {
    private KAlertDialog dialog;
    private boolean isDailogAvailable = false;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new KAlertDialog(this);
    }

    public void dialogProgress() {
        isDailogAvailable = true;
        dialog.changeAlertType(KAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("로딩 중");
        dialog.setContentText("잠시만 기다려주세요.");
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dialogError(String msg, Runnable... nextAction) {
        isDailogAvailable = true;
        dialog.changeAlertType(KAlertDialog.ERROR_TYPE);
        dialog.setTitleText("에러 발생");
        dialog.setContentText(msg);
        if (nextAction.length != 0) {
            dialog.setConfirmText("확인");
            dialog.setConfirmClickListener(kAlertDialog -> {
                for (Runnable r : nextAction) r.run();
            });
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dialogSuccess(String title, String content, Runnable... nextAction) {
        isDailogAvailable = true;
        dialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        if (nextAction.length != 0) {
            dialog.setConfirmText("확인");
            dialog.setConfirmClickListener(kAlertDialog -> {
                for (Runnable r : nextAction) r.run();
            });
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    public void setDialog(Consumer<KAlertDialog> consumer) {
        consumer.accept(dialog);
        isDailogAvailable = true;
    }

    public void showDialog() {
        if (isDailogAvailable) dialog.show();
        else throw new IllegalStateException("다이얼로그가 아직 세팅되지 않았어요");
    }

    public void dissmissDialog() {
        if (isDailogAvailable) dialog.dismissWithAnimation();
        else throw new IllegalStateException("다이얼로그가 아직 세팅되지 않았어요");
    }

    public void showSuccessToast(String msg) {
        Toasty.success(this, msg).show();
    }

    public void showErrorToast(String msg) {
        Toasty.error(this, msg).show();
    }

    public void showInfoToast(String msg) {
        Toasty.info(this, msg).show();
    }

    public void showWarningToast(String msg) {
        Toasty.warning(this, msg).show();
    }
}
