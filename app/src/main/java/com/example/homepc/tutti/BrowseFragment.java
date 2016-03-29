package com.example.homepc.tutti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by HomePC on 28/3/2016.
 */
public class BrowseFragment extends Fragment implements RecyclerViewAdapter.ClickListener{

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    ArrayList<ItemsModel> data = new ArrayList<>();
    ProgressDialog dialog;
    final private int PICK_IMAGE = 50;
    final private int CAPTURE_IMAGE = 51;

    private static final String IMAGE_DIRECTORY = "My tutti";
    private Uri fileUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.browse_fragment_layout, container, false);

        setupFloatingActionMenu(getActivity());
        recyclerView = (RecyclerView) v.findViewById(R.id.browsefragment_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerView(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                int limit = current_page * 5;
                loadMoreItems(limit);
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    // setup floatingactionmenu
    private void setupFloatingActionMenu(Context context){
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.plus_icon);

        // create button to attach to menu
        FloatingActionButton actionButton = new FloatingActionButton.Builder((Activity) context)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_button_action_blue)
                .build();

        // create menu items
        ImageView capture = new ImageView(context);
        capture.setImageResource(R.drawable.camera_icon);

        final ImageView upload = new ImageView(context);
        upload.setImageResource(R.drawable.share_icon);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder((Activity) context)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_button_action_blue));

        SubActionButton buttonCapture = itemBuilder.setContentView(capture).build();
        SubActionButton buttonUpload = itemBuilder.setContentView(upload).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder((Activity)context)
                .addSubActionView(buttonCapture)
                .addSubActionView(buttonUpload)
                .enableAnimations()
                .attachTo(actionButton)
                .build();

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


    }

    private void captureImage() {

        File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);

        if (!fileDir.exists()) {
            if (!fileDir.mkdir()) {
                Toast.makeText(getContext(), "Fail to create directory", Toast.LENGTH_SHORT).show();
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHMMSS", Locale.getDefault()).format(new Date());

        File mediaFile = new File(fileDir.getPath() + File.separator + "IMG_" + timeStamp +".jpg");

        fileUri = Uri.fromFile(mediaFile);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(i, CAPTURE_IMAGE);
    }

    private void uploadImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Pick image"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            Uri path;
            int type;
            if (requestCode == PICK_IMAGE) {
                path = data.getData();
                type = 50;
            } else {
                path = fileUri;
                type = 51;
            }
            Intent i = new Intent(getActivity(), PostActivity.class);
            i.putExtra("extras", path);
            i.putExtra("image_from", type);
            startActivity(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setOnScrollListener(new EndlessRecyclerView(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                int limit = current_page * 5;
                loadMoreItems(limit);
            }
        });
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putParcelable("file_uri", fileUri);
        }
    }

    private void onRestoreInstanceState(Bundle inState) {
        if (inState != null){
            fileUri = inState.getParcelable("file_uri");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(getActivity(), ActivityItemDetails.class);
        ItemsModel model = data.get(position);
        i.putExtra("price", model.getPrice());
        i.putExtra("date", model.getDate());
        i.putExtra("title", model.getTitle());
        i.putExtra("desc", model.getDesc());
        i.putExtra("phone", model.getPhone());
        i.putExtra("url", model.getImageFile());
        i.putExtra("objectId", model.getObjectId());
        startActivity(i);
    }

    private void loadMoreItems(int threshold) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
        query.setLimit(threshold).orderByAscending("createdAt");
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait..");
        dialog.show();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                dialog.dismiss();
                if (e == null) {
                    data.clear();
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        ItemsModel model = new ItemsModel();
                        model.setDate(String.valueOf(object.getCreatedAt().toLocaleString()));
                        model.setPrice(object.getNumber("price").toString());
                        model.setPhone(object.getNumber("phone").toString());
                        model.setDesc(object.getString("desc"));
                        model.setTitle(object.getString("title"));
                        model.setImageFile(object.getParseFile("image").getUrl());
                        model.setObjectId(object.getObjectId());

                        data.add(model);
                    }
                    adapter.updateData(data);
                } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
        query.setLimit(5).orderByAscending("createdAt");
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait..");
        dialog.show();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                dialog.dismiss();
                if (e == null) {
                    data.clear();
                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject object = objects.get(i);
                        ItemsModel model = new ItemsModel();
                        model.setDate(String.valueOf(object.getCreatedAt().toLocaleString()));
                        model.setPrice(object.getNumber("price").toString());
                        model.setPhone(object.getNumber("phone").toString());
                        model.setDesc(object.getString("desc"));
                        model.setTitle(object.getString("title"));
                        model.setImageFile(object.getParseFile("image").getUrl());
                        model.setObjectId(object.getObjectId());

                        data.add(model);
                    }

                    adapter = new RecyclerViewAdapter(data, getActivity());
                    adapter.setClickListener(BrowseFragment.this);

                    recyclerView.setAdapter(adapter);

                } else if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
