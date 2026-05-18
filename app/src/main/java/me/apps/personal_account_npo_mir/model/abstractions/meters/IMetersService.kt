package me.apps.personal_account_npo_mir.model.abstractions.meters

import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.bind_meter.BindMeterRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.find_devices.FindMeterRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.get_meters.GetMetersRequestResult

interface IMetersService {
    /**
     * Получить счетчики пользователя
     * @param username Имя пользователя
     */

    fun bindMeter(
        deviceId: Int,
        token: String,
        resultListener: IServerRequestResultListener<BindMeterRequestResult>
    )

    var meters: Array<Meter>
    var id: Int
    fun saveMeters(meters: Array<Meter>)
    fun findMeters(
        key: Int?,
        limit: Int,
        token: String,
        resultListener: IServerRequestResultListener<FindMeterRequestResult>
    )

    fun getMeters(
        token: String,
        resultListener: IServerRequestResultListener<GetMetersRequestResult>
    )

}