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

import com.google.firebase.database.FirebaseDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.freetempo.yamviewer.db.AppDatabase;
import com.freetempo.yamviewer.db.SearchHistoryEntity;
import com.freetempo.yamviewer.utils.ToastUtil;
import com.freetempo.yamviewer.utils.UrlParsingUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.freetempo.yamviewer.MainActivity.FIREBASE_KEY;

public class AlbumListActivity extends AppCompatActivity {

    public static final String TAG = "AlbumListActivity";

    private static final String KEY_USER_NAME = "user_name";

    private String userName;
    private String albumStatus = "passwd";
    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private AlbumListAdapter adapter;

    private int currentPage = 1;
    private boolean isLastPage = false;


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

        // handle deep link
        if (userName == null) {
            userName = UrlParsingUtil.getUserName(getIntent().getData());
            // save to db
            SearchHistoryEntity entity = new SearchHistoryEntity();
            entity.userName = userName;
            AppDatabase.getInstance(this).searchHistoryDao().insertSearchHistory(entity);
            // save to firebase
            FirebaseDatabase.getInstance().getReference(FIREBASE_KEY).child(String.valueOf(System.currentTimeMillis()))
                    .child("name").setValue(userName);
        }

        initPageButtons();
        initAlbumTypeButtons();

        recyclerView = findViewById(R.id.album_list_recycler_view);
        adapter = new AlbumListAdapter(this);
        // grid layout
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        // linear layout
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        getAlbumList(currentPage);
    }

    private void initPageButtons() {
        final Button preButton = findViewById(R.id.btn_previous_page);
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage <= 1) {
                    Context context = preButton.getContext();
                    ToastUtil.showToast(context, context.getString(R.string.already_first_page));
                } else {
                    getAlbumList(--currentPage);
                }
            }
        });

        final Button nextButton = findViewById(R.id.btn_next_page);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLastPage) {
                    Context context = nextButton.getContext();
                    ToastUtil.showToast(context, String.format(context.getString(
                            R.string.already_last_page), currentPage));
                } else {
                    getAlbumList(++currentPage);
                }
            }
        });

    }

    private void initAlbumTypeButtons() {
        Button allButton = findViewById(R.id.btn_all_albums);
        Button openButton = findViewById(R.id.btn_open_albums);
        Button lockedButton = findViewById(R.id.btn_locked_albums);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewId = view.getId();
                if (viewId == R.id.btn_all_albums) {
                    albumStatus = "all";
                } else if (viewId == R.id.btn_open_albums) {
                    albumStatus = "open";
                } else if (viewId == R.id.btn_locked_albums) {
                    albumStatus = "passwd";
                }
                currentPage = 1;
                getAlbumList(currentPage);
            }
        };

        allButton.setOnClickListener(onClickListener);
        openButton.setOnClickListener(onClickListener);
        lockedButton.setOnClickListener(onClickListener);
    }

    private void getAlbumList(int page) {
        JsonObjectRequest request = new JsonObjectRequest(createUrlString(userName, page),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                adapter.updateList(parseAlbums(response));
                recyclerView.smoothScrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onError: " + error.toString());
            }
        }
        );

        requestQueue.add(request);
        ToastUtil.showToast(this, String.format(getString(R.string.page_number), page));
    }

    private List<AlbumInfo> parseAlbums(JSONObject rawObject) {
        int code = rawObject.optInt("code");
        if (code != 200) {
            Log.e(TAG, "parse album error. code: " + code);
        }

        JSONObject dataObject = rawObject.optJSONObject("data");
        List<AlbumInfo> list = new ArrayList<>();
        JSONObject viewCountObject = dataObject.optJSONObject("pv");

        JSONArray albumArray = dataObject.optJSONArray("albums");
        for (int i = 0; i < albumArray.length(); i++) {
            AlbumInfo albumInfo = new AlbumInfo(albumArray.optJSONObject(i));
            // set username due to no user name field in the JSON
            albumInfo.setUserName(userName);
            // set view count
            albumInfo.setViewCount(Integer.parseInt(viewCountObject.optString(albumInfo.getId())));
            list.add(albumInfo);
        }
        isLastPage = dataObject.optBoolean("lastPage");
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
