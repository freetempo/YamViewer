package com.freetempo.yamviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freetempo.yamviewer.db.AppDatabase;
import com.freetempo.yamviewer.db.SearchHistoryDao;
import com.freetempo.yamviewer.db.SearchHistoryEntity;
import com.freetempo.yamviewer.utils.ToastUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener /* for firebase */  /* , ChildEventListener */ {

    private AutoCompleteTextView userNameEdit;
    private Button submitButton, cleatButton, clearHistoryButton, pcHomeButton;
    private SearchHistoryDao searchHistoryDao;
    private List<String> searchHistoryList;

    public static final String FIREBASE_KEY = "searched";

    private DatabaseReference dbReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchHistoryDao = AppDatabase.getInstance(this).searchHistoryDao();
        dbReference = FirebaseDatabase.getInstance().getReference(FIREBASE_KEY);

        getSearchDataFromDb();
        getViews();
        
        // getFromFireBase();
        dynamicTest();
        dynamicTestAndroid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSearchDataFromDb();
        resetAutoCompleteAdapter();
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(userNameEdit.getText().toString())) {
            userNameEdit.setText("");
        } else {
            super.onBackPressed();
        }
    }

    private void getSearchDataFromDb() {
        searchHistoryList = new ArrayList<>();
        List<SearchHistoryEntity> list = searchHistoryDao.loadAllSearchHistory();
        for (int i = list.size() - 1; i >= 0; i--) {
            searchHistoryList.add(list.get(i).userName);
        }
    }

    private void getViews() {
        initAutoCompleteEditText();
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        cleatButton = findViewById(R.id.clear_button);
        cleatButton.setOnClickListener(this);
        clearHistoryButton = findViewById(R.id.clear_history_button);
        clearHistoryButton.setOnClickListener(this);
        pcHomeButton = findViewById(R.id.pchome_button);
        pcHomeButton.setOnClickListener(this);
    }

    private void initAutoCompleteEditText() {
        userNameEdit = findViewById(R.id.user_name);
        resetAutoCompleteAdapter();

        userNameEdit.setCompletionHint(getResources().getString(R.string.search_history_list));
        userNameEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameEdit.getAdapter().getCount() > 0) {
                    userNameEdit.showDropDown();
                }
            }
        });
        userNameEdit.setThreshold(1);
    }

    private void resetAutoCompleteAdapter() {
        userNameEdit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, searchHistoryList));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_button) {
            String userName = userNameEdit.getText().toString();
            // check input strings
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToast(this, getString(R.string.user_name_cant_be_empty));
                return;
            }
            SearchHistoryEntity entity = new SearchHistoryEntity();
            entity.userName = userName;
            searchHistoryDao.insertSearchHistory(entity);
            // save user name to firebase db
            saveToFireBase(userName);
            AlbumListActivity.launch(this, userName);
        } else if (view.getId() == R.id.clear_button) {
            // clear edit text
            userNameEdit.setText("");
        } else if (view.getId() == R.id.clear_history_button) {
            searchHistoryDao.clearAllSearchHistory();
            getSearchDataFromDb();
            resetAutoCompleteAdapter();
        } else if (view.getId() == R.id.pchome_button) {
            startActivity(new Intent(this, PCHomeActivity.class));
        }
    }

    private void saveToFireBase(String userName) {
        // use timestamp as unique key id
        long timeStamp = System.currentTimeMillis();
        dbReference.push().child("name").setValue(userName);
    }

    // https://Ffreetempo.page.link
    // test for firebase dynamic link
    // AIzaSyB1yDmmqS-C9ZAfGYxotaR15O_lt-SA99o
    private void dynamicTest() {
        String url = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=AIzaSyB1yDmmqS-C9ZAfGYxotaR15O_lt-SA99o";
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("longDynamicLink", "https://freetempo.page.link/?link=https://www.example.com/&apn=com.example.android&ibi=com.example.ios");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Larry test", "onResponse: " + response);
            }
        },  new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Larry test", "onErrorResponse: " + error.getMessage() + " " + error.toString());
                Log.d("Larry test", "onErrorResponse: " + error.networkResponse.data.toString());
            }
        } )
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("longDynamicLink", "https://example.page.link/?link=https://www.example.com/&apn=com.example.android&ibi=com.example.ios");
////                map.put("page", Integer.toString(page));
//                return map;
//            }ㄢ
//        }
        ;

        Volley.newRequestQueue(this).add(request);
    }

    private void dynamicTestAndroid() {
        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDynamicLinkDomain("https://freetempo.page.link/");
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.android").build())
//                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build());
        Log.d("Larry test", "buildDynamicLink(): " + builder.buildDynamicLink().getUri().toString());


        builder.buildShortDynamicLink().addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
            @Override
            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                if (task.isSuccessful()) {
                    Log.d("Larry test", "buildShortDynamicLink(): " + task.getResult().getShortLink());
                    Log.d("Larry test", "buildShortDynamicLink() 2: " + task.getResult().getPreviewLink());
                } else {
                    Log.d("Larry test", "not successful: ");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Larry test", "onFail: " + e.getLocalizedMessage().toString());
            }
        });

//        Log.d("Larry test", "buildShortDynamicLink(): " + builder.buildShortDynamicLink());
    }



    // firebase functions

    //    private void getFromFireBase() {
//        // get an entity one time, multiple times in onChildAdded()
//        dbReference.addChildEventListener(this);
//        // get all entities at one time in onDataChange()
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("Larry test", "onDataChange: " + dataSnapshot.getChildrenCount());
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("Larry test", "snapshot: " + snapshot.child("name").getValue());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("Larry test", "onCancelled");
//            }
//        });
//    }

//    @Override
//    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, /* number of entity? */ @Nullable String s) {
//        Log.d("Larry test", "onChildAdded :" + s);
//        String got = String.valueOf(dataSnapshot.child("name").getValue());
//        Log.d("Larry test", "getChildrenCount: " + dataSnapshot.getChildrenCount());
//        Log.d("Larry test", "onChildAdded got :" + got);
//    }
//
//    @Override
//    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//        Log.d("Larry test", "onChildChanged :" + s);
//        String got = String.valueOf(dataSnapshot.child("name").getValue());
//        Log.d("Larry test", "onChildChanged got :" + got);
//    }
//
//    @Override
//    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//        Log.d("Larry test", "onChildRemoved");
//        String got = String.valueOf(dataSnapshot.child("name").getValue());
//        Log.d("Larry test", "onChildRemoved got :" + got);
//    }
//
//    @Override
//    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//        Log.d("Larry test", "onChildMoved" + s);
//        String got = String.valueOf(dataSnapshot.child("name").getValue());
//        Log.d("Larry test", "onChildMoved got :" + got);
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//        Log.d("Larry test", "DatabaseError");
//        String got = String.valueOf(databaseError.getMessage());
//        Log.d("Larry test", "DatabaseError got :" + got);
//    }



}
