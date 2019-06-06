package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import android.content.Intent;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.named_entity.SongEntityRecognizer;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.response.SongResponseGenerator;
import com.welfarerobotics.welfareapplcation.core.youtube.YoutubeActivity;

import java.io.IOException;
import java.util.List;

/**
 * @author : Hyunwoong
 * @when : 6/7/2019 3:31 AM
 * @homepage : https://github.com/gusdnd852
 */
public class SongScenario {
    public static void process(String speech, Activity activity) throws IOException {
        List<String> entities = SongEntityRecognizer.recognize(speech);
        String url = SongResponseGenerator.response(entities);
        //Youtube URL에서 ID만 추출
        int UrlIdIndex = url.indexOf("=");
        url = url.substring(UrlIdIndex + 1);
        //YoutubeActivity 실행 및 URL 전달
        Intent youtubeIntent = new Intent(activity.getApplicationContext(), YoutubeActivity.class);
        youtubeIntent.putExtra("url", url);
        activity.startActivity(youtubeIntent);
    }
}
