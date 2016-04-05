package edu.stanford.tmowlett.smartbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText userNameEditText;
    EditText passwordEditText;
    private final static String HARDCODE_USERNAME = "PasswordIsTaco";
    private final static String HARDCODE_PASSWORD = "Taco";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create login button Object
        loginButton = (Button)findViewById(R.id.login_button);
        userNameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override

            // Function for Login Button Click
            public void onClick(View view) {
                // Get username and password strings

                String userNameIn = userNameEditText.getText().toString();
                String passwordIn = passwordEditText.getText().toString();

                // Compare inputted username and password to actual
                if (userNameIn.equals(HARDCODE_USERNAME) && passwordIn.equals(HARDCODE_PASSWORD)) {
                    // Move to control activity
                    Intent goToControl = new Intent(LoginActivity.this, ControlActivity.class);
                    startActivity(goToControl);
                } else {
                    // Authentication failed
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
