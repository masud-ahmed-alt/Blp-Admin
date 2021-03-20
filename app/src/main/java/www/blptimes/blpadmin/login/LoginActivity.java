package www.blptimes.blpadmin.login;

import androidx.appcompat.app.AppCompatActivity;
import www.blptimes.blpadmin.MainActivity;
import www.blptimes.blpadmin.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText userId, password;
    private Button loginBtn;

    private String user, pass;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin","false").equals("yes")){
            openDashboard();
        }

        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.btnLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        user = userId.getText().toString();
        pass = password.getText().toString();

        if (user.isEmpty()){
            userId.setError("Required");
            userId.requestFocus();
        }else if (pass.isEmpty()){
            password.setError("Required");
            password.requestFocus();
        }else if (user.equals("masud")&&pass.equals("masud")){
            openDashboard();
        }else {
            Toast.makeText(this, "Check Id and Password", Toast.LENGTH_LONG).show();
        }
    }

    private void openDashboard() {
        editor.putString("isLogin","yes");
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}