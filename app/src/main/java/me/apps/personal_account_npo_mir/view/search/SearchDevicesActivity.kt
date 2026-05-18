package me.apps.personal_account_npo_mir.view.search

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.SearchDevicesPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.ISearchDevicesView
import me.apps.personalaccountnpomir.R

class SearchDevicesActivity : AppCompatActivity(), View.OnClickListener, ISearchDevicesView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_devices)

        presenter.onViewCreated(this)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener(this)

        searchEditText = findViewById(R.id.searchDevicesEditText)
        textView = findViewById(R.id.textEnterSerialNumber)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = foundDevicesAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                presenter.onSearchTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onClick(view: View?) {
        if (view === backButton) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun showFoundDevices(devices: List<Meter>) {
        foundDevicesAdapter.setDevices(devices)

        if (devices.isEmpty()) {
            textView.visibility = View.VISIBLE
            textView.text = "Приборы не найдены"
        } else {
            textView.visibility = View.GONE
        }
    }

    override fun showEmptySearch() {
        foundDevicesAdapter.setDevices(emptyList())
        textView.visibility = View.VISIBLE
        textView.text = getString(R.string.enter_devices_number)
    }

    override fun showSearchError() {
        foundDevicesAdapter.setDevices(emptyList())
        textView.visibility = View.VISIBLE
        textView.text = "Приборы не найдены"
    }

    override fun showContractNumberDialog(meter: Meter) {
        val input = EditText(this)
        input.hint = "Введите номер договора"
        input.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle(meter.name)
            .setMessage("Запросите пароль уровня считывателя СПОДЭС у вашей снабжающей организации")
            .setView(input)
            .setPositiveButton("Проверить") { _, _ ->
                presenter.onContractNumberEntered(
                    meter,
                    input.text.toString()
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun showWrongContractNumber() {
        Toast.makeText(this, "Пароль не совпадает", Toast.LENGTH_SHORT).show()
    }

    override fun showLinkSuccess() {
        Toast.makeText(this, "Прибор успешно привязан", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showLinkError() {
        Toast.makeText(this, "Ошибка привязки прибора", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private val presenter = SearchDevicesPresenter()

    private val foundDevicesAdapter = FoundDevicesAdapter { meter ->
        presenter.onDeviceClicked(meter)
    }

    private lateinit var backButton: AppCompatButton
    private lateinit var searchEditText: EditText
    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
}