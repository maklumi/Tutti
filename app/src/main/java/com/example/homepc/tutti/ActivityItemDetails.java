package com.example.homepc.tutti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

/**
 * Created by Maklumi on 29-03-16.
 */
public class ActivityItemDetails extends AppCompatActivity{

    TextView price, date, title, phone, description;
    ImageView imageView;
    Button buttonDelete;
    boolean deleted = false;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_layout);

        price = (TextView) findViewById(R.id.detailactivity_tv_price);
        date = (TextView) findViewById(R.id.detailactivity_tv_date);
        title = (TextView) findViewById(R.id.detailactivity_tv_title);
        phone = (TextView) findViewById(R.id.detailactivity_tv_phone);
        description = (TextView) findViewById(R.id.detailactivity_tv_description);
        imageView = (ImageView)findViewById(R.id.detailactivity_imageview);
        buttonDelete = (Button) findViewById(R.id.detailactivity_btnDelete);
        position = getIntent().getIntExtra("position", 0);
        final String objectId = getIntent().getStringExtra("objectId");
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog
            }
        });

        price.setText(getIntent().getStringExtra("price"));
        date.setText(getIntent().getStringExtra("date"));
        title.setText(getIntent().getStringExtra("title"));
        phone.setText(getIntent().getStringExtra("phone"));
        description.setText(getIntent().getStringExtra("description"));
        String url = getIntent().getStringExtra("url");

        Picasso.with(this).load(url).into(imageView);
    }

    private void deleteItem(ParseObject parseObject){
        parseObject.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(ActivityItemDetails.this, "Post deleted successfully.", Toast.LENGTH_LONG).show();
                    deleted=true;
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("result", deleted);
        i.putExtra("position", position);
        setResult(RESULT_OK, i);
        finish();
    }

    private void showDialog(final String objectId){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm");
        alert.setMessage("Enter Message");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = input.getText().toString();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
                query.whereEqualTo("objectId", objectId);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e==null){
                            if (object.getString("password").equals(value)) {
                                deleteItem(object);
                            } else {
                                Toast.makeText(ActivityItemDetails.this, "Wrong Password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                break;
            case R.id.homeAsUp:
                onBackPressed();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
