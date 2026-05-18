package me.apps.personal_account_npo_mir.model.server_connect.bind_meter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import okio.use
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class BindMeterServerRequest(
    val url: String, val deviceId: Int, val token: String, val scope: CoroutineScope
) : IServerRequest<BindMeterRequestResult> {
    override fun setServerRequestListener(listener: IServerRequestResultListener<BindMeterRequestResult>) {
        this.listener = listener
    }

    override fun run() {
        scope.launch {
            if (url == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_URL)
                }
            } else if (token == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_TOKEN)
                }
            } else {
                var httpURLConnection: HttpURLConnection? = null
                var streamReader: InputStreamReader? = null

                try {
                    var requestCode: String = ""
                    val urlAddress = URL(url + "/Devices/linktouser?deviceId=" + deviceId)

                    httpURLConnection = urlAddress.openConnection() as HttpURLConnection
                    httpURLConnection.setRequestProperty("X-User-Token", token)
                    httpURLConnection.apply {
                        connectTimeout = 10000
                        doInput = true
                        requestMethod = "POST"
                    }

                    streamReader = InputStreamReader(httpURLConnection.inputStream)
                    streamReader.use { requestCode = it.readText() }

                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(BindMeterRequestResult(requestCode))
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

    private var listener: IServerRequestResultListener<BindMeterRequestResult>? = null
}