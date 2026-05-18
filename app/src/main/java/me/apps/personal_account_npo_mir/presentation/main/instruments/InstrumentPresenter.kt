package me.apps.personal_account_npo_mir.presentation.main.instruments

import android.util.Log
import com.google.gson.Gson
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personal_account_npo_mir.model.server_connect.ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_last_measure.GetLastMeasureRequestResult
import me.apps.personal_account_npo_mir.model.server_connect.get_meters.GetMetersRequestResult
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IMainView

class InstrumentPresenter : IPresenter<IMainView>,
    IServerRequestResultListener<GetMetersRequestResult> {
    /**
     * Колбэк при создании View
     */
    override fun onViewCreated(view: IMainView) {
        this.view = view
        val username = App.userDataService.username
        view.setHeader(username)
        refreshData()
    }
    fun refreshData() {
        val token = App.userDataService.token
        App.metersService.getMeters(token, this)
    }

    override fun onRequestSuccess(result: GetMetersRequestResult) {
        try {
            val meters:Array<Meter> = Gson().fromJson(result.meters, Array<Meter>::class.java)
            App.metersService.saveMeters(meters)
            view?.refreshItems()
            meters.forEach { meter ->
                    App.measuresService.getLastMeasure(
                        meter.id,
                        App.userDataService.token,
                        object : IServerRequestResultListener<GetLastMeasureRequestResult>{
                            override fun onRequestSuccess(result: GetLastMeasureRequestResult) {
                                try{
                                    val measure: Measure = Gson().fromJson(
                                        result.measure,
                                        Measure::class.java
                                    )
                                    App.measuresService.saveMeasuresMap(result.deviceId, measure)
                                    view?.refreshItems()
                                }
                                catch (e: Exception){
                                    e.printStackTrace()
                                }
                            }

                            override fun onRequestFail(message: ErrorCode){
                                println("Ошибка загрузки последнего показания: $message")
                            }
                        }
                    )
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onRequestFail(message: ErrorCode) {
        println("Ошибка загрузки счетчиков:$message")
    }

    /**
     * Колбэк при завершении работы презентера
     */
    override fun onDestroy() {
        view = null
    }

    /**
     * Колбэк при создании элемента списка
//     * @param view Представление элемента списка счетчиков\
//     * @param position Индекс позиции, по которой будет отображен элемент
     */

//    fun onBindViewItem(view: IMeterListViewItem, position: Int) {
//        view.setName(meters[position].name)
//        view.setIndications(meters[position].serialNumber.toString())
//        // TODO: поставить имя счетчика
//    }

    fun setIndex(index: Int){
        App.indexService.index = index
    }

    fun onAddDevicesButtonClick(){
        view?.startSearchDevicesActivity()
    }

    /**
     * Колбэк при нажатии на кнопку "Архив показаний"
     */
    fun onArchiveButtonClick() {
        view?.startArchiveActivity()
    }

    /**
     * Колбэк при нажатии на кнопку "Диагностика"
     */
    fun onDiagnosticButtonClick() {
        view?.startDiagnosticActivity()
    }

    /**
     * Колбэк при нажатии на кнопку "Передача показаний"
     */
    fun onTransmittalButtonClick() {
        view?.startTransmittalActivity()
    }

    /**
     * Колбэк при нажатии на кнопку "Информация"
     */
    fun onInformationButtonClick() {
        view?.startInformationActivity()
    }

    fun onLogoutButtonClick(){
      view?.startLogRegActivity()
   }

    fun deleteToken(){
        App.userDataService.deleteToken()
    }

    private var view: IMainView? = null
}

