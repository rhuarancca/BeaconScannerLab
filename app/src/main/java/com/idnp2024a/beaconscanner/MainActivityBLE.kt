package com.idnp2024a.beaconscanner

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.idnp2024a.beaconscanner.permissions.BTPermissions
import com.idnp2024a.beaconscanner.permissions.PermissionManager


class MainActivityBLE : AppCompatActivity() {

    private val TAG: String = "MainActivityBLE"
    private var alertDialog: AlertDialog? = null
    private lateinit var txtMessage: TextView
    private val permissionManager = PermissionManager.from(this)
    private lateinit var bluetoothController: BTController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ble)

        txtMessage = findViewById(R.id.txtMessage)
        bluetoothController = BTController(this, txtMessage)

        BTPermissions(this).checkPermissions()
        bluetoothController.initBluetooth()

        val btnAdversting = findViewById<Button>(R.id.btnAdversting)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnAdversting.setOnClickListener {
        }

        btnStart.setOnClickListener {
            if (bluetoothController.isLocationEnabled()) {
                bluetoothController.bluetoothScanStart()
            } else {
                showPermissionDialog()
            }
        }
        btnStop.setOnClickListener {
            bluetoothController.bluetoothScanStop()
        }
    }

    private fun showPermissionDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
            .setMessage("El servicio de localizacion no esta activo")
            .setPositiveButton("Close") { dialog, which ->
                dialog.dismiss()
            }
        if (alertDialog == null) {
            alertDialog = builder.create()
        }

        if (!alertDialog!!.isShowing()) {
            alertDialog!!.show()
        }
    }
}