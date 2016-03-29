package com.example.homepc.tutti;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Maklumi on 29-03-16.
 */
public class MyParseAdapter extends ParseQueryAdapter<ParseObject> {

    public MyParseAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
