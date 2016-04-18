package edu.stanford.tmowlett.smartbike;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ControlActivity extends AppCompatActivity {
    ImageButton unlockButton;
    Button historyButton;
    ToggleButton modeToggle;
    ToggleButton stateToggle;
    TextView connectionTV;
    TextView bikeIDTV;

    //Bluetooth Stuff
    private BluetoothAdapter bTAdapter;
    private final static String TAG = ControlActivity.class.getSimpleName();
    private String btMAC;
    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID TX_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID RX_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    public UUID CLIENT_UUID = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb");
    public BluetoothGatt mGatt;
    BluetoothGattCharacteristic tx;
    BluetoothGattCharacteristic rx;
    public byte data[] = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

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
                createUnlockDialog();
            }
        });

        modeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data[0] |= 1;
                } else {
                    data[0] &= ~1;
                }
                Log.i(TAG, "Mode Toggled");
                tx.setValue(data);
                mGatt.writeCharacteristic(tx);
            }
        });

        stateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data[0] |= 2;
                } else {
                    data[0] &= ~2;
                }
                Log.i(TAG, "Mode Toggled");
                tx.setValue(data);
                mGatt.writeCharacteristic(tx);
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

        final EditText inputMAC = new EditText(this);
        inputMAC.setText("DE:B6:93:09:4F:17");
        dialogLayout.addView(inputMAC, params);
        unlockBuilder.setView(dialogLayout);

        unlockBuilder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check if there is text in the box
                if (!(inputMAC.getText().toString().equals(""))) {
                    connectBT();
                    btMAC = inputMAC.getText().toString();
                    Toast.makeText(ControlActivity.this, inputMAC.getText(), Toast.LENGTH_SHORT).show();
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

    private void connectBT() {
        bTAdapter = BluetoothAdapter.getDefaultAdapter();
        bTAdapter.startLeScan(mScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i(TAG, "Address Discovered " + device.getAddress());
            if (device.getAddress().equals(btMAC)) {
                mGatt = device.connectGatt(ControlActivity.this, false, mgGattCallback);
                setConnectionTV(getResources().getString(R.string.connection_state_connecting));
                setBikeIDTV(btMAC);
            }
        }
    };

    private BluetoothGattCallback mgGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.i(TAG, "Profile Connected");
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (!gatt.discoverServices()) {
                        Log.i(TAG, "Discover Services failed");
                    }
                    Log.i(TAG, "GATT Success!");
                    bTAdapter.stopLeScan(mScanCallback);
                } else {
                    Log.i(TAG, "GATT unsuccessful!");
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Currently Disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.i(TAG, "Bluetooth Gatt failure");
                return;
            }
            tx = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);
            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);

            if (!gatt.setCharacteristicNotification(rx, true)) {
                Log.i(TAG, "Unable to set rx characteristic notification");
                return;
            }

            /* CODE to get device descriptor
            for (BluetoothGattDescriptor descriptor:tx.getDescriptors()){
                Log.i(TAG, "BluetoothGattDescriptor: "+descriptor.getUuid().toString());
            }
            */

            BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);

            if (desc == null) {
                Log.i(TAG, "Descriptor is null");
                return;
            }

        desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

            if (!gatt.writeDescriptor(desc)) {
                Log.i(TAG, "Descriptor write failed");
                return;
            }

            Log.i(TAG, "Seems to be set up correctly");
            setConnectionTV(getResources().getString(R.string.connection_state_connected));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, characteristic.getStringValue(0));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i(TAG, "Writing new characteristic");
        }
    };

    public void disconnect() {
        if (mGatt != null) {
            mGatt.disconnect();
        }
        setConnectionTV(getResources().getString(R.string.connection_state_disconnected));
        mGatt = null;
        tx = null;
        rx = null;
    }
}
