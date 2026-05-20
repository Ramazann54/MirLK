package me.apps.personal_account_npo_mir.model.services

import kotlinx.coroutines.CoroutineScope
import me.apps.personal_account_npo_mir.model.abstractions.diagnostics.IDiagnosticsService
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_diagnostics.GetDiagnosticsRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.get_diagnostics.GetDiagnosticsServerRequest

class DiagnosticsService(private val scope: CoroutineScope): IDiagnosticsService {
    override fun getDiagnostics(
        deviceId: Int,
        token: String,
        resultListener: IServerRequestResultListener<GetDiagnosticsRequestResult>,
    ) {
        val request = GetDiagnosticsServerRequest(urlForHostLoopbackInterface, deviceId, token, scope)
        request.setServerRequestListener(resultListener)
        request.run()
    }
}