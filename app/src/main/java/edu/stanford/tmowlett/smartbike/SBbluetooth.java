package edu.stanford.tmowlett.smartbike;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.UUID;

public class SBbluetooth {
    //Bluetooth Module Variables
    private BluetoothAdapter bTAdapter;
    private final static String TAG = SBbluetooth.class.getSimpleName();
    private String btMAC;
    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID TX_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID RX_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public BluetoothGatt mGatt;
    BluetoothGattCharacteristic tx;
    BluetoothGattCharacteristic rx;
    public byte txData[] = {0};
    public Context context;

    // Constructor
    public SBbluetooth(Context contextin) {
        context = contextin;
    }

    // Connect function - Create bTAdapter
    public void connectBT(String macIn) {
        btMAC = macIn;
        bTAdapter = BluetoothAdapter.getDefaultAdapter();
        bTAdapter.startLeScan(mScanCallback);
    }

    // LE Scan Callback
    private BluetoothAdapter.LeScanCallback mScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i(TAG, "Address Discovered " + device.getAddress());
            if (device.getAddress().equals(btMAC)) {
                mGatt = device.connectGatt(context, false, mgGattCallback);
                ((ControlActivity)context).setConnectionTV(context.getResources().getString(R.string.connection_state_connecting));
            }
        }
    };

    // Gatt Callback
    private BluetoothGattCallback mgGattCallback = new BluetoothGattCallback() {
        @Override
        // Changed connection state
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.i(TAG, "Profile Connected");
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // If successfully connected go to discover services
                    if (!gatt.discoverServices()) {
                        Log.i(TAG, "Discover Services failed");
                    }
                    Log.i(TAG, "GATT Success!");
                    bTAdapter.stopLeScan(mScanCallback);
                } else {
                    Log.i(TAG, "GATT unsuccessful!");
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Make sure it is really disconnected
                disconnect();
                Log.i(TAG, "Currently Disconnected");
            }
        }

        // On services discovered function
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.i(TAG, "Bluetooth Gatt failure");
                return;
            }
            // Get tx and rx characteristics
            tx = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);
            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);

            //Set up characteristic notifications for rx
            if (!gatt.setCharacteristicNotification(rx, true)) {
                Log.i(TAG, "Unable to set rx characteristic notification");
                return;
            }

            BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);

            if (desc == null) {
                Log.i(TAG, "Descriptor is null");
                return;
            }

            //Set descriptor value
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

            if (!gatt.writeDescriptor(desc)) {
                Log.i(TAG, "Descriptor write failed");
                return;
            }

            Log.i(TAG, "Seems to be set up correctly");

            // Write current tx
            tx.setValue(txData);
            mGatt.writeCharacteristic(tx);

            // Update UI
            ((ControlActivity)context).setConnectionTV(context.getResources().getString(R.string.connection_state_connected));
            ((ControlActivity)context).updateLockIcon();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, "RX value " + characteristic.getValue()[0]);
            if (characteristic.equals(rx)) {
                if (characteristic.getValue()[0] == 49) {
                    ((ControlActivity)context).setMovementTV(true);
                } else {
                    ((ControlActivity)context).setMovementTV(false);
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i(TAG, "Writing new characteristic");
        }
    };

    // Method to check if the BT is fully connected
    public boolean isConnected() {
        return (mGatt != null && tx != null);
    }

    // Method to update a certain bit in TX and send if connected
    public void updateTxBit(int pos, boolean value){
        if (value) {
            txData[0] |= (1 << pos);
        } else {
            txData[0] &= ~(1 << pos);
        }

        if (isConnected()) {
            tx.setValue(txData);
            mGatt.writeCharacteristic(tx);
        }
    }

    // Disconnect method
    public void disconnect() {
        //Disconnect
        if (mGatt != null) {
            mGatt.close();
        }

        mGatt = null;
        tx = null;
        rx = null;

        // Update UI
        ((ControlActivity) context).setConnectionTV(context.getResources().getString(R.string.connection_state_disconnected));
        ((ControlActivity) context).updateLockIcon();
        ((ControlActivity) context).setBikeIDTV("");
    }
}
