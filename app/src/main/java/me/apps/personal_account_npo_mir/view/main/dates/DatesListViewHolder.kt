package me.apps.personal_account_npo_mir.view.main.dates

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.archive_date.IDateListViewItem
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.ArchivePresenter
import me.apps.personalaccountnpomir.R

class DatesListViewHolder(
    val presenter: ArchivePresenter,
    view: View
) : RecyclerView.ViewHolder(view), View.OnClickListener, IDateListViewItem {

    fun onBind() {
        dateTextView = itemView.findViewById(R.id.archiveDateTextView)
        valueTextView = itemView.findViewById(R.id.archiveValueTextView)
        itemView.setOnClickListener(this)
    }

    fun onCleanUp() {
        itemView.setOnClickListener(null)
    }

    override fun setDate(value: String) {
        dateTextView.text = formatDate(value)
    }

    override fun setValue(value: String) {
        valueTextView.text = "$value кВт·ч"
    }

    override fun onClick(view: View?) {
        if (view === itemView) {
            presenter.onItemClick(adapterPosition)
        }
    }

    private fun formatDate(value: String): String {
        return value.substringBefore(" ")
    }

    private lateinit var dateTextView: TextView
    private lateinit var valueTextView: TextView
}