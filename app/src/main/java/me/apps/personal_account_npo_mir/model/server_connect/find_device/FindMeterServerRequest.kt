package me.apps.personal_account_npo_mir.model.server_connect.find_device

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.sign_in.SignInRequestResult
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class FindMeterServerRequest(
    private val url: String,
    private val key: Int,
    private val limit: Int,
    private val token: String,
    private val scope: CoroutineScope
) : IServerRequest<FindMeterRequestResult> {
    override fun setServerRequestListener(listener: IServerRequestResultListener<FindMeterRequestResult>) {
        this.listener = listener
    }

    override fun run() {
        scope.launch {
            if (url == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_URL)
                }
            } else if (key == null || limit == null) {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.INPUT_EMPTY)
                }
            } else if (token == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_TOKEN)
                }
            } else {
                var httpURLConnection: HttpURLConnection? = null
                var streamReader: InputStreamReader? = null
                try {
                    val urlAddress: String =
                        url + "/Devices/find/" + key + "/" + limit
                    var meter = ""
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
                    streamReader.use { meter = it.readText() }
                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(FindMeterRequestResult(meter))
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

    private var listener: IServerRequestResultListener<FindMeterRequestResult>? = null

}