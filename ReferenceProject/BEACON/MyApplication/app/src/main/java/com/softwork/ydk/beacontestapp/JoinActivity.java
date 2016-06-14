package com.softwork.ydk.beacontestapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.softwork.ydk.beacontestapp.Server.ServerManager;

public class JoinActivity extends Activity {
    private EditText idEditText;
    private EditText passwordEditText;
    private EditText nicknameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idEditText = (EditText)findViewById(R.id.id_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);
        nicknameEditText = (EditText)findViewById(R.id.nickname_edit_text);
    }

    public void join(View v) {
        ServerManager.getInstance().requestJoinToServer(
                idEditText.getText().toString(),
                passwordEditText.getText().toString(),
                nicknameEditText.getText().toString()
        );

        if(ServerManager.getInstance().getResult() == ServerManager.ACCEPT) {
            Toast.makeText(this, "가입되었습니다!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "가입되지 않았습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void cancel(View v) {
        finish();
    }
}
