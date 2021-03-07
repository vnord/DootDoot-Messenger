package com.example.dootdootmessenger

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class DeviceListActivity : AppCompatActivity() {

    private val context: Context = this

    private lateinit var listPairedDevices: ListView
    private lateinit var listAvailableDevices: ListView
    private lateinit var progressScanDevices: ProgressBar


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
        progressScanDevices = findViewById(R.id.progress_scan_devices)

        adapterPairedDevices = ArrayAdapter(this, R.layout.device_list_item)
        adapterAvailableDevices = ArrayAdapter(this, R.layout.device_list_item)

        listPairedDevices.adapter = adapterPairedDevices
        listAvailableDevices.adapter = adapterAvailableDevices

        listAvailableDevices.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, _, _ ->
                val info = (view as TextView).text.toString()
                val address = info.substring(info.length - 17)

                setResult(RESULT_OK, Intent().putExtra("deviceAddress", address))
                finish()
            }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.bondedDevices.forEach {
            adapterPairedDevices.add(
                it.name + "\n" + it.address
            )
        }

        registerReceiver(
            bluetoothDeviceListener,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        registerReceiver(
            bluetoothDeviceListener,
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        )

    }

    private val bluetoothDeviceListener: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if (device!!.bondState != BluetoothDevice.BOND_BONDED) {
                        adapterAvailableDevices.add(device.name + "\n" + device.address)
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    progressScanDevices.visibility = View.GONE
                    if (adapterPairedDevices.count == 0) {
                        Toast.makeText(context, "No new devices found", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Click on a device to pair", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_device_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_scan_devices -> {
                scanDevices()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun scanDevices() {
        progressScanDevices.visibility = View.VISIBLE
        adapterAvailableDevices.clear()

        Toast.makeText(this, "Scan started", Toast.LENGTH_SHORT).show()

        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        bluetoothAdapter.startDiscovery()
    }
}