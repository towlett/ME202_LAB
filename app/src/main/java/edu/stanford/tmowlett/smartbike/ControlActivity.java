package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
    }

    public void unlockBike(View view) {
        Toast.makeText(ControlActivity.this, "Unlock Pressed", Toast.LENGTH_SHORT).show();
    }

}
