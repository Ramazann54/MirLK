package me.apps.personal_account_npo_mir.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import me.apps.personal_account_npo_mir.presentation.main.activity_presenters.DiagnosticPresenter
import me.apps.personal_account_npo_mir.view.abstractions.main.IDiagnosticView
import me.apps.personalaccountnpomir.R

class DiagnosticFragment : Fragment(), IDiagnosticView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_diagnostic_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        statusTextView = view.findViewById(R.id.diagnosticStatusTextView)
        titleTextView = view.findViewById(R.id.diagnosticTitleTextView)
        descriptionTextView = view.findViewById(R.id.diagnosticDescriptionTextView)

        presenter.onViewCreated(this)

        presenter.loadDiagnostics()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showLoading() {
        statusTextView.text = "Диагностика"
        titleTextView.text = "Проверяем состояние прибора"
        descriptionTextView.text = "Подождите, выполняется запрос к серверу диагностики."
    }

    override fun showDiagnosticResult(
        title: String,
        description: String,
        statusText: String,
        isGood: Boolean
    ) {
        statusTextView.text = statusText
        titleTextView.text = title
        descriptionTextView.text = description

        if (isGood) {
            statusTextView.setTextColor(resources.getColor(R.color.main_blue, null))
        } else {
            statusTextView.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
        }
    }

    override fun showDiagnosticError() {
        statusTextView.text = "Ошибка диагностики"
        titleTextView.text = "Не удалось получить данные"
        descriptionTextView.text = "Сервер диагностики не вернул результат. Проверьте подключение и повторите попытку."
        statusTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
    }

    private lateinit var statusTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var currentDeviceTextView: TextView

    private val presenter = DiagnosticPresenter()
}