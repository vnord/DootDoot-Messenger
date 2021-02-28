package com.example.dootdootmessenger

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class DeviceListActivity : AppCompatActivity() {

    private lateinit var listPairedDevices: ListView
    private lateinit var listAvailableDevices: ListView
    private lateinit var adapterPairedDevices: ArrayAdapter<String>
    private lateinit var adapterAvailableDevices: ArrayAdapter<String>

    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)
        init()
    }

    private fun init() {
        listPairedDevices = findViewById(R.id.list_paired_devices)
        listAvailableDevices = findViewById(R.id.list_available_devices)

        adapterPairedDevices = ArrayAdapter(this, R.layout.device_list_item)
        adapterAvailableDevices = ArrayAdapter(this, R.layout.device_list_item)

        listPairedDevices.adapter = adapterPairedDevices
        listAvailableDevices.adapter = adapterAvailableDevices

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.bondedDevices.forEach {
            adapterPairedDevices.add(
                it.name + "\n" + it.address
            )
        }

    }
}