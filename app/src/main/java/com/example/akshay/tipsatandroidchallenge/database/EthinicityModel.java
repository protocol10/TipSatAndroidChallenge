package com.example.akshay.tipsatandroidchallenge.database;

import com.orm.SugarRecord;

/**
 * Created by akshay on 24/10/15.
 */
public class EthinicityModel extends SugarRecord<EthinicityModel> {

    int value;
    String text;

    public EthinicityModel() {
        super();
    }

    public EthinicityModel(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
