package edu.stanford.tmowlett.smartbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final static String HARDCODE_USERNAME = "PasswordIsTaco";
    private final static String HARDCODE_PASSWORD = "Taco";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameIn = ((EditText)findViewById(R.id.username)).getText().toString();
                String passwordIn = ((EditText)findViewById(R.id.password)).getText().toString();

                if (userNameIn.equals(HARDCODE_USERNAME) && passwordIn.equals(HARDCODE_PASSWORD)) {
                    //move to control activity
                    Intent goToControl = new Intent(LoginActivity.this, ControlActivity.class);
                    startActivity(goToControl);
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
