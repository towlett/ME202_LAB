package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity {
    ImageButton unlockButton;
    Button historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        //Create unlock button object
        unlockButton = (ImageButton)findViewById(R.id.unlock_button);
        historyButton = (Button)findViewById(R.id.ride_history_button);

        // Set up on click listener for unlock button
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUnlockDialog();
            }
        });

        // Set up on click listener for ride history button
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHistory = new Intent(ControlActivity.this, RideHistoryActivity.class);
                startActivity(goToHistory);
            }
        });
    }

    //Function to build the unlock dialog box
    private void createUnlockDialog() {
        AlertDialog.Builder unlockBuilder = new AlertDialog.Builder(ControlActivity.this);
        unlockBuilder.setTitle(R.string.unlock_dialog_message);

        // Use a linear layout to put margins on the edittext box
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 0, 40, 0);

        final EditText input = new EditText(this);
        dialogLayout.addView(input, params);
        unlockBuilder.setView(dialogLayout);

        unlockBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check if there is text in the box
                if (!(input.getText().toString().equals(""))) {
                    //Do Unlock Here
                    Toast.makeText(ControlActivity.this, input.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ControlActivity.this, R.string.blank_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        unlockBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel Unlock
                dialog.cancel();
            }
        });

        AlertDialog unlockDialog = unlockBuilder.create();
        unlockDialog.show();
    }
}
