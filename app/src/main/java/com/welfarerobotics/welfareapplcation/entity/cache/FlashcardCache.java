package com.welfarerobotics.welfareapplcation.entity.cache;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.welfarerobotics.welfareapplcation.entity.FlashCard;

import java.util.ArrayList;

public class FlashcardCache {

    private static FlashcardCache instance;
    private static ArrayList<FlashCard> flashCards =new ArrayList<>();

    private FlashcardCache() {
    }

    public synchronized static FlashcardCache getInstance() {
        if (instance == null) {
            instance = new FlashcardCache();
        }
        return instance;
    }


    public ArrayList<FlashCard> getFlashcard(){
        return flashCards;
    }

    public FlashCard getFlashcard(int pos){
        return flashCards.get(pos);
    }



    public void clear(){
        flashCards.clear();
        System.out.println(getClass().getName() + " : flashcards clear");
    }

    public void setFlashcard(DataSnapshot snapshot) {
        FlashCard flashCard;
        flashCard = snapshot.getValue(FlashCard.class);
        Log.d("플래시카드","URL:"+flashCard.getImageURL()+"\n"+"WORD"+flashCard.getWord());
        flashCards.add(flashCard);

    }
}
