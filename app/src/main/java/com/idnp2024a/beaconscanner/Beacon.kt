package com.idnp2024a.beaconscanner

class Beacon(mac: String?) {
    val macAddress = mac
    var manufacturer: String? = null
    var rssi: Int? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Beacon) return false
        if (macAddress != other.macAddress) return false
        return true
    }
    override fun hashCode(): Int {
        return macAddress?.hashCode() ?: 0
    }
}