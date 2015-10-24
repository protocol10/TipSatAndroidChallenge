package com.example.akshay.tipsatandroidchallenge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.akshay.tipsatandroidchallenge.R;
import com.example.akshay.tipsatandroidchallenge.database.EthinicityModel;

import java.util.List;

/**
 * Created by akshay on 24/10/15.
 */
public class EthinicityAdapter extends ArrayAdapter<EthinicityModel> {

    Context context;
    int resource, textViewResourceId;
    List<EthinicityModel> list;

    public EthinicityAdapter(Context context, int resource, int textViewResourceId, List<EthinicityModel>
            list) {
        super(context, resource, textViewResourceId, list);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        EthinicityHolder ethinicityHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_ethinicity, parent, false);
            ethinicityHolder = new EthinicityHolder(view);
            view.setTag(ethinicityHolder);

        } else {
            ethinicityHolder = (EthinicityHolder) view.getTag();
        }
        EthinicityModel model = list.get(position);
        ethinicityHolder.textView.setText("" + model.getText());
        return view;
    }

    static class EthinicityHolder {
        TextView textView;

        public EthinicityHolder(View view) {
            textView = (TextView) view.findViewById(R.id.textView);
        }
    }
}
