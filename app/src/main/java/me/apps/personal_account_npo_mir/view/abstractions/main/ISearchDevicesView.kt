package me.apps.personal_account_npo_mir.view.abstractions.main

import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter

interface ISearchDevicesView {
    fun showEmptySearch()
    fun showFoundDevices(devices: List<Meter>)
    fun showSearchError()

    fun showPasswordDialog(meter: Meter)

    fun showWrongContractNumber()
    fun showLinkSuccess()
    fun showLinkError()
}