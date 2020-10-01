package sample;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> words;

    public Dictionary() {
        words = new ArrayList<Word>();
    }

    public Word getWord(int i) {
        return words.get(i);
    }

    public int getLength() {
        return words.size();
    }

    public void addWord(Word a) {
        words.add(a);
    }


    public void removeWord(Word a) {
        for (int i = 0; i < words.size(); i++) {
            if (a.equals(words.get(i))) {
                words.remove(i);
                break;
            }
        }
    }

}