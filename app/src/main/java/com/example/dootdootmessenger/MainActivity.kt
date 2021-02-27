package com.example.dootdootmessenger

import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_devices -> {
                enableBluetooth()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(context, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            bluetoothAdapter.enable()
        }
    }
}