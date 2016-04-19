package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ControlActivity extends AppCompatActivity {
    private final String TAG = ControlActivity.class.getSimpleName();
    private final String DEVICE_MAC = "DE:B6:93:09:4F:17";
    ImageButton unlockButton;
    Button historyButton;
    ToggleButton modeToggle;
    ToggleButton stateToggle;
    TextView connectionTV;
    TextView bikeIDTV;
    SBbluetooth btController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        btController = new SBbluetooth(ControlActivity.this);

        //Create unlock button object
        unlockButton = (ImageButton)findViewById(R.id.unlock_button);
        historyButton = (Button)findViewById(R.id.ride_history_button);
        modeToggle = (ToggleButton)findViewById(R.id.light_mode_switch);
        stateToggle = (ToggleButton)findViewById(R.id.light_state_switch);
        connectionTV = (TextView)findViewById(R.id.connection_state);
        bikeIDTV = (TextView)findViewById(R.id.bike_id);

        // Set up on click listener for unlock button
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btController.isConnected()) {
                    createUnlockDialog();
                } else {
                    btController.disconnect();
                }
            }
        });

        modeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btController.updateTxBit(0,true);
                } else {
                    btController.updateTxBit(0,false);
                }
                Log.i(TAG, "Mode Toggled");
            }
        });

        stateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btController.updateTxBit(1,true);
                } else {
                    btController.updateTxBit(1,false);
                }
                Log.i(TAG, "Mode Toggled");
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

    @Override
    protected void onStop() {
        super.onStop();
        btController.disconnect();
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

        final EditText inputMAC = new EditText(this);
        inputMAC.setText(DEVICE_MAC);
        dialogLayout.addView(inputMAC, params);
        unlockBuilder.setView(dialogLayout);

        unlockBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check if there is text in the box
                if (!(inputMAC.getText().toString().equals(""))) {
                    btController.connectBT(inputMAC.getText().toString());
                    //Toast.makeText(ControlActivity.this, inputMAC.getText(), Toast.LENGTH_SHORT).show();
                    String newMac = getResources().getString(R.string.bike_id) + inputMAC.getText().toString();
                    bikeIDTV.setText(newMac);
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
        //BT code here:
    }

    public void setConnectionTV(final String ConnectionStateIn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectionTV.setText(ConnectionStateIn);
            }
        });
    }

    public void setBikeIDTV(final String iDIn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String displayText = getResources().getString(R.string.bike_id) + iDIn;
                bikeIDTV.setText(displayText);
            }
        });
    }

    public void updateLockIcon() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!btController.isConnected()) {
                    unlockButton.setImageResource(R.drawable.unlock);
                } else {
                    unlockButton.setImageResource(R.drawable.lock);
                }
            }
        });
    }
}
