package me.apps.personal_account_npo_mir.model.server_connect.put_measure

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PutMeasureServerRequest(
    private val urlForHostLoopbackInterface: String,
    private val deviceId: Int,
    val token: String,
    val measure: Measure,
    private val scope: CoroutineScope
) : IServerRequest<PutMeasureRequestResult> {
    override fun setServerRequestListener(listener: IServerRequestResultListener<PutMeasureRequestResult>) {
        this.listener = listener
    }

    override fun run() {
        scope.launch {
            if (urlForHostLoopbackInterface == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_URL)
                }
            } else if (token == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_TOKEN)
                }
            } else if (deviceId == null) {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_METER_ID)
                }
            } else if (measure == null) {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.INPUT_EMPTY)
                }
            } else {
                val urlAddress: String =
                    urlForHostLoopbackInterface + "/Measures/PutMeasure?deviceId=" + deviceId
                val httpURLConnection: HttpURLConnection? = null
                val writer: OutputStreamWriter? = null
                val gson = Gson()
                try {
                    val url = URL(urlAddress)
                    val connection =
                        withContext(Dispatchers.IO) {
                            url.openConnection()
                        } as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("X-User-Token", token)
                    connection.doOutput = true
                    val now: LocalDateTime = LocalDateTime.now()
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formatDateTime: String = now.format(formatter)
                    measure.timestamp = formatDateTime
                    val measureJson = gson.toJson(measure)
                    println(measureJson)
                    val outputStream = OutputStreamWriter(connection.outputStream)
                    withContext(Dispatchers.IO) {
                        outputStream.write(measureJson)
                        outputStream.flush()
                        outputStream.close()
                    }
                    val responseCode = connection.responseCode
                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(PutMeasureRequestResult(responseCode))
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
                    writer?.close()
                }

                listener = null
            }
        }
    }

    private var listener: IServerRequestResultListener<PutMeasureRequestResult>? = null
}
