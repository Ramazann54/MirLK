package me.apps.personal_account_npo_mir.view.main.instruments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.apps.personal_account_npo_mir.di.App
import me.apps.personal_account_npo_mir.presentation.main.instruments.InstrumentPresenter

class DeviceAdapter(fragment: FragmentActivity, val presenter : InstrumentPresenter) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        val count = App.metersService.meters.size
        Log.d("CHECK_ADAPTER", "DeviceAdapter itemCount=$count")
        return count
    }
    override fun createFragment(position: Int): Fragment {
        Log.d("CHECK_ADAPTER", "Create fragment position=$position")
        val fragment = InstrumentFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }
        return fragment
    }
}