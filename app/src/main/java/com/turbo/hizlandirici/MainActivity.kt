package com.turbo.hizlandirici

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(MemoryBridge(), "AndroidBridge")

        webView.loadUrl("file:///android_asset/index.html")
    }

    inner class MemoryBridge {

        @JavascriptInterface
        fun getAvailableMemoryMB(): Long {
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val info = ActivityManager.MemoryInfo()
            am.getMemoryInfo(info)
            return info.availMem / (1024 * 1024)
        }

        @JavascriptInterface
        fun getTotalMemoryMB(): Long {
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val info = ActivityManager.MemoryInfo()
            am.getMemoryInfo(info)
            return info.totalMem / (1024 * 1024)
        }

        @JavascriptInterface
        fun optimizeMemory() {
            try {
                cacheDir?.deleteRecursively()
            } catch (e: Exception) {
            }
            runOnUiThread {
                webView.clearCache(true)
            }
            System.gc()
        }
    }
}
