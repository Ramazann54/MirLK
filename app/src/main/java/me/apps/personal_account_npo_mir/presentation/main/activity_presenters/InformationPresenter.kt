package me.apps.personal_account_npo_mir.presentation.main.activity_presenters

import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IInformationView

class InformationPresenter : IPresenter<IInformationView> {

    override fun onViewCreated(view: IInformationView) {
        this.view = view
        meter = App.metersService.meters[App.indexService.index]
    }

    override fun onDestroy() {
        view = null
    }

    fun getMeterName(): String {
        return meter.name
    }

    fun getSerialNumber(): String {
        return meter.serialNumber.toString()
    }

    fun getShortAddress(): String {
        return formatShortAddress(meter.address)
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

    private var view: IInformationView? = null
    private var meter = App.metersService.meters[App.indexService.index]
}