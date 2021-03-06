package com.stone.zxingscandemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.zxing.client.android.CaptureActivity
import com.google.zxing.client.android.Intents
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.longToast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan.setOnClickListener { startScan() }
    }

    private fun startScan() {
//        此处关于权限的处理，在你应用时建议切换为 封装好的权限兼容库，官方提供的权限API在国产ROM上存在兼容性问题。
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(act)
                        .setTitle("Tips: ")
                        .setMessage("please give me the permission")
                        .setPositiveButton("ok") { _, _ ->
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                        }
                        .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        } else {
            startCapture()
        }

    }

    private fun startCapture() {
        //直接唤起，默认是内部处理解析结果
//            startActivity(Intent(this, CaptureActivity::class.java))

        //通过action参数唤起，将解析结果返回外部处理
        val intent = Intent(this, CaptureActivity::class.java)
        intent.action = Intents.Scan.ACTION
        startActivityForResult(intent, REQ_SCAN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        接收解析结果
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_SCAN_CODE) {
            val codeResult = data?.getStringExtra(Intents.Scan.RESULT)
            longToast(codeResult!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        处理相机权限的问题
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCapture()
        } else if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "request camara permission fail!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 0x1
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0x2

        private const val REQ_SCAN_CODE = 0x10
    }
}
