package com.example.akshay.tipsatandroidchallenge.database;

import com.example.akshay.tipsatandroidchallenge.parsers.MemberParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by akshay on 24/10/15.
 */
public class MembersModel extends SugarRecord <MembersModel>{

    private String memberId;

    private String dob;

    private String status;

    private String ethnicity;

    private String weight;

    private String height;

    private String isVeg;

    private String drink;

    private String image;

    private boolean isFavourite = false;

    public MembersModel() {
        super();
    }

    public MembersModel(MemberParser memberParser) {
        this.memberId = memberParser.getId();
        this.dob = memberParser.getDob();
        this.status = memberParser.getStatus();
        this.ethnicity = memberParser.getEthnicity();
        this.weight = memberParser.getWeight();
        this.weight = memberParser.getWeight();
        this.height = memberParser.getHeight();
        this.isVeg = memberParser.getIsVeg();
        this.drink = memberParser.getDrink();
        this.image = memberParser.getImage();
        isFavourite = false;
    }

    public String getMemberIdId() {
        return memberId;
    }

    public void setId(String id) {
        this.memberId = id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(String isVeg) {
        this.isVeg = isVeg;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }
}
