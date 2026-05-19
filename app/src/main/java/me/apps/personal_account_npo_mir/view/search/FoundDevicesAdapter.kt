package me.apps.personal_account_npo_mir.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personalaccountnpomir.R

class FoundDevicesAdapter(
    private val onClick: (Meter) -> Unit
) : RecyclerView.Adapter<FoundDevicesViewHolder>() {

    private var devices: List<Meter> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundDevicesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_found_device, parent, false)

        return FoundDevicesViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: FoundDevicesViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size

    fun setDevices(newDevices: List<Meter>) {
        devices = newDevices
        notifyDataSetChanged()
    }
}