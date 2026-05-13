package me.apps.personal_account_npo_mir.view.abstractions.main

interface IArchiveView {
    fun setHeader(header : String)

    fun refreshItems()

    fun startItemActivity()
}