package me.apps.personal_account_npo_mir.view.search

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personalaccountnpomir.R

class FoundDeviceViewHolder(
    itemView: View,
    private val onClick: (Meter) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(R.id.deviceNameTextView)
    private val serialTextView: TextView = itemView.findViewById(R.id.deviceSerialTextView)
    private val addressTextView: TextView = itemView.findViewById(R.id.deviceAddressTextView)

    fun bind(meter: Meter) {
        nameTextView.text = meter.name
        serialTextView.text = "Серийный номер: ${meter.serialNumber}"
        addressTextView.text = meter.address

        itemView.setOnClickListener {
            onClick(meter)
        }
    }
}