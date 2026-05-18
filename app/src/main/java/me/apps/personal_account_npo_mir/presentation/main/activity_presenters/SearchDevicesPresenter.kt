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
    }

    fun onSearchTextChanged(text: String) {
        val key = text.trim()

        if (key.isBlank()) {
            view?.showEmptySearch()
            return
        }

        App.metersService.findMeters(
            key = key.toIntOrNull(),
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

                        val availableDevices = devices
                            .filter { foundDevice ->
                                foundDevice.id !in alreadyLinkedDeviceIds
                            }

                        view?.showFoundDevices(availableDevices.toList())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        view?.showSearchError()
                    }
                }

                override fun onRequestFail(message: ErrorCode) {
                    view?.showSearchError()
                }
            }
        )
    }

    fun onDeviceClicked(meter: Meter) {
        view?.showContractNumberDialog(meter)
    }

    fun onContractNumberEntered(meter: Meter, enteredContractNumber: String) {
        val isAlreadyLinked = App.metersService.meters.any { it.id == meter.id }

        if (isAlreadyLinked) {
            view?.showLinkError()
            return
        }
        if (enteredContractNumber != meter.contractNumber) {
            view?.showWrongContractNumber()
            return
        }
        App.metersService.bindMeter(
            deviceId = meter.id,
            token = App.userDataService.token,
            resultListener = object : IServerRequestResultListener<BindMeterRequestResult>{
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
    }

    private var view: ISearchDevicesView? = null
}