package me.apps.personal_account_npo_mir.presentation.main.activity_presenters

import android.util.Log
import com.google.gson.Gson
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.model.abstractions.archive_date.IDateListViewItem
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure
import me.apps.personal_account_npo_mir.model.server_connect.   ErrorCode
import me.apps.personal_account_npo_mir.model.server_connect.abstractions.IServerRequestResultListener
import me.apps.personal_account_npo_mir.model.server_connect.get_measures.GetMeasuresRequestResult
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IArchiveView

class ArchivePresenter :
    IPresenter<IArchiveView>,
    IServerRequestResultListener<GetMeasuresRequestResult> {

    override fun onViewCreated(view: IArchiveView) {
        this.view = view

        val username = App.userDataService.username
        view.setHeader(username)
        val currentMeter = App.metersService.meters[App.indexService.index]
        val currentMeterId = currentMeter.id
        if(App.archiveDateService.meterId != currentMeterId) {
            App.archiveDateService.dates = emptyList()
            App.archiveDateService.arrayOfMeasures = emptyArray()
            App.archiveDateService.currentClickedDate = 0
            App.archiveDateService.meterId = currentMeterId
        }
        dates = App.archiveDateService.dates
        view.refreshItems()
    }

    override fun onDestroy() {
        this.view = null
    }

    fun onBindViewItem(view: IDateListViewItem, position: Int) {
        val measure = App.archiveDateService.arrayOfMeasures[position]

        view.setDate(measure.timestamp)
        view.setValue(measure.summary)
    }

    fun onItemClick(position: Int) {
        currentClickedPosition = position
        App.archiveDateService.currentClickedDate = position
        view?.startItemActivity()
    }

    fun onTransferButtonClick(fromDate: String, toDate: String) {
        val currentMeter = App.metersService.meters[App.indexService.index]
        App.archiveDateService.meterId = currentMeter.id
        val dateFromForRequest = "${fromDate}%2000%3A00"
        val dateToForRequest = "${toDate}%2000%3A00"
        App.measuresService.getMeasures(
            currentMeter.id,
            App.userDataService.token,
            dateFromForRequest,
            dateToForRequest,
            0,
            100,
            this
        )
    }

    val itemsCount: Int
        get() = App.archiveDateService.arrayOfMeasures.size

    override fun onRequestSuccess(result: GetMeasuresRequestResult) {
        Log.d("ARCHIVE_TEST", "Зашли в onRequestSuccess")
        Log.d("ARCHIVE_TEST", "Ответ архива: ${result.measures}")

        val measures = Gson().fromJson(result.measures, Array<Measure>::class.java)
        Log.d("ARCHIVE_TEST", "Количество показаний: ${measures.size}")
        App.archiveDateService.arrayOfMeasures = measures

        val timestamps = measures.map { it.timestamp }
        Log.d("ARCHIVE_TEST", "Даты для списка: $timestamps")
        dates = timestamps
        App.archiveDateService.dates = timestamps

        view?.refreshItems()
    }

    override fun onRequestFail(errorCode: ErrorCode) {
        Log.d("ARCHIVE_TEST", "Зашли в onRequestFail")
        Log.d("ARCHIVE_TEST", "Ошибка архива: $errorCode")
    }

    private var dates: List<String> = App.archiveDateService.dates
    private var view: IArchiveView? = null
    private var currentClickedPosition: Int = -1
}