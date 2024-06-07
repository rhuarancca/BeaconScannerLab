package com.idnp2024a.beaconscanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.Context
import android.location.LocationManager
import android.util.Log
import android.widget.TextView
import androidx.core.location.LocationManagerCompat
import com.idnp2024a.beaconscanner.permissions.Permission
import com.idnp2024a.beaconscanner.permissions.PermissionManager

class BTController(private val context: Context, private val txtMessage: TextView) {
    private val TAG: String = "BluetoothController"
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var btScanner: BluetoothLeScanner
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val permissionManager = PermissionManager.from(context)

    fun initBluetooth() {
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter != null) {
            btScanner = bluetoothAdapter.bluetoothLeScanner
        } else {
            Log.d(TAG, "BluetoothAdapter is null")
        }
    }

    fun bluetoothScanStart() {
        Log.d(TAG, "btScan ...1")
        if (btScanner != null) {
            Log.d(TAG, "btScan ...2")
            permissionManager
                .request(Permission.Location)
                .rationale("Bluetooth permission is needed")
                .checkPermission { isgranted ->
                    if (isgranted) {
                        btScanner.startScan(BleScanCallback(
                            onScanResultAction,
                            onBatchScanResultAction,
                            onScanFailedAction
                        ))
                    } else {
                        Log.d(TAG, "Alert you don't have Bluetooth permission")
                    }
                }
        } else {
            Log.d(TAG, "btScanner is null")
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    @SuppressLint("MissingPermission")
    fun bluetoothScanStop() {
        Log.d(TAG, "btScan ...1")
        if (btScanner != null) {
            Log.d(TAG, "btScan ...2")
            btScanner.stopScan(BleScanCallback(
                onScanResultAction,
                onBatchScanResultAction,
                onScanFailedAction
            ))
        } else {
            Log.d(TAG, "btScanner is null")
        }
    }

    @SuppressLint("MissingPermission")
    private val onScanResultAction: (ScanResult?) -> Unit = { result ->
        val scanRecord = result?.scanRecord
        val beacon = Beacon(result?.device?.address)
        beacon.manufacturer = result?.device?.name
        beacon.rssi = result?.rssi
        if (scanRecord != null && beacon.manufacturer == "ESP32 Beacon") {
            scanRecord.bytes?.let { BeaconDecoder.decodeiBeacon(it, beacon.rssi, txtMessage, TAG) }
        }
    }

    private val onBatchScanResultAction: (MutableList<ScanResult>?) -> Unit = {
        if (it != null) {
            Log.d(TAG, "BatchScanResult $it")
        }
    }

    private val onScanFailedAction: (Int) -> Unit = {
        Log.d(TAG, "ScanFailed $it")
    }
}