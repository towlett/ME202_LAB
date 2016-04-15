package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Class Variables
    Button loginButton;
    Button registerButton;
    EditText userNameEditText;
    EditText passwordEditText;
    private Map<String,String> LoginCreds=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create login button Object
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        // Add default username/password
        LoginCreds.put(getString(R.string.hardcode_username), getString(R.string.hardcode_password));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // Function for Login Button Click
            public void onClick(View view) {
                // Get username and password strings
                String userNameIn = userNameEditText.getText().toString();
                String passwordIn = passwordEditText.getText().toString();

                // Compare inputted username and password to actual
                if (LoginCreds.containsKey(userNameIn) && passwordIn.equals(LoginCreds.get(userNameIn))) {
                    // Move to control activity
                    Intent goToControl = new Intent(LoginActivity.this, ControlActivity.class);
                    startActivity(goToControl);
                    finish();
                } else {
                    // Authentication failed
                    Toast.makeText(LoginActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRegisterDialog();
            }
        });
    }

    private void createRegisterDialog() {
        AlertDialog.Builder registerBuilder = new AlertDialog.Builder(LoginActivity.this);
        registerBuilder.setTitle(R.string.register_dialog_message);

        // Use a linear layout to put margins on the edittext boxes
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 0, 40, 0);

        final EditText newUsername = new EditText(this);
        final EditText newPassword = new EditText(this);
        newPassword.setInputType(129);
        dialogLayout.addView(newUsername, params);
        dialogLayout.addView(newPassword, params);
        registerBuilder.setView(dialogLayout);

        registerBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check if there is text in the box
                if (!(newUsername.getText().toString().equals("")||(newPassword.getText().toString().equals("")))) {
                    //Do Unlock Here
                    if (LoginCreds.containsKey(newUsername.getText().toString())) {
                        Toast.makeText(LoginActivity.this, R.string.un_taken, Toast.LENGTH_SHORT).show();
                    } else {
                        LoginCreds.put(newUsername.getText().toString(), newPassword.getText().toString());
                        Toast.makeText(LoginActivity.this, R.string.login_added, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, R.string.blank_un_pw, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel Unlock
                dialog.cancel();
            }
        });

        AlertDialog unlockDialog = registerBuilder.create();
        unlockDialog.show();
    }

}
