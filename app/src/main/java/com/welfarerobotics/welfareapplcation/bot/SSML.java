package com.welfarerobotics.welfareapplcation.bot;

import java.text.MessageFormat;

/**
 * @author : Hyunwoong
 * @when : 7/15/2019 8:39 PM
 * @homepage : https://github.com/gusdnd852
 */
public class SSML {
    public static String setSSML(String tts) {
        return MessageFormat.format(
                "<speak>" +
                "<prosody volume=\"x-loud\">" +
                "<prosody rate=\"89%\">" +
                "<amazon:auto-breaths>" +
                "{0}" +
                "</amazon:auto-breaths>" +
                "</prosody>" +
                "</prosody>" +
                "</speak>", tts);
    }
}
