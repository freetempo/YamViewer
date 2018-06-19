package com.freetempo.yamviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.freetempo.yamviewer.db.AppDatabase;
import com.freetempo.yamviewer.db.SearchHistoryDao;
import com.freetempo.yamviewer.db.SearchHistoryEntity;
import com.freetempo.yamviewer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView userNameEdit;
    private Button submitButton, cleatButton, clearHistoryButton;
    private SearchHistoryDao searchHistoryDao;
    private List<String> searchHistoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchHistoryDao = AppDatabase.getInstance(this).searchHistoryDao();
        getSearchDataFromDb();
        getViews();
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
        for (int i = 0; i < list.size(); i++) {
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
}
