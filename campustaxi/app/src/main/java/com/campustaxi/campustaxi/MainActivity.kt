package com.campustaxi.campustaxi

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//웹뷰 추가 라이브러
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import java.io.File
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.WebChromeClient.FileChooserParams

import android.webkit.ValueCallback

import android.webkit.WebChromeClient
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import android.os.Build
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.DialogInterface
import android.provider.Settings

import androidx.annotation.NonNull
import android.os.Parcelable

import androidx.core.content.FileProvider










class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 웹뷰 설정 시작
        var myWebView: WebView = findViewById(R.id.webView)
        myWebView.webViewClient = WebViewClient()
        var mWebSettings = myWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        var dir : File = getCacheDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mWebSettings.setAppCachePath(dir.getPath());
        mWebSettings.setAppCacheEnabled(true);
        // 웹뷰 설정 끝

        // 이미지 첨부 설정 시작
        myWebView.setWebChromeClient(object : WebChromeClient() {
            // For Android 5.0+
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                // Callback 초기화 (중요!)
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop!!.onReceiveValue(null)
                    filePathCallbackLollipop = null
                }
                filePathCallbackLollipop = filePathCallback
                val isCapture = fileChooserParams.isCaptureEnabled
                runCamera(isCapture)
                return true
            }
        })
        // 이미지 첨부 설정 끝

        myWebView.loadUrl("http://www.campus-taxi.com/")

    }

//    이미지 창띄우기 시작
var filePathCallbackNormal: ValueCallback<Uri?>? = null
    var filePathCallbackLollipop: ValueCallback<Array<Uri>>? = null
    val FILECHOOSER_NORMAL_REQ_CODE = 2001
    val FILECHOOSER_LOLLIPOP_REQ_CODE = 2002
    private var cameraImageUri: Uri? = null

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 1) {
        if (grantResults.size > 0) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // 하나라도 거부한다면.
                    AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 앱을 이용할 수 있습니다.")
                        .setPositiveButton("종료",
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                                finish()
                            }).setNegativeButton("권한 설정",
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                                val intent: Intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + applicationContext.packageName))
                                applicationContext.startActivity(intent)
                            }).setCancelable(false).show()
                    return
                }
            }
            //Toast.makeText(this, "Succeed Read/Write external storage !", Toast.LENGTH_SHORT).show();
            //startApp();
        }
    }
}
private fun runCamera(_isCapture: Boolean) {
    if (!_isCapture) { // 갤러리 띄운다.
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = MediaStore.Images.Media.CONTENT_TYPE
        pickIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val pickTitle = "사진 가져올 방법을 선택하세요."
        val chooserIntent = Intent.createChooser(pickIntent, pickTitle)
        startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE)
        return
    }
    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    val path = filesDir
    val file = File(path, "fokCamera.png")
    // File 객체의 URI 를 얻는다.
    cameraImageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val strpa = applicationContext.packageName
        FileProvider.getUriForFile(this, applicationContext.packageName + ".fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
    if (!_isCapture) { // 선택팝업 카메라, 갤러리 둘다 띄우고 싶을 때..
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = MediaStore.Images.Media.CONTENT_TYPE
        pickIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val pickTitle = "사진 가져올 방법을 선택하세요."
        val chooserIntent = Intent.createChooser(pickIntent, pickTitle)

        // 카메라 intent 포함시키기..
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(intentCamera))
        startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE)
    } else { // 바로 카메라 실행..
        startActivityForResult(intentCamera, FILECHOOSER_LOLLIPOP_REQ_CODE)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);
        var data = data
        when (requestCode) {
            FILECHOOSER_NORMAL_REQ_CODE -> if (resultCode == RESULT_OK) {
                if (filePathCallbackNormal == null) return
                val result = if (data == null || resultCode != RESULT_OK) null else data.data
                filePathCallbackNormal!!.onReceiveValue(result)
                filePathCallbackNormal = null
            }
            FILECHOOSER_LOLLIPOP_REQ_CODE -> if (resultCode == RESULT_OK) {
                if (filePathCallbackLollipop == null) return
                if (data == null) data = Intent()
                if (data.data == null) data.data = cameraImageUri
                filePathCallbackLollipop!!.onReceiveValue(
                    FileChooserParams.parseResult(
                        resultCode,
                        data
                    )
                )
                filePathCallbackLollipop = null
            } else {
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop!!.onReceiveValue(null)
                    filePathCallbackLollipop = null
                }
                if (filePathCallbackNormal != null) {
                    filePathCallbackNormal!!.onReceiveValue(null)
                    filePathCallbackNormal = null
                }
            }
            else -> {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //    이미지 창띄우기 끝
//    웹뷰 뒤로가기
    override fun onBackPressed() {
    var myWebView: WebView = findViewById(R.id.webView)
        if (myWebView.canGoBack())
        {
            myWebView.goBack()
        }
        else
        {
            finish()
        }
    }
}