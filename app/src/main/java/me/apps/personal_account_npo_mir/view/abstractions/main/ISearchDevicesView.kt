package me.apps.personal_account_npo_mir.view.abstractions.main

import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter

interface ISearchDevicesView {
    fun showFoundDevices(devices: List<Meter>)
    fun showEmptySearch()
    fun showSearchError()
    fun showContractNumberDialog(meter: Meter)
    fun showWrongContractNumber()
    fun showLinkSuccess()
    fun showLinkError()
}