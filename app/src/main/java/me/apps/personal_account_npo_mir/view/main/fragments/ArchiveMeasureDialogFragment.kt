package me.apps.personal_account_npo_mir.view.main.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import me.apps.personal_account_npo_mir.di.App
import me.apps.personalaccountnpomir.R

class ArchiveMeasureDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)

        return inflater.inflate(R.layout.dialog_archive_measure, container, false)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = App.archiveDateService.currentClickedDate
        val measure = App.archiveDateService.arrayOfMeasures[position]
        val meter = App.metersService.meters[App.indexService.index]

        val addressTextView = view.findViewById<TextView>(R.id.addressTextView)
        val summaryTextView = view.findViewById<TextView>(R.id.summaryTextView)
        val tariff1TextView = view.findViewById<TextView>(R.id.tariff1TextView)
        val tariff2TextView = view.findViewById<TextView>(R.id.tariff2TextView)
        val tariff3TextView = view.findViewById<TextView>(R.id.tariff3TextView)
        val tariff4TextView = view.findViewById<TextView>(R.id.tariff4TextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val copyButton = view.findViewById<View>(R.id.copyButton)

        val formattedDate = formatDateTime(measure.timestamp)

        addressTextView.text = "Адрес: ${meter.address}"
        summaryTextView.text = "Суммарные показания: ${measure.summary}"
        tariff1TextView.text = "Тариф 1: ${measure.tariff1}"
        tariff2TextView.text = "Тариф 2: ${measure.tariff2}"
        tariff3TextView.text = "Тариф 3: ${measure.tariff3}"
        tariff4TextView.text = "Тариф 4: ${measure.tariff4}"
        dateTextView.text = "Дата: $formattedDate"

        val copyText = """
            Адрес: ${meter.name}
            Суммарные показания: ${measure.summary}
            Тариф 1: ${measure.tariff1}
            Тариф 2: ${measure.tariff2}
            Тариф 3: ${measure.tariff3}
            Тариф 4: ${measure.tariff4}
            Дата: $formattedDate
        """.trimIndent()

        copyButton.setOnClickListener {
            copyToClipboard(copyText)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clip = ClipData.newPlainText("Показание", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(requireContext(), "Показание скопировано", Toast.LENGTH_SHORT).show()
    }

    private fun formatDateTime(value: String): String {
        return value
            .replace("T", " ")
            .substringBefore("+")
            .trim()
    }
}