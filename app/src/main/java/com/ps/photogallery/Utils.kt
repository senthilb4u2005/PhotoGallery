package com.ps.photogallery

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity

fun showAlertDialog(
    message: String,
    context: FragmentActivity,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    context.let {
        AlertDialog.Builder(it).apply {
            setCancelable(false)
            setMessage(message)
            setTitle(R.string.error)
            setPositiveButton(R.string.retry
            ) { _, _ ->
                onRetry()
            }
            setNegativeButton(R.string.exit
            ) { _, _ ->
                onExit()
            }

            create()
        }.show()
    }
}