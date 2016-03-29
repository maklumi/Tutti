package com.example.homepc.tutti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HomePC on 28/3/2016.
 */
public class SearchFragment extends Fragment {
        Spinner regionSpinner, categorySpinner;
        EditText search_query;
        MyParseAdapter regionAdapter;
        MyParseAdapter categoryAdapter;
        Button find;
        ParseObject regionobject, categoryobject;
        ArrayList<ItemsModel> data = new ArrayList<ItemsModel>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.search_fragment_layout,
                container, false);
        regionSpinner = (Spinner) rootView.findViewById(R.id.searchfragment_spinner_region);
        search_query = (EditText) rootView.findViewById(R.id.searchfragment_ed_search);
        categorySpinner = (Spinner) rootView.findViewById(R.id.searchfragment_spinner_category);
        find = (Button) rootView.findViewById(R.id.searchfragment_button_find);
        find.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ParseQuery<ParseObject> query = ParseQuery.getQuery("product");
                query.whereEqualTo("cat_object", categoryobject);
                query.whereEqualTo("reg_object", regionobject);
                if (search_query.getText().length() > 0) {
                    query.whereMatches("title", "(" + search_query.getText().toString() + ")", "i");
                }
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList,
                                     ParseException e) {
                        if (e == null) {
                            data.clear();
                            for (int a = 0; a < scoreList.size(); a++) {
                                ParseObject object = scoreList.get(a);
                                ItemsModel model = new ItemsModel();
                                model.setDate(String.valueOf(object
                                        .getCreatedAt().toLocaleString()));
                                model.setPrice(object.getNumber("price")
                                        .toString());
                                model.setPhone(object.getString("phone"));
                                model.setDesc(object.getString("desc"));
                                model.setTitle(object.getString("title"));
                                model.setImageFile(object.getParseFile("image")
                                        .getUrl());
                                model.setObjectId(object.getObjectId());


                                data.add(model);

                            }
                            Intent i = new Intent(getActivity(),
                                    SearchResultActivity.class);
                            i.putExtra("data", data);
                            startActivity(i);

                        } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                            Toast.makeText(getActivity(),
                                    "Error " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("Barakah", e.getMessage());
                        } else {
                            Toast.makeText(getActivity(), "No internet connection!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        regionSpinnerSetup();
        CategorySpinnerSetup();

    }

    public void regionSpinnerSetup() {
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Region");
                return query;
            }
        };

        regionAdapter = new MyParseAdapter(getActivity(), factory);
        regionAdapter.setTextKey("name");
        regionSpinner.setAdapter(regionAdapter);
        regionSpinner.setSelection(1);
        regionSpinner.setOnItemSelectedListener(new RegionSpinnerListener());
    }

    public void CategorySpinnerSetup() {
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Category");
                return query;
            }
        };

        categoryAdapter = new MyParseAdapter(getActivity(),
                factory);
        categoryAdapter.setTextKey("name");
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(1);
        categorySpinner.setOnItemSelectedListener(new CategorySpinnerListener());
    }

    class RegionSpinnerListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View v, int position,
                                   long id) {
            // TODO Auto-generated method stub

            ParseObject theSelectedObject = regionAdapter.getItem(position);
            Log.e("ABC", "Name is : " + theSelectedObject.getString("name") + " objectId is : " + theSelectedObject.getObjectId());
            regionobject = theSelectedObject;
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // TODO Auto-generated method stub
            // Do nothing.
        }

    }

    class CategorySpinnerListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View v, int position,
                                   long id) {
            // TODO Auto-generated method stub
            ParseObject theSelectedObject = categoryAdapter.getItem(position);
            Log.e("ABC", theSelectedObject.getString("name") + " objectId is : " + theSelectedObject.getObjectId());
            categoryobject = theSelectedObject;
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // TODO Auto-generated method stub
            // Do nothing.
        }

    }
}
