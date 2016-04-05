package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
                createUnlockDialog();
            }
        });
    }

    private void createUnlockDialog() {
        AlertDialog.Builder unlockBuilder = new AlertDialog.Builder(ControlActivity.this);
        unlockBuilder.setMessage(R.string.unlock_dialog_message);
        final EditText input = new EditText(this);
        unlockBuilder.setView(input);

        unlockBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do Unlock Here
                Toast.makeText(ControlActivity.this, input.getText(), Toast.LENGTH_SHORT).show();
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
