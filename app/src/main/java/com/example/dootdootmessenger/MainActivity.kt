package com.example.dootdootmessenger

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val LOCATION_PERMISSION_REQUEST: Int = 88
private const val SELECT_DEVICE: Int = 420

class MainActivity : AppCompatActivity() {

    private val context: Context = this

    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search_devices -> {
                enableBluetooth()
                checkPermissions()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissions() {
        //TODO: Use companion thing on newer APIs
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("We be needin' that permission famalam")
                .setPositiveButton("Grant") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST
                    )
                }
                .setNegativeButton("Deny") { _, _ ->
                    this.finish()
                }.show()
        } else {
            startActivityForResult(Intent(this, DeviceListActivity::class.java), SELECT_DEVICE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_DEVICE && resultCode == RESULT_OK) {
            val address = data!!.getStringExtra("deviceAddress")
            Toast.makeText(context, "Address: $address", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    startActivityForResult(
                        Intent(this, DeviceListActivity::class.java),
                        SELECT_DEVICE
                    )
                } else {
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("You didn't give us location access, bye lol")
                        .setPositiveButton("Bye!") { _, _ ->
                            this.finish()
                        }.show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(context, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            bluetoothAdapter.enable()
        }
    }
}