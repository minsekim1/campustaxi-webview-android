package com.campustaxi.campustaxi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//웹뷰 추가 라이브러
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        myWebView.loadUrl("http://www.campus-taxi.com/")
    }

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