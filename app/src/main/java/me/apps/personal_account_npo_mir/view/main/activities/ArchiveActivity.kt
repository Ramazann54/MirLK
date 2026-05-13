package me.apps.personal_account_npo_mir.view.main.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.ArchivePresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IArchiveView
import me.apps.personal_account_npo_mir.view.main.dates.DatesRowAdapter
import me.apps.personal_account_npo_mir.view.main.activities.OnDateArchiveActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import com.google.android.material.datepicker.MaterialDatePicker
import me.apps.personalaccountnpomir.R

class ArchiveActivity : AppCompatActivity(), IArchiveView, OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        recyclerView = findViewById<RecyclerView>(R.id.archiveRecycler).apply {
            adapter = this@ArchiveActivity.adapter
        }

        presenter.onViewCreated(this)

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(this)

        fromDate = findViewById(R.id.fromDate)
        toDate = findViewById(R.id.toDate)

        fromDate.setOnClickListener {
            showDatePicker(fromDate)
        }
        toDate.setOnClickListener {
            showDatePicker(toDate)
        }
    }

    override fun onClick(view: View?) {
        if (view === backButton) {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun setHeader(header: String) {
        supportActionBar?.title = header
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshItems() {
        adapter.notifyDataSetChanged()
    }

    override fun startItemActivity() {
        val intent = Intent(this, OnDateArchiveActivity::class.java)
        startActivity(intent)
    }
    private fun showDatePicker(targetField: EditText) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setTheme(R.style.MirDatePickerTheme)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            targetField.setText(dateFormat.format(Date(selection)))

            tryLoadArchive()
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }
    private fun tryLoadArchive() {
        val from = fromDate.text.toString()
        val to = toDate.text.toString()

        if (from.isNotBlank() && to.isNotBlank()) {
            presenter.onTransferButtonClick(from, to)
        }
    }
    private lateinit var recyclerView: RecyclerView
    private val presenter = ArchivePresenter()
    private val adapter = DatesRowAdapter(presenter)
    private lateinit var backButton: Button
    private lateinit var fromDate: EditText
    private lateinit var toDate: EditText
}