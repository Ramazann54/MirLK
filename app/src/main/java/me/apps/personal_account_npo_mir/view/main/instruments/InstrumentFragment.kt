package me.apps.personal_account_npo_mir.view.main.instruments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.apps.personal_account_npo_mir.presentation.main.instruments.InstrumentFragmentPresenter
import me.apps.personalaccountnpomir.R
import java.text.SimpleDateFormat
import java.util.*

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
        sumIndications = text
        indicationsTextView?.text = sumIndications
    }

    fun setMeterTime(timestamp: String) {
        dateView = view?.findViewById(R.id.dateTextView)
        //dateView?.text = simpleDate.parse(timestamp)?.toString()
        dateView?.text = timestamp
    }

    fun setMeterName(name: String) {
        meterName = view?.findViewById(R.id.meterNameTextView2)
        meterName?.text = name
    }

    fun setMeterId(id: Int) {
        meterIdTextView = view?.findViewById(R.id.meterIdTextView)
        meterIdTextView?.text = id.toString()
    }


    private val simpleDate = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.CHINA)
    private val presenter = InstrumentFragmentPresenter()
    private var sumIndications: String = ""
    private var dateView: TextView? = null
    private var meterName: TextView? = null

    private var meterIdTextView: TextView? = null


}