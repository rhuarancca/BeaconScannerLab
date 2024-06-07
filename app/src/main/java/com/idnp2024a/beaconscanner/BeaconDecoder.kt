package com.idnp2024a.beaconscanner

import android.util.Log
import android.widget.TextView

    object BeaconDecoder {

        fun decodeiBeacon(data: ByteArray, rssi: Int?, txtMessage: TextView, TAG: String) {
            val data_len = Integer.parseInt(Utils.toHexString(data.copyOfRange(0, 1)), 16)
            val data_type = Integer.parseInt(Utils.toHexString(data.copyOfRange(1, 2)), 16)
            val LE_flag = Integer.parseInt(Utils.toHexString(data.copyOfRange(2, 3)), 16)
            val len = Integer.parseInt(Utils.toHexString(data.copyOfRange(3, 4)), 16)
            val type = Integer.parseInt(Utils.toHexString(data.copyOfRange(4, 5)), 16)
            val company = Utils.toHexString(data.copyOfRange(5, 7))
            val subtype = Integer.parseInt(Utils.toHexString(data.copyOfRange(7, 8)), 16)
            val subtypelen = Integer.parseInt(Utils.toHexString(data.copyOfRange(8, 9)), 16)
            val iBeaconUUID = Utils.toHexString(data.copyOfRange(9, 25))
            val major = Integer.parseInt(Utils.toHexString(data.copyOfRange(25, 27)), 16)
            val minor = Integer.parseInt(Utils.toHexString(data.copyOfRange(27, 29)), 16)
            val txPower = Integer.parseInt(Utils.toHexString(data.copyOfRange(29, 30)), 16)

            val factor = (-1 * txPower - rssi!!) / (10 * 4.0)
            val distance = Math.pow(10.0, factor)

            val display = "TxPower:$txPower \nRSSI:$rssi \nRSSI2:rssi2 \nDistance:$distance"
            txtMessage.text = display

            Log.d(
                TAG,
                "DECODE data_len:$data_len data_type:$data_type LE_flag:$LE_flag len:$len type:$type subtype:$subtype subtype_len:$subtypelen company:$company UUID:$iBeaconUUID major:$major minor:$minor txPower:$txPower"
            )
        }
    }