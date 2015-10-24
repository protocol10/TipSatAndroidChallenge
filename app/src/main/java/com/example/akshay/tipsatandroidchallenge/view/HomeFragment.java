package com.example.akshay.tipsatandroidchallenge.view;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.tipsatandroidchallenge.BaseApplication;
import com.example.akshay.tipsatandroidchallenge.R;
import com.example.akshay.tipsatandroidchallenge.adapters.EthinicityAdapter;
import com.example.akshay.tipsatandroidchallenge.adapters.MemeberListAdapter;
import com.example.akshay.tipsatandroidchallenge.database.EthinicityModel;
import com.example.akshay.tipsatandroidchallenge.database.MembersModel;
import com.example.akshay.tipsatandroidchallenge.interfaces.OnItemClickListener;
import com.example.akshay.tipsatandroidchallenge.parsers.FetchMembersParser;
import com.example.akshay.tipsatandroidchallenge.parsers.MemberParser;
import com.example.akshay.tipsatandroidchallenge.service.WebService;
import com.example.akshay.tipsatandroidchallenge.utils.UniversalAppConstants;
import com.example.akshay.tipsatandroidchallenge.utils.Utils;
import com.orm.SugarTransactionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by akshay on 24/10/15.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, OnItemClickListener {

    RecyclerView recyclerView;
    EditText searchText;
    ProgressBar progressBar;
    TextView textEthinicity;
    LinearLayout linearLayout;
    ImageView btnReload;
    RelativeLayout mainLayout;
    ImageView imageView;
    TextView errorTextView, favouriteText;
    WebService webService;
    LinearLayoutManager linearLayoutManager;
    Context context;
    MemeberListAdapter adapter;
    Button btnWeight, btnHeight;
    List<EthinicityModel> ethinicityModelList;
    List<MembersModel> membersModelList;
    String ethincityStr = "";
    int ethinicityValue = 0;
    boolean sortByHeight, sortByWeight;
    Map<String, String> queryMap;
    boolean isApiLaunched;
    boolean isFavouriteSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        membersModelList = new ArrayList<>();
        ethinicityModelList = Utils.fetchEthinicity();
        queryMap = new HashMap<>();
        queryMap.put("query", "list_status");
        queryMap.put("type", "json");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        initUi(view);
        init();
        setUpListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isApiLaunched)
            updateUIByEthinicity(ethinicityValue);
    }

    private void initUi(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.memberList);
        searchText = (EditText) view.findViewById(R.id.searchText);
        textEthinicity = (TextView) view.findViewById(R.id.textEthinicity);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnReload = (ImageView) view.findViewById(R.id.btnReload);
        btnHeight = (Button) view.findViewById(R.id.btnHeight);
        btnWeight = (Button) view.findViewById(R.id.btnWeight);
        linearLayout = (LinearLayout) view.findViewById(R.id.errorViewLayout);
        errorTextView = (TextView) view.findViewById(R.id.textView);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        favouriteText = (TextView) view.findViewById(R.id.favouriteText);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new MemeberListAdapter(membersModelList, context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setUpListeners() {
        textEthinicity.setOnClickListener(this);
        btnHeight.setOnClickListener(this);
        btnWeight.setOnClickListener(this);
        adapter.setOnItemClickListener(this);
        favouriteText.setOnClickListener(this);
        btnReload.setOnClickListener(this);
    }

    private void init() {
        webService = ((BaseApplication) getActivity().getApplication()).getWebService();
        if (webService == null) {
            webService = ((BaseApplication) getActivity().getApplication()).initializeRetrofit();
        }
        if (Utils.isNetworkAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            fetchMemberDetails(queryMap);
        } else {
            List<MembersModel> list = MembersModel.listAll(MembersModel.class);
            if (list.isEmpty()) {
                mainLayout.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "Please check your NetWork Connection", Toast.LENGTH_SHORT).show();
                membersModelList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void fetchMemberDetails(Map<String, String> queryMap) {
        webService.fetchMembers(queryMap, new Callback<FetchMembersParser>() {
            @Override
            public void success(FetchMembersParser fetchMembersParser, Response response) {
                Log.i("List Size is", "" + fetchMembersParser.getMembers());
                isApiLaunched = true;
                if (response.getStatus() == 200) {
                    linearLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    saveDataToDb(fetchMembersParser.getMembers());
                } else {

                    //TODO: Show Message
                    List<MembersModel> list = Utils.
                            fetchMembersByEthinicity(String.valueOf(ethinicityValue));
                    if (list.isEmpty()) {
                        linearLayout.setVisibility(View.VISIBLE);
                        errorTextView.setText("No Data found");
                        mainLayout.setVisibility(View.GONE);
                    } else {

                        linearLayout.setVisibility(View.GONE);
                        membersModelList.clear();
                        membersModelList.addAll(list);
                        mainLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Some thing went Wrong", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                List<MembersModel> list = Utils.
                        fetchMembersByEthinicity(String.valueOf(ethinicityValue));
                if (list.isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                    errorTextView.setText("Server Time Out");
                } else {
                    membersModelList.clear();
                    membersModelList.addAll(list);
                    Toast.makeText(getActivity(), "Server Time Out", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void saveDataToDb(final List<MemberParser> list) {
        SugarTransactionHelper.doInTansaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {
                MembersModel.deleteAll(MembersModel.class);
                for (MemberParser parser : list) {
                    MembersModel membersModel = new MembersModel(parser);
                    membersModel.save();
                }
                updateUIByEthinicity(ethinicityValue);
            }
        });
    }

    private void updateUIByEthinicity(int ethinicityValue) {
        membersModelList.clear();
        List<MembersModel> list = Utils.fetchMembersByEthinicity(String.valueOf(ethinicityValue));
        if (list.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);
            errorTextView.setText("No records found for selected ethinicity. Please try another");
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        membersModelList.addAll(list);
        if (membersModelList.isEmpty()) {
            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textEthinicity:
                showEthinicityPopUp();
                break;
            case R.id.btnHeight:
                if (sortByHeight) {
                    sortByHeight = false;
                    btnHeight.setBackgroundResource(R.drawable.filter_button_pressed);
                } else {
                    sortByHeight = true;
                    btnHeight.setBackgroundResource(R.color.color_filter_ideas);
                }
                fiterMemberList(sortByHeight, sortByWeight);
                break;
            case R.id.btnWeight:
                if (sortByWeight) {
                    sortByWeight = false;
                    btnHeight.setBackgroundResource(R.drawable.filter_button_pressed);
                } else {
                    sortByWeight = true;
                    btnHeight.setBackgroundResource(R.color.color_filter_ideas);
                }
                fiterMemberList(sortByHeight, sortByWeight);
                break;
            case R.id.btnReload:
                fetchMemberDetails(queryMap);
                break;
            case R.id.favouriteText:
                if (!isFavouriteSelected) {
                    isFavouriteSelected = true;
                    favouriteText.setBackgroundResource(R.drawable.drawable_yellow);
                    List<MembersModel> list = Utils.fetchFavouriteMembers(ethinicityValue);
                    if (list.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        errorTextView.setText("No Favourite Members Present");
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
//                        errorTextView.setText("No Favourite Members Present");
                        membersModelList.clear();
                        membersModelList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    isFavouriteSelected = false;
                    favouriteText.setBackgroundResource(R.drawable.drawable_rectangle);
                    updateUIByEthinicity(ethinicityValue);
                    linearLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void fiterMemberList(boolean sortByHeight, boolean sortByWeight) {
        List<MembersModel> list = new ArrayList<>();
        if (sortByHeight && sortByWeight) {
            list = Utils.fetchMembersByWeightAndHeight(String.valueOf(ethinicityValue));
        } else if (sortByHeight) {
            list = Utils.fetchMemberByHeight(String.valueOf(ethinicityValue));
        } else if (sortByWeight) {
            list = Utils.fetchMemberByHeight(String.valueOf(ethinicityValue));
        } else {
            list = Utils.fetchMembersByEthinicity(String.valueOf(ethinicityValue));
        }
        if (!list.isEmpty()) {
            membersModelList.clear();
            membersModelList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void showEthinicityPopUp() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(context.getString(R.string.string_ethinic));
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
                ethincityStr = ethinicityModelList.get(position).getText();
                updateUIByEthinicity(ethinicityValue);
                textEthinicity.setText("Ethinicity " + ethincityStr);
            }
        });
        if (isAdded())
            dialog.show();

    }

    @Override
    public void onItemClickListener(View view, int position) {
        MembersModel membersModel = membersModelList.get(position);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(UniversalAppConstants.INTENT_ID, membersModel.getMemberIdId());
        intent.putExtra(UniversalAppConstants.INTENT_ETH_POS, ethinicityValue);
        intent.putExtra(UniversalAppConstants.INTENT_ETH_VALUE, ethincityStr);
        startActivity(intent);
    }
}
