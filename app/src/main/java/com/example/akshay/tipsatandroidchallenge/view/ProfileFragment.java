package com.example.akshay.tipsatandroidchallenge.view;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.akshay.tipsatandroidchallenge.R;
import com.example.akshay.tipsatandroidchallenge.adapters.EthinicityAdapter;
import com.example.akshay.tipsatandroidchallenge.database.EthinicityModel;
import com.example.akshay.tipsatandroidchallenge.database.MembersModel;
import com.example.akshay.tipsatandroidchallenge.utils.UniversalAppConstants;
import com.example.akshay.tipsatandroidchallenge.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by akshay on 24/10/15.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    RelativeLayout relativeLayout;
    TextView textViewEthinic, textViewLabelBDay, textViewBDay, textViewWtLabel, textViewWeight,
            textViewHtLabel, textViewHeight, textViewVegLabel, textViewVeg, textViewDrinksLabel, textViewDrinks;
    EditText edit_text_status;
    ImageView imageView;
    Button btnFavourite, btnShare, btnBack, btnSms;
    String memberId;
    int ethinicityValue = 0;
    String ethinicityStr = "";
    MembersModel membersModel;
    List<EthinicityModel> ethinicityModelList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ethinicityModelList = Utils.fetchEthinicity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        updateValues(getArguments());
        setUpListeners();
        return view;
    }

    private void setUpListeners() {
        btnSms.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        textViewEthinic.setOnClickListener(this);
    }

    private void updateValues(Bundle arguments) {
        memberId = arguments.getString(UniversalAppConstants.INTENT_ID);
        ethinicityStr = arguments.getString(UniversalAppConstants.INTENT_ETH_VALUE);
        ethinicityValue = arguments.getInt(UniversalAppConstants.INTENT_ETH_POS, 0);

        List<MembersModel> list = Utils.fetchMemberById(memberId);

        if (!list.isEmpty()) {
            membersModel = list.get(0);
            textViewBDay.setText("" + membersModel.getDob());
            textViewHeight.setText("" + membersModel.getHeight() + " cm");
            textViewWeight.setText("" + Utils.formatNumbers(Utils.covertGramsToKg(membersModel.getWeight())) + " kg");
            updateDrinks(membersModel.getDrink());
            updateIsVeg(membersModel.getIsVeg());
            Picasso.with(getActivity()).load(membersModel.getImage()).resize(
                    (int) getActivity().getResources().getDimension(R.dimen.imig_size),
                    (int) getActivity().getResources().getDimension(R.dimen.imig_size)).
                    placeholder(R.mipmap.img_placeholder)
                    .error(R.mipmap.img_placeholder).into(imageView);
            if (membersModel.isFavourite()) {
                btnFavourite.setBackgroundResource(R.color.color_filter_ideas);
            } else {
                btnFavourite.setBackgroundResource(R.drawable.filter_button_pressed);
            }
            if (!TextUtils.isEmpty(membersModel.getStatus()))
                edit_text_status.setText("" + membersModel.getStatus());
            else {
                edit_text_status.setText("No Status");
            }
            relativeLayout.setVisibility(View.VISIBLE);
            Utils.updateEthinicUI(textViewEthinic, membersModel.getEthnicity());
        }
    }

    private void updateDrinks(String drink) {
        if (drink.equals("1")) {
            textViewDrinks.setText("Yes!");
        } else {
            textViewDrinks.setText("No");
        }
    }

    private void updateIsVeg(String isVeg) {
        if (isVeg.equals("1")) {
            textViewVeg.setText("Yes!");
        } else {
            textViewVeg.setText("No");
        }
    }

    private void initViews(View view) {
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnFavourite = (Button) view.findViewById(R.id.btnFavourite);
        btnShare = (Button) view.findViewById(R.id.btnShare);
        btnSms = (Button) view.findViewById(R.id.btnSms);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.layoutRelative);

        textViewBDay = (TextView) view.findViewById(R.id.textViewBDay);
        textViewLabelBDay = (TextView) view.findViewById(R.id.textViewLabelBDay);
        textViewDrinks = (TextView) view.findViewById(R.id.textViewDrinks);
        textViewDrinksLabel = (TextView) view.findViewById(R.id.textViewDrinksLabel);
        textViewEthinic = (TextView) view.findViewById(R.id.textViewEthinic);
        textViewHeight = (TextView) view.findViewById(R.id.textViewHeight);
        textViewHtLabel = (TextView) view.findViewById(R.id.textViewHtLabel);
        textViewWtLabel = (TextView) view.findViewById(R.id.textViewWtLabel);
        textViewWeight = (TextView) view.findViewById(R.id.textViewWeight);

        textViewVeg = (TextView) view.findViewById(R.id.textViewVeg);
        textViewVegLabel = (TextView) view.findViewById(R.id.textViewVegLabel);

        edit_text_status = (EditText) view.findViewById(R.id.edit_text_status);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                if (membersModel != null) {
                    if (!TextUtils.isEmpty(edit_text_status.getText().toString())) {
                        membersModel.setStatus(edit_text_status.getText().toString());
                        membersModel.save();
                    }
                }
                getActivity().onBackPressed();
                break;
            case R.id.btnFavourite:
                if (membersModel != null) {
                    if (membersModel.isFavourite()) {
                        membersModel.setIsFavourite(false);
                        btnFavourite.setBackgroundResource(R.drawable.filter_button_pressed);
                        if (!TextUtils.isEmpty(edit_text_status.getText().toString())) {
                            membersModel.setStatus(edit_text_status.getText().toString());
                        }
                        membersModel.save();
                    } else {
                        membersModel.setIsFavourite(true);
                        btnFavourite.setBackgroundResource(R.color.color_filter_ideas);
                        if (!TextUtils.isEmpty(edit_text_status.getText().toString())) {
                            membersModel.setStatus(edit_text_status.getText().toString());
                        }
                        membersModel.save();
                    }
                }
                break;
            case R.id.btnShare:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "TipStat Challenge \n Information of Member Id " +
                        membersModel.getMemberIdId() + " are as follows Height is " +
                        membersModel.getHeight() + " Weight is " + textViewWeight.getText() + " Status is " + membersModel.getStatus();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.textViewEthinic:
                showEthinicityPopUp();
                break;
        }
    }

    private void showEthinicityPopUp() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(getActivity().getString(R.string.string_ethinic));
        dialog.setContentView(R.layout.layout_ethinicity);
        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        EthinicityAdapter ethinicityAdapter = new EthinicityAdapter(getActivity(),
                R.layout.row_ethinicity, R.id.textView, ethinicityModelList);
        listView.setAdapter(ethinicityAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                ethinicityValue = ethinicityModelList.get(position).getValue();
                if (membersModel != null) {
                    membersModel.setEthnicity(String.valueOf(ethinicityValue));
                    membersModel.save();
                }
                String ethincityStr = ethinicityModelList.get(position).getText();
                textViewEthinic.setText(ethincityStr);
            }
        });
        if (isAdded())
            dialog.show();

    }
}
