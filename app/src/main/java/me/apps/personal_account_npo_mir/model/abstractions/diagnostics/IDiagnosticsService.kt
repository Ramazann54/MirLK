package me.apps.personal_account_npo_mir.model.abstractions.diagnostics

import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_diagnostics.GetDiagnosticsRequestResult

interface IDiagnosticsService {
    fun getDiagnostics(
        deviceId: Int,
        token: String,
        resultListener: IServerRequestResultListener<GetDiagnosticsRequestResult>,
    )
}