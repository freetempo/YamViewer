package com.freetempo.yamviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.freetempo.yamviewer.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userNameEdit;
    private Button submitButton, cleatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
    }

    private void getViews() {
        userNameEdit = findViewById(R.id.user_name);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        cleatButton = findViewById(R.id.clear_button);
        cleatButton.setOnClickListener(this);
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
