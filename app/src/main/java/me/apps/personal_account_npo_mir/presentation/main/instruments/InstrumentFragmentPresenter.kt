package me.apps.personal_account_npo_mir.presentation.main.instruments

import com.google.gson.Gson
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_last_measure.GetLastMeasureRequestResult
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.main.instruments.InstrumentFragment
import android.util.Log

class InstrumentFragmentPresenter : IPresenter<InstrumentFragment>{

    override fun onViewCreated(view: InstrumentFragment) {
        this.view = view
    }

    override fun onDestroy() {
        view = null
    }


    fun onMeterIndexCreate(meterIndex : Int){
        this.meterIndex = meterIndex

        Log.d("CHECK_CARD", "onMeterIndexCreate meterIndex=$meterIndex")
        Log.d("CHECK_CARD", "meters count=${App.metersService.meters.size}")

        val meter = App.metersService.meters[meterIndex]
        Log.d("CHECK_CARD", "meter id=${meter.id}, name=${meter.name}, serial=${meter.serialNumber}")
        val measure = App.measuresService.measuresMap[meter.id]
        Log.d("CHECK_CARD", "measure for meterId=${meter.id}: $measure")

        name = meter.name
        view?.setMeterName(name)
        view?.setMeterId(meter.id)

        if (measure!=null){
            view?.setMeterIndications(measure.summary)
            view?.setMeterTime(measure.timestamp)
        }
        else {
            view?.setMeterIndications("—")
            view?.setMeterTime("Данные загружаются")
        }
    }

    private var view: InstrumentFragment? = null
    var name : String = ""
    var meterIndex = 0
}