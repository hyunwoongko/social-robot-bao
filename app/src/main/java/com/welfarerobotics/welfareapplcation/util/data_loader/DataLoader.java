package com.welfarerobotics.welfareapplcation.util.data_loader;

import com.google.firebase.database.DataSnapshot;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 7:17 PM
 * @homepage : https://github.com/gusdnd852
 */
public interface DataLoader {
    TangramDataLoader TANGRAM_DATA_LOADER = TangramDataLoader.getInstance();
    FairytaleDataLoader FAIRYTALE_DATA_LOADER = FairytaleDataLoader.getInstance();
    FlashCardDataLoader FLASH_CARD_DATA_LOADER = FlashCardDataLoader.getInstance();
    EmotionCardDataLoader EMOTION_CARD_DATA_LOADER = EmotionCardDataLoader.getInstance();
    EmotionChatDataLoader EMOTION_CHAT_DATA_LOADER = EmotionChatDataLoader.getInstance();
    FallbackDataLoader FALLBACK_DATA_LOADER = FallbackDataLoader.getInstance();
    DanceDataLoader DANCE_DATA_LOADER = DanceDataLoader.getInstance();
    ServerDataLoader SERVER_DATA_LOADER = ServerDataLoader.getInstance();

    void load();

    void save(DataSnapshot snapshot);


    static void onDataLoad() {
        TANGRAM_DATA_LOADER.load();
        FAIRYTALE_DATA_LOADER.load();
        FLASH_CARD_DATA_LOADER.load();
        EMOTION_CARD_DATA_LOADER.load();
        EMOTION_CHAT_DATA_LOADER.load();
        FALLBACK_DATA_LOADER.load();
        DANCE_DATA_LOADER.load();
        SERVER_DATA_LOADER.load();

    }
}
