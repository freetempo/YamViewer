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
import android.widget.EditText;

import com.freetempo.yamviewer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView userNameEdit;
    private Button submitButton, cleatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
    }

    private void getViews() {
        initAutoCompleteEditText();
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        cleatButton = findViewById(R.id.clear_button);
        cleatButton.setOnClickListener(this);
    }

    private void initAutoCompleteEditText() {
        userNameEdit = findViewById(R.id.user_name);
        List<String> list = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.frequent_use_user_name)));
        userNameEdit.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list));

        userNameEdit.setCompletionHint(getResources().getString(R.string.pop_user_name));
        userNameEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEdit.showDropDown();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_button) {
            String userName = userNameEdit.getText().toString();
            // check input strings
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToast(this, "帳號不可為空");
                return;
            }
            AlbumListActivity.launch(this, userName);
        } else if (view.getId() == R.id.clear_button) {
            // clear edit text
            userNameEdit.setText("");
        }
    }
}
