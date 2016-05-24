package taghere.project.helloworld.taghere.LogIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import taghere.project.helloworld.taghere.R;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText idEdit;
    private EditText passwordEdit;
    private EditText nameEdit;
    private EditText phoneNumberEdit;
    private EditText emailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //editText들 설정
        idEdit = (EditText)findViewById(R.id.createIDEditText);
        passwordEdit = (EditText)findViewById(R.id.createPasswordEditText);
        nameEdit = (EditText)findViewById(R.id.createNameEditText);
        phoneNumberEdit = (EditText)findViewById(R.id.createPhoneEditText);
        emailEdit = (EditText)findViewById(R.id.createEmailEditText);
    }

    public void onClickNewAccountButton(View v) {
        //디비에서 아이디가 겹치는지 확인 후 계정 생성하고 디비에 추가
        Intent LoginIntent = new Intent(CreateAccountActivity.this, LogInActivity.class);
        startActivity(LoginIntent);
        CreateAccountActivity.this.finish();
    }
}
