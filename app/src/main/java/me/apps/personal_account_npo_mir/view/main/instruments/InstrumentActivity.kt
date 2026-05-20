package me.apps.personal_account_npo_mir.view.main.instruments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.presentation.main.instruments.InstrumentPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IMainView
import me.apps.personal_account_npo_mir.view.login.LogRegActivity
import me.apps.personalaccountnpomir.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import android.widget.FrameLayout
import me.apps.personal_account_npo_mir.view.main.fragments.ArchiveFragment
import me.apps.personal_account_npo_mir.view.main.fragments.TransmittalFragment
import me.apps.personal_account_npo_mir.view.search.SearchDevicesFragment
import me.apps.personal_account_npo_mir.view.main.fragments.DiagnosticFragment
import me.apps.personal_account_npo_mir.view.main.fragments.InformationFragment
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat


class InstrumentActivity : FragmentActivity(), IMainView, OnClickListener, TransmittalFragment.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrument)

        homeIcon = findViewById(R.id.homeIcon)
        archiveIcon = findViewById(R.id.archiveIcon)
        addIcon = findViewById(R.id.addIcon)
        transIcon = findViewById(R.id.transIcon)
        infoIcon = findViewById(R.id.infoIcon)

        setActiveBottomIcon(homeIcon)
        titleTextView = findViewById(R.id.titleTextView)
        mainContentContainer = findViewById(R.id.mainContentContainer)

        homeButton = findViewById(R.id.homeTab)
        homeButton.setOnClickListener(this)

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
        setupViewPagerCarousel()
        tabLayout = findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "${(position + 1)}"
        }.attach()


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

        if (view === homeButton) {
            showHomeContent()
            return
        }

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
            setupViewPagerCarousel()

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
        showArchiveFragment()
    }

    override fun startDiagnosticActivity() {
        showDiagnosticFragment()
    }

    override fun startTransmittalActivity() {
        showTransmittalFragment()
    }

    override fun startInformationActivity() {
        showInformationFragment()
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
        showSearchDevicesFragment()
    }


    override fun onDestroy() {
        super.onDestroy()

        archiveButton.setOnClickListener(null)
        diagnosticButton.setOnClickListener(null)
        transmittalButton.setOnClickListener(null)
        informationButton.setOnClickListener(null)

        logoutButton.setOnClickListener(null)
        homeButton.setOnClickListener(null)

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
    private fun setupViewPagerCarousel() {
        val recyclerView = viewPager.getChildAt(0) as RecyclerView

        val sideVisibleWidth = 24.dp()
        val pageMargin = 6.dp()

        recyclerView.clipToPadding = false
        recyclerView.clipChildren = false
        recyclerView.setPadding(sideVisibleWidth, 0, sideVisibleWidth, 0)

        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3

        viewPager.setPageTransformer { page, position ->
            page.translationX = -(pageMargin * position)
            page.scaleY = 0.96f + (1 - abs(position)) * 0.04f
            page.alpha = 0.85f + (1 - abs(position)) * 0.15f
        }
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun showArchiveFragment() {
        setActiveBottomIcon(archiveIcon)
        titleTextView.text = "Архив показаний"
        viewPager.visibility = View.GONE
        diagnosticButton.visibility = View.GONE
        textView.visibility = View.GONE
        mainContentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentContainer, ArchiveFragment())
            .commit()
    }

    private fun showHomeContent() {
        setActiveBottomIcon(homeIcon)
        titleTextView.text = "Приборы учета"
        mainContentContainer.visibility = View.GONE
        viewPager.visibility = View.VISIBLE

        if (adapter.itemCount > 0) {
            diagnosticButton.visibility = View.VISIBLE
            textView.visibility = View.GONE
        } else {
            diagnosticButton.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }
    }
    private fun showSearchDevicesFragment() {
        setActiveBottomIcon(addIcon)
        titleTextView.text = "Добавление устройства"

        viewPager.visibility = View.GONE
        diagnosticButton.visibility = View.GONE
        textView.visibility = View.GONE
        mainContentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentContainer, SearchDevicesFragment())
            .commit()
    }

    private fun showTransmittalFragment() {
        setActiveBottomIcon(transIcon)
        titleTextView.text = "Передача показаний"

        viewPager.visibility = View.GONE
        diagnosticButton.visibility = View.GONE
        textView.visibility = View.GONE
        mainContentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentContainer, TransmittalFragment())
            .commit()
    }

    override fun onMeasureSubmitted() {
        showHomeContent()
        refreshItems()
    }

    private fun showDiagnosticFragment() {
        titleTextView.text = "Диагностика"

        viewPager.visibility = View.GONE
        diagnosticButton.visibility = View.GONE
        textView.visibility = View.GONE
        mainContentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentContainer, DiagnosticFragment())
            .commit()
    }

    private fun showInformationFragment() {
        setActiveBottomIcon(infoIcon)
        titleTextView.text = "Информация об устройстве"

        viewPager.visibility = View.GONE
        diagnosticButton.visibility = View.GONE
        textView.visibility = View.GONE
        mainContentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentContainer, InformationFragment())
            .commit()
    }

    private fun setActiveBottomIcon(activeIcon: AppCompatImageView) {
        val activeColor = ContextCompat.getColor(this, R.color.main_blue)
        val inactiveColor = ContextCompat.getColor(this, R.color.light_gray)

        homeIcon.setColorFilter(inactiveColor)
        archiveIcon.setColorFilter(inactiveColor)
        addIcon.setColorFilter(inactiveColor)
        transIcon.setColorFilter(inactiveColor)
        infoIcon.setColorFilter(inactiveColor)

        activeIcon.setColorFilter(activeColor)
    }

    private lateinit var adapter: DeviceAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var addDevicesButton: View
    private lateinit var archiveButton: View
    private lateinit var diagnosticButton: View
    private lateinit var transmittalButton: View
    private lateinit var informationButton: View
    private lateinit var logoutButton: View
    private lateinit var textView: TextView
    private var presenter = InstrumentPresenter()
    private var firstResume = true
    private lateinit var mainContentContainer: FrameLayout
    private lateinit var homeButton: View
    private lateinit var titleTextView: TextView
    private lateinit var homeIcon: AppCompatImageView
    private lateinit var archiveIcon: AppCompatImageView
    private lateinit var addIcon: AppCompatImageView
    private lateinit var transIcon: AppCompatImageView
    private lateinit var infoIcon: AppCompatImageView

}
