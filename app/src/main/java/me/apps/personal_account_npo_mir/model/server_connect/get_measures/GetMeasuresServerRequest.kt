package me.apps.personal_account_npo_mir.model.server_connect.get_measures

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_last_measure.GetLastMeasureRequestResult
import me.apps.personal_account_npo_mir.model.services.urlForHostLoopbackInterface
import okio.use
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GetMeasuresServerRequest(
    private val url: String,
    private val deviceId: Int,
    private val token: String,
    private val dateFrom: String,
    private val dateTo: String,
    private val pageNumber: Int,
    private val countInPage: Int,
    private val scope: CoroutineScope
) : IServerRequest<GetMeasuresRequestResult> {

    override fun setServerRequestListener(listener: IServerRequestResultListener<GetMeasuresRequestResult>) {
        this.listener = listener
    }

    override fun run() {
        scope.launch {


            if (url == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_URL)
                }
            } else if (deviceId == null || dateFrom == "" || dateTo == "" || pageNumber == null || countInPage == null) {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.INPUT_EMPTY)
                }
            } else if (token == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_TOKEN)
                }
            } else {
                val urlAddress: String =
                    "$urlForHostLoopbackInterface/Measures/getmeasures/$deviceId/$dateFrom/$dateTo/$pageNumber/$countInPage"
                println(urlAddress)
                Log.d("ARCHIVE_TEST", "URL архива = $urlAddress")
                var httpURLConnection: HttpURLConnection? = null
                var streamReader: InputStreamReader? = null
                var measures: String = ""
                try {
                    httpURLConnection =
                        URL(urlAddress).openConnection() as HttpURLConnection
                    httpURLConnection.apply {
                        connectTimeout = 10000
                        doInput = true
                        httpURLConnection.setRequestProperty("X-User-Token", token)
                    }
                    val streamReader = InputStreamReader(httpURLConnection.inputStream)
                    streamReader.use { measures = it.readText() }
                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(GetMeasuresRequestResult(measures))
                    }
                } catch (e: MalformedURLException) {
                    withContext(Dispatchers.Main) {
                        listener?.onRequestFail(ErrorCode.WRONG_URL)
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        listener?.onRequestFail(ErrorCode.IOEXCEPTION)
                    }
                } finally {
                    httpURLConnection?.disconnect()
                    streamReader?.close()
                }
            }

            listener = null
        }
    }

    private var listener: IServerRequestResultListener<GetMeasuresRequestResult>? = null
}