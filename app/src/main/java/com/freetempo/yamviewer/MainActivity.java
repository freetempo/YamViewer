package com.freetempo.yamviewer;

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

import com.freetempo.yamviewer.db.AppDatabase;
import com.freetempo.yamviewer.db.SearchHistoryDao;
import com.freetempo.yamviewer.db.SearchHistoryEntity;
import com.freetempo.yamviewer.utils.ToastUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener {

    private AutoCompleteTextView userNameEdit;
    private Button submitButton, cleatButton, clearHistoryButton;
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
        
        getFromFireBase();
    }

    private void getFromFireBase() { ;
        // get an entity one time, multiple times in onChildAdded()
        dbReference.addChildEventListener(this);
        // get all entities at one time in onDataChange()
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Larry test", "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Larry test", "snapshot: " + snapshot.child("name").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Larry test", "onCancelled");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSearchDataFromDb();
        resetAutoCompleteAdapter();
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
        }
    }

    private void saveToFireBase(String userName) {
        // use timestamp as unique key id
        long timeStamp = System.currentTimeMillis();
        Log.d("Larry test", "save to firebase: " + timeStamp +" - " + userName);
        dbReference.child(String.valueOf(timeStamp)).child("name").setValue(userName);
    }

    // firebase functions
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, /* number of entity? */ @Nullable String s) {
        Log.d("Larry test", "onChildAdded :" + s);
        String got = String.valueOf(dataSnapshot.child("name").getValue());
        Log.d("Larry test", "getChildrenCount: " + dataSnapshot.getChildrenCount());
        Log.d("Larry test", "onChildAdded got :" + got);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d("Larry test", "onChildChanged :" + s);
        String got = String.valueOf(dataSnapshot.child("name").getValue());
        Log.d("Larry test", "onChildChanged got :" + got);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d("Larry test", "onChildRemoved");
        String got = String.valueOf(dataSnapshot.child("name").getValue());
        Log.d("Larry test", "onChildRemoved got :" + got);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d("Larry test", "onChildMoved" + s);
        String got = String.valueOf(dataSnapshot.child("name").getValue());
        Log.d("Larry test", "onChildMoved got :" + got);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.d("Larry test", "DatabaseError");
        String got = String.valueOf(databaseError.getMessage());
        Log.d("Larry test", "DatabaseError got :" + got);
    }
}
