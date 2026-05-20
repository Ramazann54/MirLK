package me.apps.personal_account_npo_mir.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.InformationPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IInformationView
import me.apps.personalaccountnpomir.R

class InformationFragment : Fragment(), IInformationView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onViewCreated(this)

        devicesNameText = view.findViewById(R.id.devicesNameText)
        serialNumberText = view.findViewById(R.id.serialNumberText)
        checkDateText = view.findViewById(R.id.checkDateText)
        addressText = view.findViewById(R.id.addressText)

        devicesNameText.text = presenter.getMeterName()
        serialNumberText.text = presenter.getSerialNumber()
        checkDateText.text = "01.01.2015"
        addressText.text = presenter.getShortAddress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    private lateinit var devicesNameText: TextView
    private lateinit var serialNumberText: TextView
    private lateinit var checkDateText: TextView
    private lateinit var addressText: TextView

    private val presenter = InformationPresenter()
}