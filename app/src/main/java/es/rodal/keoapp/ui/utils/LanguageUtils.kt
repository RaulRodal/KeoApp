package es.rodal.keoapp.ui.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun setLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration()
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    context.createConfigurationContext(config)

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