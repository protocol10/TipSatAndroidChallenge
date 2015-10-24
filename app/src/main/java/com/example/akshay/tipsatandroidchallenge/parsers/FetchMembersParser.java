package com.example.akshay.tipsatandroidchallenge.parsers;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FetchMembersParser {

    @SerializedName("members")
    @Expose
    private List<MemberParser> members = new ArrayList<>();

    /**
     * @return The members
     */
    public List<MemberParser> getMembers() {
        return members;
    }

    /**
     * @param members The members
     */
    public void setMembers(List<MemberParser> members) {
        this.members = members;
    }

}
