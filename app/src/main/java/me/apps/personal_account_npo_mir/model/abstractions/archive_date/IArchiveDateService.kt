package me.apps.personal_account_npo_mir.model.abstractions.archive_date

import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure

interface IArchiveDateService {
    var dates: List<String>
    var datesCount: Int
    var currentClickedDate: Int
    var arrayOfMeasures: Array<Measure>
    var meterId: Int?
}