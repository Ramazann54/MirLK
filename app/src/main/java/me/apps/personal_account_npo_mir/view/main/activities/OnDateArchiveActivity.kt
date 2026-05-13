package me.apps.personal_account_npo_mir.view.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.widget.Button
import me.apps.personal_account_npo_mir.model.abstractions.measures.Measure
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.OnDateArchivePresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IOnDateArchiveView
import me.apps.personalaccountnpomir.R

class OnDateArchiveActivity : AppCompatActivity() , IOnDateArchiveView, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_date_archive)

        summary = findViewById(R.id.indicationOfSumTextView2)
        tariff1 = findViewById(R.id.indicationFirstTariffTextView)
        tariff2 = findViewById(R.id.indicationSecondTextView)
        tariff3 = findViewById(R.id.indicationThirdTextView)
        tariff4 = findViewById(R.id.indicationFourthTextView)

        presenter.onViewCreated(this)
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(this)
//        val arguments = intent.extras
//        date = arguments?.getLong("date") as Long
        // TODO: Нужно передавать в эту активити дату и ID счетчика
    }
    override fun onClick(view: View?) {
        if (view === backButton) {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun setMeasure(measure : Measure) {
        summary.text = measure.summary.toString()
        tariff1.text = measure.tariff1.toString()
        tariff2.text = measure.tariff2.toString()
        tariff3.text = measure.tariff3.toString()
        tariff4.text = measure.tariff4.toString()
    }

    override fun setHeader(header : String) {
        supportActionBar?.title = header
    }

    override fun getPosition() : Int {
//        arguments?.getInt("position")
        return presenter.currentClickedDate
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
//    private var date : Long? = null
    private lateinit var summary : TextView
    private lateinit var tariff1 : TextView
    private lateinit var tariff2 : TextView
    private lateinit var tariff3 : TextView
    private lateinit var tariff4 : TextView
    private lateinit var backButton: Button
    private var presenter = OnDateArchivePresenter()
}