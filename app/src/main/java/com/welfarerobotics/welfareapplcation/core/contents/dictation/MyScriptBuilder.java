package com.welfarerobotics.welfareapplcation.core.contents.dictation;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.myscript.atk.scw.SingleCharWidgetApi;
import com.myscript.certificate.MyCertificate;

/*  MyScript Api
   from RANDOM BOX
 */
public class MyScriptBuilder {

    private SingleCharWidgetApi widget;
    private Activity activity;

    public MyScriptBuilder(SingleCharWidgetApi widget, Activity activity) {
        this.widget = widget;
        this.activity = activity;
    }

    public void Build() {

        if (!widget.registerCertificate(MyCertificate.getBytes())) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return;
        }
        widget.addSearchDir("zip://" + activity.getPackageCodePath() + "!/assets/conf");
        widget.configure("ko_KR", "cur_text");
    }
}