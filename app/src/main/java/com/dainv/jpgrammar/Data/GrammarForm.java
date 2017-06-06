package com.dainv.jpgrammar.Data;

/**
 * Created by user on 4/20/2016.
 */
public class GrammarForm {
    private int id;
    private int level; // N5,N4,N3,N2,N1
    private String form;
    private String description;

    public GrammarForm() {
        id = 0;
        level = 0;
        form = "";
        description = "";
    }

    public GrammarForm(String form, String description, int level) {
        this.form = form;
        this.description = description;
        this.level = level;
    }

    public GrammarForm(int id, String form, String description, int level) {
        this.id = id;
        this.form = form;
        this.description = description;
        this.level = level;
    }

    public void setId(int id) {
        // set id
        this.id = id;
    }

    public void setForm(String form) {
        // set grammar form
        this.form = form;
    }

    public void setDescription(String description) {
        // grammar form explanation
        this.description = description;
    }

    public void setLevel(int level) {
        // set level of grammar form
        this.level = level;
    }

    public int getId() {
        // get grammar form index
        return this.id;
    }

    public String getForm() {
        // get grammar form
        return this.form;
    }

    public String getDescription() {
        // get explanation
        return this.description;
    }

    public int getLevel() {
        // get level
        return this.level;
    }
}
