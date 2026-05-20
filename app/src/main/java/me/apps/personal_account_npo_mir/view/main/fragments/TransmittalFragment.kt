package me.apps.personal_account_npo_mir.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.TransmittalPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.ITransmittalView
import me.apps.personalaccountnpomir.R

class TransmittalFragment : Fragment(), ITransmittalView {

    interface Listener {
        fun onMeasureSubmitted()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transmittal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onViewCreated(this)

        handOverButton = view.findViewById(R.id.handOverButton)

        sumTextView = view.findViewById(R.id.indicationOfSumTextView)
        tariff1IndicationsTextView = view.findViewById(R.id.indicationFirstTariffTextView)
        tariff2IndicationsTextView = view.findViewById(R.id.indicationSecondTextView)
        tariff3IndicationsTextView = view.findViewById(R.id.indicationThirdTextView)
        tariff4IndicationsTextView = view.findViewById(R.id.indicationFourthTextView)

        nameTextView = view.findViewById(R.id.meter_name_text_view)
        nameTextView.text = presenter.getMeterName()

        handOverButton.setOnClickListener {
            presenter.onSummaryTextChanged(sumTextView.text.toString())
            presenter.onTariff1TextChanged(tariff1IndicationsTextView.text.toString())
            presenter.onTariff2TextChanged(tariff2IndicationsTextView.text.toString())
            presenter.onTariff3TextChanged(tariff3IndicationsTextView.text.toString())
            presenter.onTariff4TextChanged(tariff4IndicationsTextView.text.toString())
            presenter.onClickHandOverButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handOverButton.setOnClickListener(null)
        presenter.onDestroy()
    }

    override fun showDialog() {
        Toast.makeText(
            requireContext(),
            "Показания успешно переданы",
            Toast.LENGTH_SHORT
        ).show()

        clearFields()

        (activity as? Listener)?.onMeasureSubmitted()
    }

    override fun setSummaryBackground(resourceID: Int) {
        setInputBackground(sumTextView, resourceID)
    }

    override fun setTariff1Background(resourceID: Int) {
        setInputBackground(tariff1IndicationsTextView, resourceID)
    }

    override fun setTariff2Background(resourceID: Int) {
        setInputBackground(tariff2IndicationsTextView, resourceID)
    }

    override fun setTariff3Background(resourceID: Int) {
        setInputBackground(tariff3IndicationsTextView, resourceID)
    }

    override fun setTariff4Background(resourceID: Int) {
        setInputBackground(tariff4IndicationsTextView, resourceID)
    }

    private fun setInputBackground(input: AppCompatEditText, resourceID: Int) {
        if (resourceID == R.drawable.ic_warning_frame_trans) {
            input.setBackgroundResource(R.drawable.transmittal_input_error_bg)
        } else {
            input.setBackgroundResource(R.drawable.transmittal_input_bg)
        }
    }

    private fun clearFields() {
        sumTextView.text?.clear()
        tariff1IndicationsTextView.text?.clear()
        tariff2IndicationsTextView.text?.clear()
        tariff3IndicationsTextView.text?.clear()
        tariff4IndicationsTextView.text?.clear()
    }

    private lateinit var sumTextView: AppCompatEditText
    private lateinit var tariff1IndicationsTextView: AppCompatEditText
    private lateinit var tariff2IndicationsTextView: AppCompatEditText
    private lateinit var tariff3IndicationsTextView: AppCompatEditText
    private lateinit var tariff4IndicationsTextView: AppCompatEditText
    private lateinit var handOverButton: View
    private lateinit var nameTextView: TextView

    private var presenter = TransmittalPresenter()
}