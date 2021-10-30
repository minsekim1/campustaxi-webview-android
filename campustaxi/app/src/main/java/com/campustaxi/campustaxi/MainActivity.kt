package com.campustaxi.campustaxi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//웹뷰 추가 라이브러
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myWebView: WebView = findViewById(R.id.webView)
        myWebView.webViewClient = WebViewClient()

        var mWebSettings = myWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
//        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
//        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

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