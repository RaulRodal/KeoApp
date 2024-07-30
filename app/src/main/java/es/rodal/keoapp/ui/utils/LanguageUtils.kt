package es.rodal.keoapp.ui.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

fun setLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration()
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        context.createConfigurationContext(config)
    } else {
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}

fun saveLanguagePreference(context: Context, language: String) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("selected_language", language)
        apply()
    }
}

fun loadLanguagePreference(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return sharedPreferences.getString("selected_language", Locale.getDefault().language)
}