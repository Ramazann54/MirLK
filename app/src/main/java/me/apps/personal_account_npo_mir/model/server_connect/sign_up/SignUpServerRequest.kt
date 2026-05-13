package me.apps.personal_account_npo_mir.model.server_connect.sign_up

import me.apps.personal_account_npo_mir.model.server_connect.sign_in.SignInRequestResult

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

class SignUpServerRequest(
    private val url: String,
    private val username: String,
    private val password: String,
    private val scope: CoroutineScope
) : IServerRequest<SignInRequestResult> {
    override fun setServerRequestListener(listener: IServerRequestResultListener<SignInRequestResult>) {
        this.listener = listener
    }


    override fun run() {
        scope.launch {


            if (url == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_URL)
                }
            } else if (username == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_USERNAME)
                }
            } else if (password == "") {
                withContext(Dispatchers.Main) {
                    listener?.onRequestFail(ErrorCode.BLANK_PASSWORD)
                }
            } else {
                var httpURLConnection: HttpURLConnection? = null
                var streamReader: InputStreamReader? = null
                try {
                    val URLAddress: String = url + "/SignUp" + "/" + username + "/" + password
                    var token: String = ""
                    httpURLConnection =
                        URL(URLAddress).openConnection() as HttpURLConnection
                    httpURLConnection.apply {
                        connectTimeout = 10000
                        doInput = true
                    }
                    streamReader = InputStreamReader(httpURLConnection.inputStream)
                    streamReader.use { token = it.readText() }
                    withContext(Dispatchers.Main) {
                        listener?.onRequestSuccess(SignInRequestResult(token, username))
                    }
                } catch (e: MalformedURLException) {
                    withContext(Dispatchers.Main) {
                        listener?.onRequestFail(ErrorCode.WRONG_URL)
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        listener?.onRequestFail(ErrorCode.USER_EXIST)
                    }
                } finally {
                    httpURLConnection?.disconnect()
                    streamReader?.close()
                }
            }

            listener = null
        }
    }

    private var listener: IServerRequestResultListener<SignInRequestResult>? = null
}