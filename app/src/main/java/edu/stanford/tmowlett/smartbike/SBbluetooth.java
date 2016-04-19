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
    public byte data[] = {0};
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
                disconnect();
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

            /* CODE to get device descriptor
            for (BluetoothGattDescriptor descriptor:tx.getDescriptors()){
                Log.i(TAG, "BluetoothGattDescriptor: "+descriptor.getUuid().toString());
            }
            */

            if (!gatt.setCharacteristicNotification(rx, true)) {
                Log.i(TAG, "Unable to set rx characteristic notification");
                return;
            }

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

            ((ControlActivity)context).setConnectionTV(context.getResources().getString(R.string.connection_state_connected));
            ((ControlActivity)context).updateLockIcon();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, "RX value" + characteristic.getStringValue(0));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i(TAG, "Writing new characteristic");
        }
    };

    public boolean isConnected() {
        return (mGatt != null && tx != null);
    }

    public void updateTxBit(int pos, boolean value){
        if (value) {
            data[0] |= (1 << pos);
        } else {
            data[0] &= ~(1 << pos);
        }

        if (isConnected()) {
            tx.setValue(data);
            mGatt.writeCharacteristic(tx);
        }
    }

    public void disconnect() {
        if (mGatt != null) {
            mGatt.disconnect();
            mGatt.close();
        }

        mGatt = null;
        tx = null;
        rx = null;

        ((ControlActivity) context).setConnectionTV(context.getResources().getString(R.string.connection_state_disconnected));
        ((ControlActivity) context).updateLockIcon();
    }
}
