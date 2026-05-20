package me.apps.personal_account_npo_mir.presentation.main.activity_presenters

import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.put_measure.PutMeasureRequestResult
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.presentation.abstraction.ISupportWarningDialogPresenter
import me.apps.personal_account_npo_mir.view.abstractions.dialogs.IWarningDialogView
import me.apps.personal_account_npo_mir.view.abstractions.main.ITransmittalView
import me.apps.personalaccountnpomir.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransmittalPresenter :
    IPresenter<ITransmittalView>,
    ISupportWarningDialogPresenter,
    IServerRequestResultListener<PutMeasureRequestResult> {

    override fun onViewCreated(view: ITransmittalView) {
        this.view = view
    }

    fun onSummaryTextChanged(summary: String) {
        this.summary = summary
    }

    fun onTariff1TextChanged(tariff1: String) {
        this.tariff1 = tariff1
    }

    fun onTariff2TextChanged(tariff2: String) {
        this.tariff2 = tariff2
    }

    fun onTariff3TextChanged(tariff3: String) {
        this.tariff3 = tariff3
    }

    fun onTariff4TextChanged(tariff4: String) {
        this.tariff4 = tariff4
    }

    fun onClickHandOverButton() {
        var success = true

        if (summary.isBlank()) {
            success = false
            view?.setSummaryBackground(R.drawable.ic_warning_frame_trans)
        } else {
            view?.setSummaryBackground(R.drawable.rec_trans)
        }

        if (tariff1.isBlank()) {
            success = false
            view?.setTariff1Background(R.drawable.ic_warning_frame_trans)
        } else {
            view?.setTariff1Background(R.drawable.rec_trans)
        }

        if (tariff2.isBlank()) {
            success = false
            view?.setTariff2Background(R.drawable.ic_warning_frame_trans)
        } else {
            view?.setTariff2Background(R.drawable.rec_trans)
        }

        if (tariff3.isBlank()) {
            success = false
            view?.setTariff3Background(R.drawable.ic_warning_frame_trans)
        } else {
            view?.setTariff3Background(R.drawable.rec_trans)
        }

        if (tariff4.isBlank()) {
            success = false
            view?.setTariff4Background(R.drawable.ic_warning_frame_trans)
        } else {
            view?.setTariff4Background(R.drawable.rec_trans)
        }

        if (!success) return

        val currentMeter = App.metersService.meters[App.indexService.index]

        val measure = Measure(
            summary = summary,
            tariff1 = tariff1,
            tariff2 = tariff2,
            tariff3 = tariff3,
            tariff4 = tariff4,
            timestamp = getCurrentDateTime()
        )

        lastSentMeasure = measure

        App.measuresService.putMeasure(
            currentMeter.id,
            App.userDataService.token,
            measure,
            this
        )
    }

    fun getMeterName(): String {
        val meter = App.metersService.meters[App.indexService.index]
        return formatShortAddress(meter.address)
    }

    override fun onRequestSuccess(result: PutMeasureRequestResult) {
        if (result.responseCode == 200) {
            val currentMeter = App.metersService.meters[App.indexService.index]
            val measure = lastSentMeasure

            if (measure != null) {
                App.measuresService.measuresMap[currentMeter.id] = measure
            }

            view?.showDialog()
        } else {
            throw Exception("Error with connecting to server")
        }
    }

    override fun onRequestFail(message: ErrorCode) {
        throw Exception("server error")
    }

    override fun onDestroy() {
        this.view = null
        summary = ""
        tariff1 = ""
        tariff2 = ""
        tariff3 = ""
        tariff4 = ""
        lastSentMeasure = null
    }

    override fun onDialogCreate(view: IWarningDialogView) {
        view.setTitle(R.string.success)
        view.setWarningMessage(R.string.well_done)
    }

    override fun onDialogDestroy() {
    }

    override fun onOkButtonClick() {
    }

    override fun onCancelButtonClick() {
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
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

    private var view: ITransmittalView? = null

    private var summary: String = ""
    private var tariff1: String = ""
    private var tariff2: String = ""
    private var tariff3: String = ""
    private var tariff4: String = ""

    private var lastSentMeasure: Measure? = null
}