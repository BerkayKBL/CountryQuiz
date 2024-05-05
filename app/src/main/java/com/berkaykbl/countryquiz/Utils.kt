package com.berkaykbl.countryquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle

class Utils {

    fun changeActivity(from: Context, to: Class<*>, historyEnable: Boolean, extra: Bundle? = null) {
        val intent = Intent(from, to)
        if (!historyEnable) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        if (extra != null) {
            intent.putExtras(extra)
        }
        from.startActivity(intent)
    }
}