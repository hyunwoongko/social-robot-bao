package com.welfarerobotics.welfareapplcation.core.alarm;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import com.welfarerobotics.welfareapplcation.R;

import java.util.Calendar;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

;

public final class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "alarm_channel";

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Alarm alarm = intent.getBundleExtra(BUNDLE_EXTRA).getParcelable(ALARM_KEY);
        if (alarm == null) {
            Log.e(TAG, "Alarm is null", new NullPointerException());
            return;
        }

        final int id = AlarmUtils.getNotificationId(alarm);

        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Intent notifIntent = new Intent(context, AlarmLandingPageActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pIntent = PendingIntent.getActivity(
                context, id, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_alarm_white_24dp);
        builder.setColor(ContextCompat.getColor(context, R.color.accent));
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(alarm.getLabel());
        builder.setTicker(alarm.getLabel());
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        builder.setSound(null);
        builder.setPriority(Notification.PRIORITY_HIGH);
        Notification notification = builder.build();

        manager.notify(id, notification);

        //Reset Alarm manually
        setReminderAlarm(context, alarm);
        context.startActivity(new Intent(context, AlarmLandingPageActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
        System.out.println("AAAAAAAAAAAAAAAALLLLLLLLLLLAAAAAAAARRRRRMMMMMM");
    }

    //Convenience method for setting a notification
    public static void setReminderAlarm(Context context, Alarm alarm) {

        //Check whether the alarm is set to run on any days
        if (!AlarmUtils.isAlarmActive(alarm)) {
            //If alarm not set to run on any days, cancel any existing notifications for this alarm
            cancelReminderAlarm(context, alarm);
            return;
        }

        final Calendar nextAlarmTime = getTimeForNextAlarm(alarm);
        alarm.setTime(nextAlarmTime.getTimeInMillis());

        final Intent intent = new Intent(context, AlarmReceiver.class);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ALARM_KEY, alarm);
        intent.putExtra(BUNDLE_EXTRA, bundle);

        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                AlarmUtils.getNotificationId(alarm),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), pIntent);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, alarm.getTime(), pIntent);
        }

    }

    /**
     * Calculates the actual time of the next alarm/notification based on the user-set time the
     * alarm should sound each day, the days the alarm is set to run, and the current time.
     *
     * @param alarm Alarm containing the daily time the alarm is set to run and days the alarm
     *              should run
     * @return A Calendar with the actual time of the next alarm.
     */
    private static Calendar getTimeForNextAlarm(Alarm alarm) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());

        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);

        int count = 0;
        boolean isAlarmSetForDay;

        final SparseBooleanArray daysArray = alarm.getDays();

        do {
            final int index = (startIndex + count) % 7;
            isAlarmSetForDay =
                    daysArray.valueAt(index) && (calendar.getTimeInMillis() > currentTime);
            if (!isAlarmSetForDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
        } while (!isAlarmSetForDay && count < 7);

        return calendar;

    }

    public static void cancelReminderAlarm(Context context, Alarm alarm) {

        final Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                AlarmUtils.getNotificationId(alarm),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pIntent);
    }

    private static int getStartIndexFromTime(Calendar c) {

        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = 0;
                break;
            case Calendar.TUESDAY:
                startIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                startIndex = 2;
                break;
            case Calendar.THURSDAY:
                startIndex = 3;
                break;
            case Calendar.FRIDAY:
                startIndex = 4;
                break;
            case Calendar.SATURDAY:
                startIndex = 5;
                break;
            case Calendar.SUNDAY:
                startIndex = 6;
                break;
        }

        return startIndex;

    }

    private static void createNotificationChannel(Context ctx) {
        if (SDK_INT < O) return;

        final NotificationManager mgr = ctx.getSystemService(NotificationManager.class);
        if (mgr == null) return;

        final String name = ctx.getString(R.string.channel_name);
        if (mgr.getNotificationChannel(name) == null) {
            final NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_HIGH);
            channel.setBypassDnd(true);
            channel.setSound(null, null);
            mgr.createNotificationChannel(channel);
        }
    }

}
