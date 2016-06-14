package com.softwork.ydk.beacontestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.softwork.ydk.beacontestapp.FloorPlanList.FloorPlanListActivity;
import com.softwork.ydk.beacontestapp.Server.ServerManager;

public class LoginActivity extends Activity {
    private EditText idEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEditText = (EditText) findViewById(R.id.idEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void login(View v) {
        String nickname = ServerManager.getInstance().requestLoginToServer(
                idEditText.getText().toString(),
                passwordEditText.getText().toString()
        );

        if(ServerManager.getInstance().getResult() == ServerManager.ACCEPT) {
            ServerManager.getInstance().setUserNickName(nickname);
            Intent floorPlanListActivity = new Intent(this, FloorPlanListActivity.class);
            startActivity(floorPlanListActivity);
            finish();
        } else {
            Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show();
        }
    }

    public void join(View v) {
        Intent joinActivity = new Intent(this, JoinActivity.class);
        startActivity(joinActivity);
    }
}
