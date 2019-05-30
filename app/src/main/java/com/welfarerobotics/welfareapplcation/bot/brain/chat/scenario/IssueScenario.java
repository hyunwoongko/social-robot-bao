package com.welfarerobotics.welfareapplcation.bot.brain.chat.scenario;

import com.welfarerobotics.welfareapplcation.bot.brain.Brain;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.crawler.IssueApi;
import com.welfarerobotics.welfareapplcation.bot.brain.chat.state.ChatState;

import java.io.IOException;

/**
 * @author : Hyunwoong
 * @when : 5/28/2019 2:29 AM
 * @homepage : https://github.com/gusdnd852
 */
public class IssueScenario {
    public static ChatState process(String speech) throws IOException {
        String issue = IssueApi.getIssue();
        Brain.hippocampus.decideToSay(issue);
        return ChatState.NORMAL_STATE;
    }
}
