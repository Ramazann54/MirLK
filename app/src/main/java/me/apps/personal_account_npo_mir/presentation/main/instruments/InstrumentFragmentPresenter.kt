package me.apps.personal_account_npo_mir.presentation.main.instruments

import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.main.instruments.InstrumentFragment

class InstrumentFragmentPresenter : IPresenter<InstrumentFragment> {

    override fun onViewCreated(view: InstrumentFragment) {
        this.view = view
    }

    override fun onDestroy() {
        view = null
    }

    fun onMeterIndexCreate(meterIndex: Int) {
        this.meterIndex = meterIndex

        val meter = App.metersService.meters[meterIndex]

        val measure = App.measuresService.measuresMap[meter.id]

        view?.setMeterName(formatShortAddress(meter.address))

        if (measure != null) {
            view?.setMeterIndications(measure.summary)
            view?.setMeterTime(measure.timestamp)
            view?.setTariffs(
                measure.tariff1,
                measure.tariff2,
                measure.tariff3,
                measure.tariff4
            )
        } else {
            view?.showLoadingMeasure()
        }
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

    private var view: InstrumentFragment? = null
    private var meterIndex = 0
}