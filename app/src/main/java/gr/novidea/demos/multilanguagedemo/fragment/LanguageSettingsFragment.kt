package gr.novidea.demos.multilanguagedemo.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import gr.novidea.demos.multilanguagedemo.R
import java.util.*

class LanguageSettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedLanguageKey: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_language_settings, container, false)

        //Find the RadioGroup in the layout
        val radioGroup: RadioGroup = view.findViewById(R.id.languageGroup)

        //Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("LanguageDemo", Context.MODE_PRIVATE)
        selectedLanguageKey = "selectedLanguage"

        //Get the saved language and set the corresponding RadioButton
        val savedLanguage = sharedPreferences.getString(selectedLanguageKey, "en")
        val selectedRadioButton = when (savedLanguage) {
            "en" -> R.id.english
            "el" -> R.id.greek
            "fr" -> R.id.french
            "es" -> R.id.spanish
            "te" -> R.id.telugu
            else -> R.id.english
        }
        radioGroup.check(selectedRadioButton)

        //Set a listener for radio button changes
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            //Find the checked radio button
            val radioButton = group.findViewById<RadioButton>(checkedId)
            Toast.makeText(context, "${radioButton.text} set!", Toast.LENGTH_SHORT).show()

            //Get the selected language and save it
            val selectedLanguage = when (checkedId) {
                R.id.english -> "en"
                R.id.greek -> "el"
                R.id.french -> "fr"
                R.id.spanish -> "es"
                R.id.telugu -> "te"
                else -> "en"
            }
            saveLanguage(selectedLanguage)

            //Set the locale and recreate the activity to apply the language change
            setLocale(selectedLanguage)
            requireActivity().recreate()
        }

        return view
    }

    //Function to save the selected language in SharedPreferences
    private fun saveLanguage(language: String) {
        sharedPreferences.edit().putString(selectedLanguageKey, language).apply()
    }

    //Function to set the locale/language
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
