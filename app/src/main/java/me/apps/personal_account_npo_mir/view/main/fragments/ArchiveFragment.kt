package me.apps.personal_account_npo_mir.view.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.ArchivePresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IArchiveView
import me.apps.personal_account_npo_mir.view.main.dates.DatesRowAdapter
import me.apps.personalaccountnpomir.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArchiveFragment : Fragment(), IArchiveView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_archive_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.archiveRecycler).apply {
            adapter = this@ArchiveFragment.adapter
        }

        fromDate = view.findViewById(R.id.fromDate)
        toDate = view.findViewById(R.id.toDate)

        fromDate.setOnClickListener {
            showDatePicker(fromDate)
        }

        toDate.setOnClickListener {
            showDatePicker(toDate)
        }

        presenter.onViewCreated(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun setHeader(header: String) {
        // В фрагменте заголовок уже есть на главном экране, поэтому тут ничего не делаем.
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshItems() {
        adapter.notifyDataSetChanged()
    }

    override fun startItemActivity() {
        ArchiveMeasureDialogFragment()
            .show(parentFragmentManager, "ARCHIVE_MEASURE_DIALOG")
    }

    private fun showDatePicker(targetField: EditText) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText("\u00A0")
            .setTheme(R.style.MirDatePickerTheme)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            targetField.setText(dateFormat.format(Date(selection)))

            tryLoadArchive()
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun tryLoadArchive() {
        val from = fromDate.text.toString()
        val to = toDate.text.toString()

        if (from.isNotBlank() && to.isNotBlank()) {
            presenter.onTransferButtonClick(from, to)
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var fromDate: EditText
    private lateinit var toDate: EditText

    private val presenter = ArchivePresenter()
    private val adapter = DatesRowAdapter(presenter)
}