package taghere.project.helloworld.taghere.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import taghere.project.helloworld.taghere.AccountFolder.Account;
import taghere.project.helloworld.taghere.R;
import taghere.project.helloworld.taghere.Menu.SelectMenuActivity;

public class LogInActivity extends AppCompatActivity {
    //임시 아이디와 패스워드
    private Account account;

    //입력된 아이디와 패스워드
    private EditText editId;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editId = (EditText)findViewById(R.id.IdEditText);
        editPassword = (EditText)findViewById(R.id.PasswordEditText);
        account = new Account(editId.getText().toString(), editPassword.getText().toString());
    }

    public void onClickLoginButton(View v) {
        Intent selectIntent = new Intent(LogInActivity.this, SelectMenuActivity.class);
        startActivity(selectIntent);
        LogInActivity.this.finish();
    }

    public void onClickMemberButton(View v) {
        Intent createAccountIntent = new Intent(LogInActivity.this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
    }
}