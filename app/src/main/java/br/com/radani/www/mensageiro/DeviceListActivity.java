package br.com.radani.www.mensageiro;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * show list of BLE devices
 */
public class DeviceListActivity  extends ListActivity {

    private final BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver bleDiscoveryBroadcastReceiver;
    private IntentFilter bleDiscoveryIntentFilter;
    private ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public DeviceListActivity() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_item);
        adapter = new ArrayAdapter<>(this, R.layout.device_list_items, listItems);
        setListAdapter(adapter); //Fill List
        Toast.makeText(getApplicationContext(),"Inicializando", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_devices, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(bleDiscoveryBroadcastReceiver, bleDiscoveryIntentFilter);
        if(mBluetoothAdapter == null || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            Toast.makeText(getApplicationContext(),"Bluetooth LE não suportado", Toast.LENGTH_SHORT).show();
        else if(!mBluetoothAdapter.isEnabled())
            Toast.makeText(getApplicationContext(), "Por favor ative o bluetooth antes de iniciar o APP", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Acesse o menu-> SCAN para encontrar novos dispositivos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
        this.unregisterReceiver(bleDiscoveryBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ble_scan) {
            startScan();
            return true;
        } else if (id == R.id.ble_scan_stop) {
            stopScan();
            return true;
        } else if (id == R.id.bt_settings) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void startScan() {
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
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Escaneando", Toast.LENGTH_SHORT).show();
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            new Handler(Looper.getMainLooper()).postDelayed(this::startScan,1); // run after onResume to avoid wrong empty-text
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getText(R.string.location_denied_title));
            builder.setMessage(getText(R.string.location_denied_message));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
    }

    private void updateScan(BluetoothDevice device) {
        if(listItems.indexOf(device) < 0) {
            listItems.add(device.getAddress() + "\n" + device.getName());
            adapter.notifyDataSetChanged();
            Log.i("Endereço", device.getAddress());
            Log.i("Nome", device.getName());
            Log.i("Nome", listItems.toString());
        }
    }

    private void stopScan() {
    //    Toast.makeText(getApplicationContext(),"Nenhum dispositivo encontrado", Toast.LENGTH_SHORT).show();
        mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        stopScan();
        String adress = listItems.get(position);
        adress = adress.substring(0, 17);
        Bundle args = new Bundle();
        args.putString("Endereço", adress);
        Toast.makeText(getApplicationContext(), adress, Toast.LENGTH_SHORT).show();
        Bundle b = new Bundle();
        b.putString(BluetoothDevice.EXTRA_DEVICE, adress);
        Intent result = new Intent();
        result.putExtras(b);
        Intent vaipraleitura = new Intent(DeviceListActivity.this, leitura.class);
        startActivity(vaipraleitura);
    }

}
