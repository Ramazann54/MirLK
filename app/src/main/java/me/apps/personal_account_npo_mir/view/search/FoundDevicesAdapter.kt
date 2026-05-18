package me.apps.personal_account_npo_mir.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personalaccountnpomir.R

class FoundDevicesAdapter(
    private val onClick: (Meter) -> Unit
) : RecyclerView.Adapter<FoundDeviceViewHolder>() {

    private val devices = mutableListOf<Meter>()

    fun setDevices(newDevices: List<Meter>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundDeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_found_device, parent, false)

        return FoundDeviceViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: FoundDeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size
}