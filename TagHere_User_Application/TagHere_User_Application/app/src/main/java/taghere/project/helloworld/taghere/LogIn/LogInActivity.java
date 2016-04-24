package taghere.project.helloworld.taghere.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import taghere.project.helloworld.taghere.GoogleMap.GoogleMapActivity;
import taghere.project.helloworld.taghere.R;

public class LogInActivity extends AppCompatActivity {
    //임시 아이디와 패스워드
    private String storeId = "abc";
    private String storePassword = "111";

    //입력된 아이디와 패스워드
    private EditText editId;
    private EditText editPassword;
    private String id;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editId = (EditText)findViewById(R.id.IdEditText);
        editPassword = (EditText)findViewById(R.id.PasswordEditText);
    }

    public void onClickLoginButton(View v) {
        id = editId.getText().toString();
        password = editPassword.getText().toString();

        System.out.print(id + password + editId + editPassword);

        if (id.equals(storeId) && password.equals(storePassword)) {
            Intent loginIntent = new Intent(LogInActivity.this, GoogleMapActivity.class);
            startActivity(loginIntent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
        }
    }
}