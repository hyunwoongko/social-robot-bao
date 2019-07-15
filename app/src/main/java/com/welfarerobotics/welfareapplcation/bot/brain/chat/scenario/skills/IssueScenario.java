package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario.skills;

import android.app.Activity;
import com.welfarerobotics.welfareapplcation.bot.Mouth;
import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.IssueApi;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:29 AM
 * @homepage : https://github.com/gusdnd852
 */
public class IssueScenario {
    public static void process(String speech, Activity activity) throws IOException {
        String issue = IssueApi.getIssue();
        Brain.hippocampus.decideToSay(issue);
        Mouth.get().say(activity);
    }
}
