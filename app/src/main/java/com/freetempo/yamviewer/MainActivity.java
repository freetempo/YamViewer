package com.freetempo.yamviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userNameEdit;
    private Button submitButton;

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
    }


    @Override
    public void onClick(View view) {
        String userName = userNameEdit.getText().toString();
        // check input strings
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "帳號不可為空", Toast.LENGTH_SHORT).show();
            return;
        }

        AlbumListActivity.launch(this, userName);
    }
}
