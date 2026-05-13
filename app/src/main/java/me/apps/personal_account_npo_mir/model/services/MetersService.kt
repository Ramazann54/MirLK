package me.apps.personal_account_npo_mir.model.services

import kotlinx.coroutines.CoroutineScope
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.meters.IMetersService
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.bind_meter.BindMeterRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.bind_meter.BindMeterServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.find_device.FindMeterRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.find_device.FindMeterServerRequest
import me.apps.personal_account_npo_mir.model.server_connect.get_meters.GetMetersRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.get_meters.GetMetersServerRequest

class MetersService(private val scope: CoroutineScope) : IMetersService{
    private val urlForHostLoopbackInterface: String = "http://127.0.0.1:5000/api"

    override fun getMeters(token:String, resultListener: IServerRequestResultListener<GetMetersRequestResult>){
        val request = GetMetersServerRequest(me.apps.personal_account_npo_mir.model.services.urlForHostLoopbackInterface, token, App.networkScope)
        request.setServerRequestListener(resultListener)
        request.run()
    }
    override fun saveMeters(meters: Array<Meter>) {
        this.meters = meters
    }

    override fun findMeter(key: Int, limit: Int, token: String, resultListener:IServerRequestResultListener<FindMeterRequestResult>) {
        val request = FindMeterServerRequest(urlForHostLoopbackInterface, key, limit, token, scope)
        request.setServerRequestListener(resultListener)
        request.run()
    }

    override fun bindMeter(
        deviceId: Int,
        token: String,
        resultListener: IServerRequestResultListener<BindMeterRequestResult>
    ) {
        val request = BindMeterServerRequest(urlForHostLoopbackInterface, deviceId, token, scope)
        request.setServerRequestListener(resultListener)
        request.run()

    }

    override var meters: Array<Meter>
        get() {
            return _meters
        }
        set(value) {
            _meters = value
        }
    override var id: Int
        get() = _id
        set(value) {
            _id = value
        }

    private var _id = 0
    private var _meters: Array<Meter> = arrayOf(Meter(), Meter(), Meter(), Meter())

}