package me.apps.personal_account_npo_mir.presentation.main.activity_presenters

import com.google.gson.Gson
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.diagnostic.DiagnosticResult
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_diagnostics.GetDiagnosticsRequestResult
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IDiagnosticView

class DiagnosticPresenter :
    IPresenter<IDiagnosticView>,
    IServerRequestResultListener<GetDiagnosticsRequestResult> {

    override fun onViewCreated(view: IDiagnosticView) {
        this.view = view
    }

    fun loadDiagnostics() {
        view?.showLoading()

        val currentMeter = App.metersService.meters[App.indexService.index]

        App.diagnosticService.getDiagnostics(
            deviceId = currentMeter.id,
            token = App.userDataService.token,
            resultListener = this
        )
    }

    override fun onRequestSuccess(result: GetDiagnosticsRequestResult) {
        try {
            handleDiagnosticsResponse(result.diagnosticsCode)
        } catch (e: Exception) {
            e.printStackTrace()
            view?.showDiagnosticError()
        }
    }

    override fun onRequestFail(message: ErrorCode) {
        view?.showDiagnosticError()
    }

    private fun handleDiagnosticsResponse(json: String) {
        try {
            val diagnostics = Gson().fromJson(
                json,
                Array<DiagnosticResult>::class.java
            )

            if (diagnostics.isEmpty()) {
                showResultByCode(0)
                return
            }

            val code = diagnostics.first().diagnosticsCode
            showResultByCode(code)

        } catch (e: Exception) {
            e.printStackTrace()
            view?.showDiagnosticError()
        }
    }

    private fun showResultByCode(code: Int) {
        when (code) {
            0 -> {
                view?.showDiagnosticResult(
                    title = "Ошибок не обнаружено",
                    description = "Прибор учета работает в штатном режиме. Передача данных выполняется корректно.",
                    statusText = "Состояние в норме",
                    isGood = true
                )
            }

            9 -> {
                view?.showDiagnosticResult(
                    title = "Требуется поверка прибора",
                    description = "Срок поверки прибора учета истёк или подходит к завершению. Рекомендуется обратиться в обслуживающую организацию.",
                    statusText = "Предупреждение",
                    isGood = false
                )
            }

            10 -> {
                view?.showDiagnosticResult(
                    title = "Обнаружено диагностическое предупреждение",
                    description = "Сервер вернул код диагностики 10. Возможны некритичные отклонения в работе прибора. Рекомендуется проверить состояние устройства.",
                    statusText = "Требуется внимание",
                    isGood = false
                )
            }

            15 -> {
                view?.showDiagnosticResult(
                    title = "Ошибка связи с прибором",
                    description = "Не удалось получить корректный ответ от прибора учета. Возможна проблема с каналом связи, модулем передачи данных или питанием устройства.",
                    statusText = "Критическая ошибка",
                    isGood = false
                )
            }

            else -> {
                view?.showDiagnosticResult(
                    title = "Неизвестный диагностический код",
                    description = "Сервер вернул код $code. Для этого кода пока нет расшифровки в приложении.",
                    statusText = "Код диагностики: $code",
                    isGood = false
                )
            }
        }
    }

    fun getCurrentMeterAddress(): String {
        val meter = App.metersService.meters[App.indexService.index]
        return formatShortAddress(meter.address)
    }

    override fun onDestroy() {
        view = null
    }

    private fun formatShortAddress(address: String): String {
        var result = address
            .replace("г. Омск,", "")
            .replace("г. Омск", "")
            .replace("ул.", "")
            .replace("д.", "")
            .trim()

        val flatRegex = Regex("""кв\.?\s*(\d+[А-Яа-яA-Za-z]?)""")
        val flat = flatRegex.find(result)?.groupValues?.getOrNull(1)

        result = result
            .replace(Regex(""",?\s*кв\.?\s*\d+[А-Яа-яA-Za-z]?"""), "")
            .replace(",", "")
            .replace(Regex("""\s+"""), " ")
            .trim()

        return if (!flat.isNullOrBlank()) {
            "$result, кв. $flat"
        } else {
            result
        }
    }

    private var view: IDiagnosticView? = null
}