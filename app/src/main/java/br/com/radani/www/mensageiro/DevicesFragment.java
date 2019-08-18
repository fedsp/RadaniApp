package br.com.radani.www.mensageiro;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import br.com.radani.www.mensageiro.R;

/**
 * show list of BLE devices

public class DevicesFragment extends Activity {

    private final BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver bleDiscoveryBroadcastReceiver;
    private IntentFilter bleDiscoveryIntentFilter;
    List<BluetoothDevice> deviceList;
    Map<String, Integer> devRssiValues;
    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();
    private ArrayAdapter<BluetoothDevice> listAdapter;

    public DevicesFragment() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bleDiscoveryBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device.getType() != BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                        runOnUiThread(() -> updateScan(device));
                    }
                }
                if(intent.getAction().equals((BluetoothAdapter.ACTION_DISCOVERY_FINISHED))) {
                    stopScan();
                }
            }
        };
        bleDiscoveryIntentFilter = new IntentFilter();
        bleDiscoveryIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bleDiscoveryIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(null);
        setListAdapter(listAdapter);
        listAdapter = new ArrayAdapter<BluetoothDevice>(this, 0, listItems) {
            BluetoothDevice device = listItems.get(position);
        };
        setContentView(R.layout.content_device_list);
    }



    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(bleDiscoveryBroadcastReceiver, bleDiscoveryIntentFilter);
        if(bluetoothAdapter == null || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            Toast.makeText(getApplicationContext(), "<Bluetooth LE não suportado>", Toast.LENGTH_SHORT).show();

        else if(!bluetoothAdapter.isEnabled())
            Toast.makeText(getApplicationContext(), "<Bluetooth está desligado>", Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(getApplicationContext(), "<use SCAN para pesquisar novos dispositivos>", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
    }

    private void startScan() {
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(deviceAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getText(R.string.location_permission_title));
                builder.setMessage(getText(R.string.location_permission_message));
                builder.setPositiveButton(android.R.string.ok,
                        (dialog, which) -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0));
                builder.show();
                return;
            }
        }
        listItems.clear();
        listAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "<Escaneando>", Toast.LENGTH_SHORT).show();

        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // ignore requestCode as there is only one in this fragment
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new Handler(Looper.getMainLooper()).postDelayed(this::startScan,1); // run after onResume to avoid wrong empty-text
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getText(R.string.location_denied_title));
            builder.setMessage(getText(R.string.location_denied_message));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void updateScan(BluetoothDevice device) {
        if(listItems.indexOf(device) < 0) {
            listItems.add(device);
            Collections.sort(listItems, DevicesFragment::compareTo);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void stopScan() {
        Toast.makeText(getApplicationContext(), "<Nenhum dispositivo encontrado>", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.cancelDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(ListView l, View v, int position, long id) {
            stopScan();
            BluetoothDevice device = listItems.get(position - 1);
            Bundle args = new Bundle();
            args.putString("device", device.getAddress());
        }



        public int compareTo(BluetoothDevice a, BluetoothDevice b) {
            boolean aValid = a.getName() != null && !a.getName().isEmpty();
            boolean bValid = b.getName() != null && !b.getName().isEmpty();
            if (aValid && bValid) {
                int ret = a.getName().compareTo(b.getName());
                if (ret != 0) return ret;
                return a.getAddress().compareTo(b.getAddress());
            }
            if (aValid) return -1;
            if (bValid) return +1;
            return a.getAddress().compareTo(b.getAddress());
        }

    }
 */