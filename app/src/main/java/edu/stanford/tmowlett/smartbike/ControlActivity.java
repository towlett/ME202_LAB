package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity {
    ImageButton unlockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        //Create unlock button object
        unlockButton = (ImageButton)findViewById(R.id.unlock_button);

        // Set up on click listener
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExampleDialog();
            }
        });
    }

    private void createExampleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ControlActivity.this);
        builder.setMessage("Enter Unique ID");
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do Unlock
                Toast.makeText(ControlActivity.this, "Unlocked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel Unlock
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
