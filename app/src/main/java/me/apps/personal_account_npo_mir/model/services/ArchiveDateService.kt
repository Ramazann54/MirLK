package me.apps.personal_account_npo_mir.model.services

import me.apps.personal_account_npo_mir.model.abstractions.archive_date.IArchiveDateService
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure

class ArchiveDateService : IArchiveDateService {

    override var dates: List<String> = mutableListOf()

    override var datesCount: Int
        get() = _datesCount
        set(value) {
            _datesCount = value
        }

    override var currentClickedDate: Int
        get() = _currentClickedDate
        set(value) {
            _currentClickedDate = value
        }

    override var arrayOfMeasures: Array<Measure> = arrayOf()

    private var _datesCount: Int = 0
    private var _currentClickedDate: Int = 0
    override var meterId: Int? = null
}