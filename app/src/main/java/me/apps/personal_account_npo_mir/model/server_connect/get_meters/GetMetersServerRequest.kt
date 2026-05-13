package me.apps.personal_account_npo_mir.model.server_connect.get_meters

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

class GetMetersServerRequest(
    private val url: String,
    private val token: String,
    private val scope: CoroutineScope
) : IServerRequest<GetMetersRequestResult> {
    override fun setServerRequestListener(listener: IServerRequestResultListener<GetMetersRequestResult>) {
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
                    val urlAddress: String = url + "/Devices/getdevices"
                    var devices: String = ""
                    httpURLConnection =
                        URL(urlAddress).openConnection() as HttpURLConnection
                    httpURLConnection.setRequestProperty("X-User-Token", token)
                    httpURLConnection.apply {
                        connectTimeout = 10000
                        doInput = true
                    }
                    streamReader = InputStreamReader(httpURLConnection.inputStream)
                    streamReader.use { devices = it.readText() }
                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(GetMetersRequestResult(devices))
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

    private var listener: IServerRequestResultListener<GetMetersRequestResult>? = null

}