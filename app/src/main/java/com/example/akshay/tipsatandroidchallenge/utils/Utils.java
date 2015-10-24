package com.example.akshay.tipsatandroidchallenge.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import com.example.akshay.tipsatandroidchallenge.database.EthinicityModel;
import com.example.akshay.tipsatandroidchallenge.database.MembersModel;
import com.orm.query.Select;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by akshay on 24/10/15.
 */
public class Utils {
    public static List<EthinicityModel> fetchEthinicity() {

        List<EthinicityModel> list = EthinicityModel.listAll(EthinicityModel.class);
        if (list.isEmpty()) {
            list.add(new EthinicityModel(0, "Asian"));
            list.add(new EthinicityModel(1, "Indian"));
            list.add(new EthinicityModel(2, "African Americans"));
            list.add(new EthinicityModel(3, "Asian Americans"));
            list.add(new EthinicityModel(4, "European"));
            list.add(new EthinicityModel(5, "British"));
            list.add(new EthinicityModel(6, "Jewish"));
            list.add(new EthinicityModel(7, "Latino"));
            list.add(new EthinicityModel(8, "Native American"));
            list.add(new EthinicityModel(9, "Arabic"));
        }
        return list;
    }

//    public static MembersModel membersModel(String memberId) {
//        MembersModel membersModel = MembersModel.find(MembersModel.class, "member_id = ?", memberId);
//    }

    public static List<MembersModel> fetchMemberById(String memberId) {
        List<MembersModel> list = MembersModel.find(MembersModel.class, "member_id = ?", memberId);
        return list;
    }

    public static Double covertGramsToKg(String s) {
        int grams = Integer.parseInt(s);
        double kg = grams * 0.001f;
        return kg;
    }

    /**
     * Method to check Network Connection
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static List<MembersModel> fetchMembersByEthinicity(String value) {
        List<MembersModel> list = MembersModel.find(MembersModel.class, "ethnicity = ?", value);
        return list;
    }

    public static List<MembersModel> fetchMembersByWeightAndHeight(String value) {
        String query = "SELECT * FROM MEMBERS_MODEL WHERE ETHNICITY =  ? ORDER BY WEIGHT, HEIGHT ASC";
        List<MembersModel> list = MembersModel.findWithQuery(MembersModel.class, query, value);
        return list;
    }

    public static List<MembersModel> fetchMemberByHeight(String value) {
        String query = "SELECT * FROM MEMBERS_MODEL WHERE ETHNICITY =  ? ORDER BY HEIGHT ASC";
        List<MembersModel> list = MembersModel.findWithQuery(MembersModel.class, query, value);
        return list;
    }

    public static List<MembersModel> fetchMemberByWeight(String value) {
        String query = "SELECT * FROM MEMBERS_MODEL WHERE ETHINICITY =  ? ORDER BY WEIGHT ASC";
        List<MembersModel> list = MembersModel.findWithQuery(MembersModel.class, query, value);
        return list;
    }

    public static String formatNumbers(double no) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(no);
    }

    public static void updateEthinicUI(TextView textViewEthinic, String ethnicity) {

        String str = "";
        switch (Integer.parseInt(ethnicity)) {
            case 0:
                str = "Asian";
                break;
            case 1:
                str = "Indian";
                break;
            case 2:
                str = "African Americans";
                break;
            case 3:
                str = "European";
                break;
            case 4:
                str = "British";
                break;
            case 5:
                str = "Jewish";
                break;
            case 6:
                str = "Jewish";
                break;
            case 7:
                str = "Latino";
                break;
            case 8:
                str = "Native American";
                break;
            case 9:
                str = "Arabic";
                break;
        }
        textViewEthinic.setText("" + str);
    }

    public static List<MembersModel> fetchFavouriteMembers(int ethinicityValue) {
        String query = "SELECT * FROM MEMBERS_MODEL WHERE IS_FAVOURITE = ?";
        Log.i("QUERY", query + " boolean " + String.valueOf(true));
        List<MembersModel> list = MembersModel.find(MembersModel.class, "is_favourite = ?", String.valueOf(true));
        return list;
    }
}
