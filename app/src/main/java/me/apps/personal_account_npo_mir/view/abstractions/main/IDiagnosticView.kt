package me.apps.personal_account_npo_mir.view.abstractions.main

interface IDiagnosticView {
    fun showLoading()
    fun showDiagnosticResult(
        title: String,
        description: String,
        statusText: String,
        isGood: Boolean
    )
    fun showDiagnosticError()
}