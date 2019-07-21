package br.com.radani.www.mensageiro;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

public class DeviceListActivity2 extends Activity {

    private final String TAG = DeviceListActivity.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private TextView mEmptyList;
    List<BluetoothDevice> deviceList;
    private DeviceListActivity.DeviceAdapter deviceAdapter;
    Map<String, Integer> devRssiValues;
    private static final long SCAN_PERIOD = 10000; //10 seconds
    private Handler mHandler;
    private boolean mScanning;

}
