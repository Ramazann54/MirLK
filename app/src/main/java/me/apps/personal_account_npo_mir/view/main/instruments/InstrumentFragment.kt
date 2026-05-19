package me.apps.personal_account_npo_mir.view.main.instruments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.apps.personal_account_npo_mir.presentation.main.instruments.InstrumentFragmentPresenter
import me.apps.personalaccountnpomir.R

const val ARG_OBJECT = "object"

class InstrumentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instrument, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onViewCreated(this)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            try {
                presenter.onMeterIndexCreate(this.getInt(ARG_OBJECT))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setMeterIndications(text: String) {
        val indicationsTextView = view?.findViewById<TextView>(R.id.meterIndicationsTextView)
        indicationsTextView?.text = text
    }

    fun setMeterTime(timestamp: String) {
        val dateView = view?.findViewById<TextView>(R.id.dateTextView)
        dateView?.text = timestamp
    }

    fun setMeterName(name: String) {
        val meterName = view?.findViewById<TextView>(R.id.meterNameTextView2)
        meterName?.text = name.ifBlank { "Дом" }
    }

    fun setTariffs(tariff1: String, tariff2: String, tariff3: String, tariff4: String) {
        val unit = getString(R.string.electric_active_energy_units)

        view?.findViewById<TextView>(R.id.tariff1TextView)?.text = "$tariff1 $unit"
        view?.findViewById<TextView>(R.id.tariff2TextView)?.text = "$tariff2 $unit"
        view?.findViewById<TextView>(R.id.tariff3TextView)?.text = "$tariff3 $unit"
        view?.findViewById<TextView>(R.id.tariff4TextView)?.text = "$tariff4 $unit"

        view?.findViewById<TariffDonutView>(R.id.tariffDonutView)
            ?.setTariffs(tariff1, tariff2, tariff3, tariff4)
    }

    fun showLoadingMeasure() {
        setMeterIndications("—")
        setMeterTime("Данные загружаются")
        setTariffs("0", "0", "0", "0")
    }

    private val presenter = InstrumentFragmentPresenter()
}