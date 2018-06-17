package com.freetempo.yamviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseAlbumActivity extends AppCompatActivity {

    private static final String TAG = "BrowseAlbumActivity";

    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_ALBUM_ID = "album_id";

    private String userName, albumId;
    private int currentPage = 1;

    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private BrowsePhotoAdapter adapter;

    private boolean isLastPage = false;

    public static void launch(Context context, String userName, String albumId) {
        Intent intent = new Intent(context, BrowseAlbumActivity.class);
        intent.putExtra(KEY_USER_NAME, userName);
        intent.putExtra(KEY_ALBUM_ID, albumId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_album);

        initPageButtons();

        recyclerView = findViewById(R.id.browse_album_recycler_view);
        adapter = new BrowsePhotoAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        userName = intent.getStringExtra(KEY_USER_NAME);
        albumId = intent.getStringExtra(KEY_ALBUM_ID);

        requestQueue = Volley.newRequestQueue(this);

        getPhotos(currentPage);
    }

    private void initPageButtons() {
        final Button preButton = findViewById(R.id.btn_previous_page);
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage <= 1) {
                    Toast.makeText(preButton.getContext(), "已經在第1頁",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getPhotos(--currentPage);
                }
            }
        });

        final Button nextButton = findViewById(R.id.btn_next_page);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLastPage) {
                    Toast.makeText(nextButton.getContext(), "已在最後一頁(" + currentPage + ")",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getPhotos(++currentPage);
                }
            }
        });

    }

    private void getPhotos(final int page) {
        StringRequest request = new StringRequest(Request.Method.POST,
                getAlbumUrl(userName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parsePhotos(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("albumId", albumId);
                map.put("page", Integer.toString(page));
                return map;
            }
        };
        requestQueue.add(request);
        String pageNow
                = new StringBuilder("第").append(Integer.toString(page)).append("頁").toString();
        Toast.makeText(this, pageNow, Toast.LENGTH_SHORT).show();
    }

    private void parsePhotos(JSONObject rawObject) {
        int code = rawObject.optInt("code");
        JSONArray photos = rawObject.optJSONArray("photos");
        int page = rawObject.optInt("page");
        isLastPage = rawObject.optBoolean("lastPage");

        if (code != 200) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append(code).append(": ").append(rawObject.optString("message"));
            Log.e(TAG, "get photos error: " + errorMessage.toString());
            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        List<PhotoItem> list = new ArrayList<>();
        for (int i = 0; i < photos.length(); i++) {
            PhotoItem item = new PhotoItem(photos.optJSONObject(i));
            list.add(item);
        }
        adapter.updatePhotoList(list);
        recyclerView.smoothScrollToPosition(0);
    }


    private String getAlbumUrl(String userName) {
        StringBuilder builder =
                new StringBuilder("https://").append(userName)
                        .append(".tian.yam.com/ajax/album/fetch");
        return builder.toString();
    }

    private JSONObject getPostObject(String albumId, int page) {
        JSONObject object = new JSONObject();
        try {
            object.put("albumId", albumId);
            object.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    private static class BrowsePhotoAdapter extends RecyclerView.Adapter<PhotoItemHolder> {

        private List<PhotoItem> photoList;
        private Context context;


        BrowsePhotoAdapter(Context context) {
            this.context = context;
        }

        public void updatePhotoList(List<PhotoItem> list) {
            photoList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PhotoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_browse_photo, parent, false);
            return new PhotoItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoItemHolder holder, int position) {
            holder.bind(photoList.get(position));
        }

        @Override
        public int getItemCount() {
            if (photoList != null) {
                return photoList.size();
            }
            return 0;
        }
    }


}
