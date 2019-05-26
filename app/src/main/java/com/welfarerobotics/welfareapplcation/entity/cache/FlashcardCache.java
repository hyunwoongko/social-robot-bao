package com.welfarerobotics.welfareapplcation.entity.cache;

import java.util.ArrayList;

public class FlashcardCache {
    private static ArrayList<String[]> flashcards = new ArrayList<>();
    private static FlashcardCache instance;

    private FlashcardCache() {
    }

    public synchronized static FlashcardCache getInstance() {
        if (instance == null) {
            instance = new FlashcardCache();
        }
        return instance;
    }
    
    public void addFlashcard(String[] item) {
        flashcards.add(item);
    }

    public ArrayList<String[]> getFlashcard(){
        return flashcards;
    }

    public String[] getFlashcard(int pos){
        return flashcards.get(pos);
    }

    public int getFlashcardSize(){
        return flashcards.size();
    }

    public void clear(){
        flashcards = new ArrayList<>();
    }
}
