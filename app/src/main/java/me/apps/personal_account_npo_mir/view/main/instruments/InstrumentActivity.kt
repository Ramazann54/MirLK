package me.apps.personal_account_npo_mir.view.main.instruments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.presentation.main.instruments.InstrumentPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IMainView
import me.apps.personal_account_npo_mir.view.login.LogRegActivity
import me.apps.personal_account_npo_mir.view.main.activities.ArchiveActivity
import me.apps.personal_account_npo_mir.view.main.activities.InformationActivity
import me.apps.personal_account_npo_mir.view.main.activities.TransmittalActivity
import me.apps.personal_account_npo_mir.view.main.activities.diagnostic.DiagnosticActivity
import me.apps.personal_account_npo_mir.view.search.SearchDevicesActivity
import me.apps.personalaccountnpomir.R
import android.content.Context
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.viewpager2.widget.MarginPageTransformer


class InstrumentActivity : FragmentActivity(), IMainView,
    OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrument)

        archiveButton = findViewById(R.id.archiveButton)
        archiveButton.setOnClickListener(this)

        diagnosticButton = findViewById(R.id.diagnosticButton)
        diagnosticButton.setOnClickListener(this)

        transmittalButton = findViewById(R.id.transButton)
        transmittalButton.setOnClickListener(this)

        informationButton = findViewById(R.id.informationButton)
        informationButton.setOnClickListener(this)

        logoutButton = findViewById(R.id.logout_btn)
        logoutButton.setOnClickListener(this)

        textView = findViewById(R.id.addDevicesTextView)

        presenter.onViewCreated(this)

        adapter = DeviceAdapter(this, presenter)

        addDevicesButton = findViewById(R.id.addDevicesBtn)
        addDevicesButton.setOnClickListener(this)

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = adapter
        tabLayout = findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "${(position + 1)}"
        }.attach()

        viewPager.setPreviewBothSide(R.dimen._30dp,R.dimen._35dp)

        if(adapter.itemCount == 0){
            informationButton.visibility = View.GONE
            archiveButton.visibility = View.GONE
            transmittalButton.visibility = View.GONE
            diagnosticButton.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
    }


    override fun onClick(view: View?) {
        presenter.setIndex(tabLayout.selectedTabPosition)

        if (view === archiveButton) {
            presenter.onArchiveButtonClick()
        }
        if (view === diagnosticButton) {
            presenter.onDiagnosticButtonClick()
        }
        if (view === transmittalButton) {
            presenter.onTransmittalButtonClick()
        }
        if (view === informationButton) {
            presenter.onInformationButtonClick()
        }
        if (view === addDevicesButton) {
            presenter.onAddDevicesButtonClick()
        }
        if (view === logoutButton) {
            presenter.onLogoutButtonClick()
        }
    }

    override fun refreshItems() {
        runOnUiThread {
            val currentItem = viewPager.currentItem

            adapter = DeviceAdapter(this, presenter)
            viewPager.adapter = adapter

            if (adapter.itemCount > 0) {
                val safeItem = currentItem.coerceAtMost(adapter.itemCount - 1)
                viewPager.setCurrentItem(safeItem, false)
            }

            if (adapter.itemCount == 0) {
                informationButton.visibility = View.GONE
                archiveButton.visibility = View.GONE
                transmittalButton.visibility = View.GONE
                diagnosticButton.visibility = View.GONE
                textView.visibility = View.VISIBLE
            } else {
                informationButton.visibility = View.VISIBLE
                archiveButton.visibility = View.VISIBLE
                transmittalButton.visibility = View.VISIBLE
                diagnosticButton.visibility = View.VISIBLE
                textView.visibility = View.GONE
            }
        }
    }
    override fun setHeader(header: String) {

    }


    override fun startArchiveActivity() {
        val intent = Intent(this, ArchiveActivity::class.java)
        startActivity(intent)
    }

    override fun startDiagnosticActivity() {
        val intent = Intent(this, DiagnosticActivity::class.java)
        startActivity(intent)
    }

    override fun startTransmittalActivity() {
        val intent = Intent(this, TransmittalActivity::class.java)
        startActivity(intent)
    }

    override fun startInformationActivity() {
        val intent = Intent(this, InformationActivity::class.java)
        startActivity(intent)
    }

    override fun startLogRegActivity() {
        val intent = Intent(this, LogRegActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        presenter.deleteToken()
        App.tokenService.deleteToken()
        startActivity(intent)
        finish()
    }

    override fun startSearchDevicesActivity() {
        val intent = Intent(this, SearchDevicesActivity::class.java)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()

        archiveButton.setOnClickListener(null)
        diagnosticButton.setOnClickListener(null)
        transmittalButton.setOnClickListener(null)
        informationButton.setOnClickListener(null)

        logoutButton.setOnClickListener(null)

        presenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (firstResume) {
            firstResume = false
        } else {
            presenter.refreshData()
        }
    }


    private lateinit var adapter: DeviceAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var addDevicesButton: AppCompatButton
    private lateinit var archiveButton: Button
    private lateinit var diagnosticButton: Button
    private lateinit var transmittalButton: Button
    private lateinit var informationButton: Button
    private lateinit var logoutButton: Button
    private lateinit var textView: TextView
    private var presenter = InstrumentPresenter()
    private var firstResume = true
}
