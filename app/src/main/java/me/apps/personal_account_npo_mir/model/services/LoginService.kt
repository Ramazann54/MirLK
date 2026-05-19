package me.apps.personal_account_npo_mir.model.services

import kotlinx.coroutines.*
import me.apps.personal_account_npo_mir.model.abstractions.login.ILoginService
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.sign_up.SignUpServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.sign_in.SignInRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.sign_in.SignInServerRequest


class SignInService(private val scope: CoroutineScope) : ILoginService {


val urlForHostLoopbackInterface: String = "http://192.168.0.106:5000/api"

    override fun signIn(
        username: String,
        password: String,
        resultListener: IServerRequestResultListener<SignInRequestResult>
    ) {

        val request = SignInServerRequest(urlForHostLoopbackInterface, username,password, scope)
        request.setServerRequestListener(resultListener)
        request.run()
    }

    override fun signOut(): Boolean {
        return true
    }

    override fun signUp(
        username: String,
        password: String,
        phoneNumber: String,
        resultListener: IServerRequestResultListener<SignInRequestResult>
    ) {
        val request = SignUpServerRequest(urlForHostLoopbackInterface, username,password, scope)
        request.setServerRequestListener(resultListener)
        request.run()
    }

}