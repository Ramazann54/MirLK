package me.apps.personal_account_npo_mir.model.server_connect.get_diagnostics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GetDiagnosticsServerRequest(
    private val url: String,
    private val meterId: Int,
    private val token: String,
    private val scope: CoroutineScope
) : IServerRequest<GetDiagnosticsRequestResult> {
    override fun setServerRequestListener(listener: IServerRequestResultListener<GetDiagnosticsRequestResult>) {
        this.listener = listener
    }

    override fun run() {
        scope.launch {
            if (url == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_URL)
                }
            } else if (meterId == null) {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_METER_ID)
                }
            } else {
                var httpURLConnection: HttpURLConnection? = null
                var streamReader: InputStreamReader? = null
                try {
                    val urlAddress: String =
                        url + "/Diagnostics/" + meterId
                    var diagnosticsCode = ""
                    httpURLConnection =
                        withContext(Dispatchers.IO) {
                            URL(urlAddress).openConnection()
                        } as HttpURLConnection
                    httpURLConnection.apply {
                        connectTimeout = 10000
                        doInput = true
                    }
                    httpURLConnection.setRequestProperty("X-User-Token", token)
                    streamReader = InputStreamReader(httpURLConnection.inputStream)
                    streamReader.use { diagnosticsCode = it.readText() }
                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(GetDiagnosticsRequestResult(diagnosticsCode))
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

    private var listener: IServerRequestResultListener<GetDiagnosticsRequestResult>? = null
}