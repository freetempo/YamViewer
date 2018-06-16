package com.freetempo.yamviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumListActivity extends AppCompatActivity {

    public static final String TAG = "AlbumListActivity";

    private static final String KEY_USER_NAME = "user_name";

    private String userName;
    private String albumStatus = "passwd";
    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private AlbumListAdapter adapter;

    // launch this activity
    public static void launch(Context context, String userName) {
        Intent intent = new Intent(context, AlbumListActivity.class);
        intent.putExtra(KEY_USER_NAME, userName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        userName = getIntent().getStringExtra(KEY_USER_NAME);
        Log.d("Larry test", "AlbumListActivity onCreate: " + userName);

        recyclerView = findViewById(R.id.album_list_recycler_view);
        adapter = new AlbumListAdapter(this);
        // grid layout
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        // linear layout
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        getAlbumList();
    }

    private void getAlbumList() {
        JsonObjectRequest request = new JsonObjectRequest(createUrlString(userName, 1),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Larry test", "onResponse: " + response.toString());
                adapter.updateList(parseAlbums(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Larry test", "onError: " + error.toString());
            }
        }
        );

        requestQueue.add(request);
    }

    private List<AlbumInfo> parseAlbums(JSONObject rawObject) {
        int code = rawObject.optInt("code");
        if (code != 200) {
            Log.e(TAG, "parse album error. code: " + code);
        }

        List<AlbumInfo> list = new ArrayList<>();

        JSONArray albumArray = rawObject.optJSONObject("data").optJSONArray("albums");
        for (int i = 0; i < albumArray.length(); i++) {
//            Log.d("Larry test", "album " + i + " " +
//                    albumArray.optJSONObject(i).optString("name"));
            AlbumInfo albumInfo = new AlbumInfo(albumArray.optJSONObject(i));
            list.add(albumInfo);

            Log.d("Larry test", "album name: " + albumInfo.getName());
            Log.d("Larry test", "cover: " + albumInfo.getCover());
        }
        return list;
    }


    private String createUrlString(String user, int page) {
        StringBuilder builder = new StringBuilder();
        builder.append("https://")
                .append(user)
                .append(".tian.yam.com/ajax/album/fetch-albums?status=")
                .append(albumStatus)
                .append("&page=")
                .append(Integer.toString(page));
        Log.d("Larry test", "createUrlString: " + builder.toString());
        return builder.toString();
    }


    private static class AlbumListAdapter extends RecyclerView.Adapter<AlbumItemHolder> {

        private List<AlbumInfo> albumInfoList;
        private Context context;

        public AlbumListAdapter(Context context) {
            this.context = context;
            albumInfoList = new ArrayList<>();
        }

        public void updateList(List<AlbumInfo> newList) {
            albumInfoList = newList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AlbumItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_album_list, parent, false);
            return new AlbumItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumItemHolder holder, int position) {
            holder.bind(albumInfoList.get(position));
        }

        @Override
        public int getItemCount() {
            if (albumInfoList != null) {
                return albumInfoList.size();
            }
            return 0;
        }
    }
}
