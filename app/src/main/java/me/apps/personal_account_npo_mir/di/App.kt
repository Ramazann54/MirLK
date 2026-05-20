package me.apps.personal_account_npo_mir.di

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.apps.personal_account_npo_mir.model.abstractions.user_data.ITokenService
import me.apps.personal_account_npo_mir.model.abstractions.archive_date.IArchiveDateService
import me.apps.personal_account_npo_mir.model.abstractions.diagnostics.IDiagnosticsService
import me.apps.personal_account_npo_mir.model.abstractions.index.IIndexService
import me.apps.personal_account_npo_mir.model.abstractions.login.ILoginService
import me.apps.personal_account_npo_mir.model.abstractions.measures.IMeasureService
import me.apps.personal_account_npo_mir.model.abstractions.meters.IMetersService
import me.apps.personal_account_npo_mir.model.abstractions.user_data.IUserDataService
import me.apps.personal_account_npo_mir.model.services.TokenService
import me.apps.personal_account_npo_mir.model.services.*

class   App : Application() {
    override fun onCreate() {
        super.onCreate()
        userDataService = UserDataService()
        networkScope = CoroutineScope(Dispatchers.IO)
        measuresService = MeasuresService(networkScope)
        loginService = SignInService(networkScope)
        tokenService = TokenService(applicationContext)
        archiveDateService = ArchiveDateService()
        metersService = MetersService(networkScope)
        indexService = IndexService()
        diagnosticService = DiagnosticsService(networkScope)
    }

    companion object {
        lateinit var tokenService: ITokenService
        lateinit var measuresService: IMeasureService
        lateinit var loginService: ILoginService
        lateinit var userDataService: IUserDataService
        lateinit var networkScope: CoroutineScope
        lateinit var archiveDateService: IArchiveDateService
        lateinit var metersService: IMetersService
        lateinit var indexService: IIndexService
        lateinit var diagnosticService: IDiagnosticsService
    }
}
