package beacon.com.beaconapp.fragment;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.ProgressBar;

import java.util.HashSet;
import java.util.Set;

import beacon.com.beaconapp.R;
import beacon.com.beaconapp.adapter.BeaconAdapter;
import beacon.com.beaconapp.model.BeaconItem;


public class BeaconFragment extends Fragment {

    private static final String TAG = BeaconFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private BeaconAdapter beaconAdapter;
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private ProgressBar progressBar;
    FloatingActionButton scanButton;
    CountDownTimer countDownTimer;
    Set<String> scannedBeacons;


    public BeaconFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beacon, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.beaconList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        beaconAdapter = new BeaconAdapter(getContext());
        recyclerView.setAdapter(beaconAdapter);

        scanButton = (FloatingActionButton) rootView.findViewById(R.id.scanBeacons);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Searching nearby beacons", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startScanning();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    private void startScanning() {
        // init BLE
        scannedBeacons = new HashSet<>();
        btManager = (BluetoothManager) getContext().getSystemService(getContext().BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                scanButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                btAdapter.stopLeScan(leScanCallback);

            }
        };
        scanButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        btAdapter.startLeScan(leScanCallback);
        countDownTimer.start();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5) {
                if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound) {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid =  hexString.substring(0,8) + "-" +
                        hexString.substring(8,12) + "-" +
                        hexString.substring(12,16) + "-" +
                        hexString.substring(16,20) + "-" +
                        hexString.substring(20,32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                Log.i(TAG, "UUID: " + uuid + "\\nmajor: " + major + "\\nminor" + minor + " RSSI: " + rssi);

                getBeaconInfoAndAdd(new BeaconItem(uuid, rssi, "Google", "This is google address", "https://www.google.com"));
            }

        }
    };

    private void getBeaconInfoAndAdd(BeaconItem item) {
        if(scannedBeacons.contains(item.getBeaconId())) {
            return;
        }
        scannedBeacons.add(item.getBeaconId());
        beaconAdapter.add(item);
        recyclerView.invalidate();

    }

    /**
     * bytesToHex method
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
