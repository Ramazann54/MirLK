package me.apps.personal_account_npo_mir.view.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.SearchDevicesPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.ISearchDevicesView
import me.apps.personalaccountnpomir.R

class SearchDevicesFragment :
    Fragment(),
    ISearchDevicesView,
    LinkDeviceDialogFragment.Listener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_devices_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchEditText = view.findViewById(R.id.searchDevicesEditText)
        hintTextView = view.findViewById(R.id.textEnterSerialNumber)
        recyclerView = view.findViewById(R.id.recyclerView)

        adapter = FoundDevicesAdapter(
            onClick = { meter ->
                presenter.onDeviceClicked(meter)
            }
        )

        recyclerView.adapter = adapter

        presenter.onViewCreated(this)

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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showEmptySearch() {
        hintTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        adapter.setDevices(emptyList())
    }

    override fun showFoundDevices(devices: List<Meter>) {
        hintTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        adapter.setDevices(devices)
    }

    override fun showSearchError() {
        Toast.makeText(
            requireContext(),
            "Не удалось найти приборы",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showPasswordDialog(meter: Meter) {
        LinkDeviceDialogFragment
            .newInstance(
                meterId = meter.id,
                deviceName = meter.name
            )
            .show(childFragmentManager, "LINK_DEVICE_DIALOG")
    }

    override fun onPasswordEntered(meterId: Int, password: String) {
        presenter.onPasswordEntered(meterId, password)
    }

    override fun showWrongContractNumber() {
        Toast.makeText(
            requireContext(),
            "Неверный пароль",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showLinkSuccess() {
        Toast.makeText(
            requireContext(),
            "Прибор успешно привязан",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showLinkError() {
        Toast.makeText(
            requireContext(),
            "Не удалось привязать прибор",
            Toast.LENGTH_SHORT
        ).show()
    }

    private lateinit var searchEditText: EditText
    private lateinit var hintTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoundDevicesAdapter

    private val presenter = SearchDevicesPresenter()
}