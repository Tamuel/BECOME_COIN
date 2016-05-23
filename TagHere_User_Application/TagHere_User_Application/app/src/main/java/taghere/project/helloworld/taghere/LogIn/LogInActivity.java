package taghere.project.helloworld.taghere.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import taghere.project.helloworld.taghere.R;
import taghere.project.helloworld.taghere.Menu.SelectMenuActivity;

public class LogInActivity extends AppCompatActivity {
    //임시 아이디와 패스워드
    private HashMap<String, String> accountHash = new HashMap<String, String>();

//    private String storeId = "abc";
//    private String storePassword = "111";

    //입력된 아이디와 패스워드
    private EditText editId;
    private EditText editPassword;
    private String id;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //등록된 아이디들 hashmap에 저장
        accountHash.put("abc", "111");

        editId = (EditText)findViewById(R.id.IdEditText);
        editPassword = (EditText)findViewById(R.id.PasswordEditText);
    }

    public void onClickLoginButton(View v) {
        id = editId.getText().toString();
        password = editPassword.getText().toString();

        System.out.print(id + password + editId + editPassword);

        if (accountHash.containsKey(id)) {
            if (password.equals(accountHash.get(id))) {
                Intent menuIntent = new Intent(LogInActivity.this, SelectMenuActivity.class);
                startActivity(menuIntent);
            }
            else {
                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "없는 계정입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickMemberButton(View v) {
        Intent createAccountIntent = new Intent(LogInActivity.this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
    }
}