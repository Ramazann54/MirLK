package me.apps.personal_account_npo_mir.presentation.main.activity_presenters

import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.presentation.abstraction.IPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IOnDateArchiveView

class OnDateArchivePresenter : IPresenter<IOnDateArchiveView> {

    override fun onViewCreated(view: IOnDateArchiveView) {
        this.view = view
        val position = App.archiveDateService.currentClickedDate

        val date = App.archiveDateService.dates[view.getPosition()]
        val measure = App.archiveDateService.arrayOfMeasures[view.getPosition()]

        view.setHeader(date)
        view.setMeasure(measure)
    }

    override fun onDestroy() {
        this.view = null
    }

    var currentClickedDate: Int = App.archiveDateService.currentClickedDate
    private var view: IOnDateArchiveView? = null
}