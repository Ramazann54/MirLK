package me.apps.personal_account_npo_mir.view.search

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.apps.personal_account_npo_mir.model.abstractions.meters.Meter
import me.apps.personalaccountnpomir.R

class FoundDevicesViewHolder(
    itemView: View,
    private val onClick: (Meter) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val deviceNameTextView: TextView =
        itemView.findViewById(R.id.deviceNameTextView)

    private val serialNumberTextView: TextView =
        itemView.findViewById(R.id.serialNumberTextView)

    private val addressTextView: TextView =
        itemView.findViewById(R.id.addressTextView)

    fun bind(meter: Meter) {
        deviceNameTextView.text = meter.name
        serialNumberTextView.text = "Серийный номер: ${meter.serialNumber}"
        addressTextView.text = meter.address

        itemView.setOnClickListener {
            onClick(meter)
        }
    }
}