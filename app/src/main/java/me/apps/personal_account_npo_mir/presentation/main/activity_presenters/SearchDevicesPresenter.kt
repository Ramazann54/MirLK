package me.apps.personal_account_npo_mir.presentation.main.activity_presenters

import com.google.gson.Gson
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.bind_meter.BindMeterRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.find_devices.FindMeterRequestResult
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.ISearchDevicesView

class SearchDevicesPresenter : IPresenter<ISearchDevicesView> {

    override fun onViewCreated(view: ISearchDevicesView) {
        this.view = view
        view.showEmptySearch()
    }

    fun onSearchTextChanged(text: String) {
        val key = text.trim()

        if (key.isBlank()) {
            foundDevices = emptyList()
            selectedMeter = null
            view?.showEmptySearch()
            return
        }

        val keyNumber = key.toIntOrNull()

        if (keyNumber == null) {
            foundDevices = emptyList()
            selectedMeter = null
            view?.showSearchError()
            return
        }

        App.metersService.findMeters(
            key = keyNumber,
            limit = 10,
            token = App.userDataService.token,
            resultListener = object : IServerRequestResultListener<FindMeterRequestResult> {

                override fun onRequestSuccess(result: FindMeterRequestResult) {
                    try {
                        val devices: Array<Meter> = Gson().fromJson(
                            result.meters,
                            Array<Meter>::class.java
                        )

                        val alreadyLinkedDeviceIds = App.metersService.meters
                            .map { it.id }
                            .toSet()

                        foundDevices = devices
                            .filter { foundDevice ->
                                foundDevice.id !in alreadyLinkedDeviceIds
                            }

                        if (foundDevices.isEmpty()) {
                            view?.showEmptySearch()
                        } else {
                            view?.showFoundDevices(foundDevices)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        foundDevices = emptyList()
                        selectedMeter = null
                        view?.showSearchError()
                    }
                }

                override fun onRequestFail(message: ErrorCode) {
                    foundDevices = emptyList()
                    selectedMeter = null
                    view?.showSearchError()
                }
            }
        )
    }

    fun onDeviceClicked(meter: Meter) {
        selectedMeter = meter
        view?.showPasswordDialog(meter)
    }

    fun onPasswordEntered(meterId: Int, password: String) {
        val meter = currentDevices.firstOrNull { it.id == meterId }

        if (meter == null) {
            view?.showLinkError()
            return
        }

        if (password != meter.contractNumber) {
            view?.showWrongContractNumber()
            return
        }

        val isAlreadyLinked = App.metersService.meters.any { it.id == meter.id }
        if (isAlreadyLinked) {
            view?.showLinkError()
            return
        }

        App.metersService.bindMeter(
            deviceId = meter.id,
            token = App.userDataService.token,
            resultListener = object : IServerRequestResultListener<BindMeterRequestResult> {
                override fun onRequestSuccess(result: BindMeterRequestResult) {
                    view?.showLinkSuccess()
                }

                override fun onRequestFail(message: ErrorCode) {
                    view?.showLinkError()
                }
            }
        )
    }

    override fun onDestroy() {
        view = null
        selectedMeter = null
    }

    private var view: ISearchDevicesView? = null
    private var foundDevices: List<Meter> = emptyList()
    private var selectedMeter: Meter? = null
    private var currentDevices: List<Meter> = emptyList()
}