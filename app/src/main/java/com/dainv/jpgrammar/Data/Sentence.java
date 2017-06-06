package com.dainv.jpgrammar.Data;

/**
 * Created by user on 4/20/2016.
 */
public class Sentence {
    private int id;
    private String kanji;
    private String romaji;
    private String translation;

    // constructor
    public Sentence() {
    }

    public Sentence(String kanji, String romaji, String translation) {
        this.kanji = kanji;
        this.romaji = romaji;
        this.translation = translation;
    }

    public Sentence(int id, String kanji, String romaji, String translation) {
        this.id = id;
        this.kanji = kanji;
        this.romaji = romaji;
        this.translation = translation;
    }

    public void setId (int id) {
        // set id
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

    public void setKanji(String kanji) {
        // set jp sentence
        this.kanji = kanji;
    }

    public void setRomaji(String romaji) {
        // set pronunciation text
        this.romaji = romaji;
    }

    public void setTranslation(String translation) {
        // set translation text
        this.translation = translation;
    }

    public String getKanji() {
        // return japanese sentence in kanji form
        return this.kanji;
    }

    public String getRomaji() {
        // return pronunciation of jp sentence
        return this.romaji;
    }

    public String getTranslation() {
        // return translation of jp sentence
        return this.translation;
    }
}
