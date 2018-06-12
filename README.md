
# ZxingScanDemo

## 关于ZXing

ZXing是Google提供的条形码、二维码等的生成、解析的开源库。

[ZXing GitHub传送门](https://github.com/zxing/zxing)

## 配置

gradle
```
implementation 'compile 'com.sqq.xiaqu:zxing-android:1.0.0'
```

## 使用说明

`zxing-android`将zxing的应用代码单独抽离出来了，内有`CaptureActivity`用以扫描二维码并解析出来。`HistoryActivity`扫描的历史记录。

具体使用的参考代码：
```kotlin
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
```


## 关于代码`zxing-android`

模块`zxing-android`中的代码绝大部分都是`zxing`开源库中的`android`目录下的源码。是对zxing:core核心库的应用。

备注：此部分的代码集成，是从`incubator-weex`中android模块的代码搬运过来的，其中的二维码就是拉取的`zxing`的源码，故此说明一下。

[incubator-weex](https://github.com/apache/incubator-weex/tree/master/android)

### 关于`incubator-weex`

原阿里项目`weex`,现由Apache维护的跨平台开发技术。这里的`incubator-weex`是提供关于`weex`开发介绍预览等功能的一个APP源码`weex-playground`。




