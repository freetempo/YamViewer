package com.freetempo.yamviewer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PowerManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


class PCHomeActivity: AppCompatActivity() {

    companion object {
        private const val STATUS_CODE_NOT_MODIFIED = 304
    }

    // http://photo.pchome.com.tw/s13/4/7/470804/book1618/p159655706095.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book325/p153062245919.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book353/p154695677719.jpg
    // http://photo.pchome.com.tw/s13/4/7/470804/book1515/p155482685211.jpg
    // http://photo.pchome.com.tw/s14/g/i/gigi54080829/book18/p119609818412.jpg
    // http://photo.pchome.com.tw/s14/g/i/gigi54080829/book21/p124124181864.jpg
    // http://photo.pchome.com.tw/s14/g/i/gigi54080829/book24/p125453570991.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book353/p154695677719.jpg
    // http://photo.pchome.com.tw/s14/g/i/gigi54080829/book16/p124260907930.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book387/p157175696368.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book325/p153062245919.jpg

    // http://photo.pchome.com.tw/s18/l/e/len7511/book202/p156142568998.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book140/p157953054746.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book372/p155828330014.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book372/p155828336558.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book283/p143325067514.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book319/p149761961155.jpg

    // 3a
    //http://photo.pchome.com.tw/s11/a/d/ad152/book293/p153771383728.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book400/p157953429235.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book190/p135653064862.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book407/p158738974835.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book387/p157175696368.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book262/p157175334329.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book416/p159508614652.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book379/p156491799014.jpg
    //http://photo.pchome.com.tw/s11/a/d/ad152/book257/p156959402192.jpg
    //http://photo.pchome.com.tw/s13/y/a/ya32388/book89/p154497522627.jpg
    // http://photo.pchome.com.tw/s11/s/e/sex3p1211/book124/p156714580525.jpg

//    private val baseUrl = "http://photo.pchome.com.tw/s11/8/7/878315/book33/p"
//    private var picNumber = 155963195546
//    private val isUpSearch = true


    //15383337460

//    private val sleepInterval = 67L
    private val sleepInterval = 51L

    // 4
//     http://photo.pchome.com.tw/s11/a/d/ad152/book305/p157944548890.jpg
//     http://photo.pchome.com.tw/s11/a/d/ad152/book305/p157944548890.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book90/p130685930615.jpg
    // http://photo.pchome.com.tw/s14/g/i/1/book18/p119609818412.jpg
    // http://photo.pchome.com.tw/s14/g/i/gigi54080829/book19/p124260905630.jpg
    // http://photo.pchome.com.tw/s14/g/i/gigi54080829/book19/p124260905630.jpg
    // http://photo.pchome.com.tw/s18/l/e/len7511/book202/p156142568998_150.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book217/p155585813480.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book92/p159379292714.jpg
    // http://photo.pchome.com.tw/s11/a/d/ad152/book200/p136370455997.jpg
    // http://photo.pchome.com.tw/s16/l/i/like0311love/book9/p126544585012.jpg
    // http://photo.pchome.com.tw/s14/l/o/love740518/book9/p124471790959.jpg
    private val baseUrl = "http://photo.pchome.com.tw/s11/a/d/ad152/book140/p"
    private var picNumber = 157953067910
    private val isUpSearch = true

//    private val fileName = "_150.jpg"
        private val fileName = ".jpg"
    // thumbnail



//    private var picNumberUp = 154695677719
//    private var picNumberUp = 159655696095
//    private var picNumberDown = 124124181864

//    private val count = 10000
    private val count = 10000

    private val shouldSleepInterval = true

//    private val targetPicNumber = picNumberDown - count
    private val checkPoint = 200L

    private val currentCount = 0L

    private val targetPicNumber = if (isUpSearch) picNumber + count else picNumber - count

    private lateinit var requestQueue: RequestQueue
    private lateinit var thread: Thread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pchome)

//        requestQueue = Volley.newRequestQueue(this)

//        startChecking()

//        thread.run {
//            startChecking()
//        }
//        thread = Thread(runnable)
//        thread = Thread(runnable2)
//        thread.start()
//        thread.run {
//
//        }

//        // keep screen on
//
//        val pm =  getSystemService(Context.POWER_SERVICE) as? PowerManager
//        pm?.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
//        this.mWakeLock.acquire();



        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        val handlerThread = HandlerThread("URLConnection")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        if (shouldSleepInterval) {
            handler.postDelayed(runnable2, sleepInterval)
        } else {
            handler.post(runnableNoDelay)
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable2)
        super.onDestroy()
    }

    lateinit var handler: Handler

//    lateinit var runnable2: Runnable

    private val runnable2 = object : Runnable {
        override fun run() {
            val targetUrl = if (isUpSearch) "$baseUrl${picNumber++}$fileName" else "$baseUrl${picNumber--}$fileName"
            try {

//                Log.d("Larry test", "runnable2")
                if ((picNumber % checkPoint).toInt()== 0) {
                    Log.d("homePC test", "check === $picNumber")
                }
                val connection = (URL(targetUrl).openConnection() as? HttpURLConnection)?.apply {
                    instanceFollowRedirects = false
                }
                val contentType = connection?.getHeaderField("Content-Type")
                Log.d("Larry test", "contentType: $contentType")
                val isImage = contentType?.startsWith("image/") ?: false//true if image
                if (isImage) {
                    Log.d("homePC test", "image: $targetUrl")
                } else if (contentType == null) {
                    Log.d("homePC test", "Error: contentType null - $targetPicNumber")
                }
                connection?.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if ((isUpSearch && (picNumber < targetPicNumber)) || (!isUpSearch && (picNumber > targetPicNumber))) {
                handler.postDelayed(this, sleepInterval)
            } else {
                Log.d("homePC test", "finish === $picNumber")
            }
        }
    }

    private val runnableNoDelay = object : Runnable {
        override fun run() {
            val targetUrl = if (isUpSearch) "$baseUrl${picNumber++}$fileName" else "$baseUrl${picNumber--}$fileName"
            try {
                if ((picNumber % checkPoint).toInt()== 0) {
                    Log.d("homePC test", "check === $picNumber")
                }
                val connection = (URL(targetUrl).openConnection() as? HttpURLConnection)?.apply {
                    instanceFollowRedirects = false
                }
                val contentType = connection?.getHeaderField("Content-Type")
                Log.d("Larry test", "contentType: $contentType")
                val isImage = contentType?.startsWith("image/") ?: false//true if image
                if (isImage) {
                    Log.d("homePC test", "image: $targetUrl")
                } else if (contentType == null) {
                    Log.d("homePC test", "Error: contentType null - $targetPicNumber")
                }
                connection?.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if ((isUpSearch && (picNumber < targetPicNumber)) || (!isUpSearch && (picNumber > targetPicNumber))) {
                handler.post(this)
            } else {
                Log.d("homePC test", "finish === $picNumber")
            }
        }
    }

//    private val runnable3 = Runnable {
//        val targetUrl = "$baseUrl${picNumberUp++}$fileName"
//        try {
//            Log.d("Larry test", "runnable2")
//            val connection: URLConnection = URL(targetUrl).openConnection()
//            val contentType: String = connection.getHeaderField("Content-Type")
//            Log.d("Larry test", "contentType: $contentType")
//            val isImage = contentType.startsWith("image/") //true if image
//            if (isImage) {
//                Log.d("Larry test", "image: $targetUrl")
//            }
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        if (picNumberUp < targetPicNumber) {
//            handler.postDelayed(runnable2, sleepInterval)
//        }
//    }

    fun testtest() {

    }


//    val runnable = Runnable {
//        for (i in 0 .. count) {
//            try {
//                Thread.sleep(sleepInterval)
//            } catch (e: Exception) { }
//
//            if ((picNumberUp % checkPoint).toInt()== 0) {
//                Log.d("Larry test", "check === $picNumberUp")
//            }
//            val targetUrl = "$baseUrl${picNumberUp++}$fileName"
//            val request: StringRequest = object : StringRequest (
//                    Method.GET,
//                    targetUrl,
//                    Response.Listener { response ->
////                        Log.d("Larry test", "response: ${response}")
////                        Log.d("Larry test", "targetUrl: $targetUrl")
////                try {
////                    parsePhotos(JSONObject(response))
////                } catch (e: JSONException) {
////                    e.printStackTrace()
////                }
//                    }, Response.ErrorListener { }) {
////                @Throws(AuthFailureError::class)
////                override fun getParams(): Map<String, String> {
////                    val map: MutableMap<String, String> = HashMap()
////                    map["albumId"] = albumId
////                    map["page"] = Integer.toString(page)
////                    return map
////                }
//
//                override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
//                    val statusCode = response?.statusCode
//                    if (statusCode == STATUS_CODE_NOT_MODIFIED) {
////                        Log.d("Larry test", "parseNetworkResponse: ${response?.statusCode}")
//                        Log.d("Larry test", "targetUrl: $targetUrl")
//                    }
//                    return super.parseNetworkResponse(response)
//                }
//
//                override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
//                    Log.d("Larry test", "parseNetworkError: ${volleyError?.networkResponse?.statusCode}")
//                    Log.d("Larry test", "errorUrl: $targetUrl")
//                    return super.parseNetworkError(volleyError)
//                }
//            }
//            requestQueue.add(request)
////            ToastUtil.showToast(this, String.format(getString(R.string.page_number), page))
//        }
//    }
//            private fun startChecking() {
//        for (i in 0 .. count) {
////            try {
////                Thread.sleep(sleepInterval)
////            } catch (e: Exception) { }
//
//            if ((picNumberUp % checkPoint).toInt()== 0) {
//                Log.d("Larry test", "check === $picNumberUp")
//            }
//            val targetUrl = "$baseUrl${picNumberUp++}$fileName"
//            val request: StringRequest = object : StringRequest (
//                    Method.GET,
//                    targetUrl,
//                    Response.Listener { response ->
////                        Log.d("Larry test", "response: ${response}")
////                        Log.d("Larry test", "targetUrl: $targetUrl")
////                try {
////                    parsePhotos(JSONObject(response))
////                } catch (e: JSONException) {
////                    e.printStackTrace()
////                }
//            }, Response.ErrorListener { }) {
////                @Throws(AuthFailureError::class)
////                override fun getParams(): Map<String, String> {
////                    val map: MutableMap<String, String> = HashMap()
////                    map["albumId"] = albumId
////                    map["page"] = Integer.toString(page)
////                    return map
////                }
//
//                override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
//                    val statusCode = response?.statusCode
//                    if (statusCode == STATUS_CODE_NOT_MODIFIED) {
////                        Log.d("Larry test", "parseNetworkResponse: ${response?.statusCode}")
//                        Log.d("Larry test", "targetUrl: $targetUrl")
//                    }
//                    return super.parseNetworkResponse(response)
//                }
//
//                override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
//                    Log.d("Larry test", "parseNetworkError: ${volleyError?.networkResponse?.statusCode}")
//                    Log.d("Larry test", "errorUrl: $targetUrl")
//                    return super.parseNetworkError(volleyError)
//                }
//            }
//            requestQueue.add(request)
////            ToastUtil.showToast(this, String.format(getString(R.string.page_number), page))
//        }
//    }
}