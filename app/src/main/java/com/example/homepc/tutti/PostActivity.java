package com.example.homepc.tutti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by Maklumi on 29-03-16.
 */
public class PostActivity extends AppCompatActivity {
    String imagePath;
    ImageView postImage;
    Spinner regionSpinner, categorySpinner;
    ParseQueryAdapter <ParseObject> regionAdapter, categoryAdapter;
    ParseObject regionObject, categoryObject;
    ScrollView scrollView;
    EditText et_title, et_desc, et_price, et_password, et_phone;
    Button buttonSubmit;
    Bitmap bitmapPost = null;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity_layout);

        scrollView = (ScrollView)findViewById(R.id.postactivity_scrollview);
        regionSpinner = (Spinner) findViewById(R.id.postactivity_spinner_region);
        categorySpinner = (Spinner) findViewById(R.id.postactivity_spinner_category);
        et_desc = (EditText) findViewById(R.id.postactivity_ed_description);
        et_title = (EditText)findViewById(R.id.postactivity_ed_title);
        et_price = (EditText)findViewById(R.id.postactivity_ed_price);
        et_password = (EditText) findViewById(R.id.postactivity_ed_password);
        et_phone = (EditText) findViewById(R.id.postactivity_ed_phone);
        buttonSubmit = (Button)findViewById(R.id.postactivity_button_post);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView.setVerticalScrollBarEnabled(false);
        setupRegionSpinner();
        setupCategorySpinner();
        buttonSubmit.setOnClickListener(new MyButtonPostListener());
        postImage = (ImageView) findViewById(R.id.postactivity_imageview);
        Uri extras = getIntent().getParcelableExtra("extras");
        int imageFrom = getIntent().getIntExtra("image_from", 0);
        getImage(extras, imageFrom);
    }

    private void getImage(Uri uri, int imageFrom) {
        if (imageFrom == 50)
            imagePath = getRealPathFrom(uri);
        else if (imageFrom == 51)
            imagePath = uri.getPath();

        Bitmap bitmap = decodeFile(imagePath);
        postImage.setImageBitmap(bitmap);
        bitmapPost = bitmap;
    }

    public void setupCategorySpinner() {
        //get items menu from parse
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                ParseQuery query = new ParseQuery("Category");
                return query;
            }
        };
        // then feed to a SpinnerAdapter <--array or CursorAdapter <-- database
        categoryAdapter = new MyParseAdapter(getApplicationContext(), factory);
        categoryAdapter.setTextKey("name");

        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(1);
        categorySpinner.setOnItemSelectedListener(new CategorySpinnerListener());
    }

    public void setupRegionSpinner() {
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Region");
                return query;
            }
        };

        regionAdapter = new MyParseAdapter(getApplicationContext(), factory);
        regionAdapter.setTextKey("name");
        regionSpinner.setAdapter(regionAdapter);
        regionSpinner.setSelection(1);
        regionSpinner.setOnItemSelectedListener(new RegionSpinnerListener());
    }

    class RegionSpinnerListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ParseObject parseObject = regionAdapter.getItem(position);
            regionObject = parseObject;
            // change color

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class CategorySpinnerListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ParseObject parseObject = categoryAdapter.getItem(position);
            categoryObject = parseObject;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    class MyButtonPostListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (et_desc.getText().toString().isEmpty() ||
                    et_phone.getText().toString().isEmpty() ||
                    et_password.getText().toString().isEmpty() ||
                    et_price.getText().toString().isEmpty() ||
                    et_title.getText().toString().isEmpty()) {
                Toast.makeText(PostActivity.this, "Please fill into all fields.", Toast.LENGTH_SHORT).show();
            } else {
                dialog = new ProgressDialog(PostActivity.this);
                dialog.setTitle("Loading");
                dialog.setMessage("Please wait..");
                dialog.show();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapPost.compress(Bitmap.CompressFormat.JPEG, 75, stream);

                final ParseFile parseFile = new ParseFile(stream.toByteArray());
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (e == null) {
                            ParseObject object = new ParseObject("Product");
                            object.put("title", et_title.getText().toString());
                            object.put("price", Double.valueOf(et_price.getText().toString()));
                            object.put("description", et_desc.getText().toString());
                            object.put("password", et_password.getText().toString());
                            object.put("category_object", categoryObject);
                            object.put("region_object", regionObject);
                            object.put("phone", Double.valueOf(et_phone.getText().toString()));
                            object.put("image", parseFile);
                            object.put("country", "Malaysia");
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (dialog.isShowing()) dialog.dismiss();
                                    if (e == null) {
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
                                        query.orderByDescending("updatedAt");
                                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject object, ParseException e) {
                                                if (object == null) {
                                                    Log.d("product", "The getFirst request failed.");
                                                } else {
                                                    Intent i = new Intent(PostActivity.this, ActivityItemDetails.class);
                                                    i.putExtra("title", object.getString("title"));
                                                    i.putExtra("price", object.getString("price"));
                                                    i.putExtra("description", object.getString("description"));
                                                    i.putExtra("phone", object.getNumber("phone").toString());
                                                    i.putExtra("date", object.getCreatedAt().toString());
                                                    i.putExtra("objectId", object.getObjectId());
                                                    ParseFile file = object.getParseFile("image");
                                                    i.putExtra("url", file.getUrl());
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        });
                                        Toast.makeText(PostActivity.this, "Post has been submitted", Toast.LENGTH_LONG).show();
                                    } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                                        Toast.makeText(PostActivity.this, "Please check internet connection.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(PostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public Bitmap decodeFile(String imagePath){
        try {
            // ori size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);

            final int REQUIRED_SIZE = 160;

            // new size
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *=2;

            // with insamplesize
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = scale;
            return BitmapFactory.decodeFile(imagePath, options1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRealPathFrom(Uri uri){
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            int col_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return  cursor.getString(col_index);
        } else {
            return null;
        }
    }
}
