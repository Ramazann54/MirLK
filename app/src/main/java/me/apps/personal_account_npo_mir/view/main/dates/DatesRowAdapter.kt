package me.apps.personal_account_npo_mir.view.main.dates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.ArchivePresenter
import me.apps.personalaccountnpomir.R

class DatesRowAdapter(private val presenter: ArchivePresenter): RecyclerView.Adapter<DatesListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_archive_measure, parent, false)

        return DatesListViewHolder(presenter, view)
    }

    override fun onBindViewHolder(holder: DatesListViewHolder, position: Int) {
        holder.onBind()
        presenter.onBindViewItem(holder, position)
    }

    override fun onViewRecycled(holder: DatesListViewHolder) {
        super.onViewRecycled(holder)
        holder.onCleanUp()
    }

    override fun getItemCount(): Int {
       return  presenter.itemsCount
    }
}