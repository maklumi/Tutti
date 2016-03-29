package com.example.homepc.tutti;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ItemsModel> data;
    AlertDialog alert = null;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_fragment_layout);
        recyclerView = (RecyclerView) findViewById(R.id.browsefragment_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        data = getIntent().getParcelableArrayListExtra("data");

        if (data.size() < 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Oops! No Record Found.");
            builder.setPositiveButton("Ok", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    alert.dismiss();
                    finish();
                }
            });

            alert = builder.create(); // create one
            alert.show();
        } else {
            adapter = new RecyclerViewAdapter(data, SearchResultActivity.this);
            adapter.setClickListener(SearchResultActivity.this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        // TODO Auto-generated method stub
        Intent i = new Intent(this, ActivityItemDetails.class);
        ItemsModel model = data.get(position);
        i.putExtra("price", model.getPrice());
        i.putExtra("date", model.getDate());
        i.putExtra("title", model.getTitle());
        i.putExtra("desc", model.getDesc());
        i.putExtra("phone", model.getPhone());
        i.putExtra("url", model.getImageFile());
        i.putExtra("objectId",model.getObjectId());
        i.putExtra("position",position);

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                boolean result=data.getBooleanExtra("result",false);
                if(result)
                {
                    int position=data.getIntExtra("position",0);
                    this.data.remove(position);
                    adapter.updateData(this.data);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
