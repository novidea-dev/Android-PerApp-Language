# Android PerApp Language demo

A simple android app with a book library layout to help you understand how multiple language selection works.

# First step
Built.gradle (app level)
Include under
android {

       defaultConfig {
                resourceConfigurations += listOf("en", "el", "fr", "es", "te")
            }
            
} 

This is the list of languages we are going to use.

Then under res/ the multiple locales are created for each language.
In this example:
values (default locale, "en")
values-el (for greek)
values-fr (for french)
values-es (for spanish)
values-te (for telugu)

Under each folder, we translate the strings.xml file for specific language, for example:
This string in strings.xml (en): <string name="about_app_title">Welcome to\Multilanguage Demo</string>
will be automatically used from strings.xml (el) <string name="about_app_title">Καλώς ήρθατε στο\Multilanguage Demo</string> in greek
when locale is set to "gr"

We keep the book data in a books.json raw file under /res/raw

We can use the same logic here with raw-el for greek, raw-fr for french etc.

#Loading the correct locale when the app starts
In our initial activity (MainActivity in this case)
we declare class-wide our Shared preferences:
 
 ``` private lateinit var sharedPreferences: SharedPreferences```

 then, with fun onCreate make sure to load the preferences before creating the view
  ``` sharedPreferences = getSharedPreferences("LanguageDemo", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selectedLanguage", "defaultLanguage")
        setLocale(savedLanguage)
```

With function setLocale we pass the two char identifier 

``` private fun setLocale(language: String?) {
    // Creating a Locale object based on the provided language, defaulting to English ("en") if the language is null
    val locale = Locale(language ?: "en")
    
    // Setting the created Locale as the default Locale for the application
    Locale.setDefault(locale)
    
    // Creating a new Configuration object based on the current resources configuration
    val config = Configuration(resources.configuration)
    
    // Setting the created Locale to the Configuration object
    config.setLocale(locale)
    
    // Updating the application's resources configuration with the new Locale and display metrics
    resources.updateConfiguration(config, resources.displayMetrics)
}
```

#Saving the selected language as locale identifier:

Under LanguageSettingsFragment we have a radio group with 5 radio buttons.

First we load the selected language the same way we did in MainActivity and depending on the identifier we check the correct
radio button.

```val savedLanguage = sharedPreferences.getString(selectedLanguageKey, "en")
        val selectedRadioButton = when (savedLanguage) {
            "en" -> R.id.english
            "el" -> R.id.greek
            "fr" -> R.id.french
            "es" -> R.id.spanish
            "te" -> R.id.telugu
            else -> R.id.english
        }
        radioGroup.check(selectedRadioButton)
```

To change the language of the app we add a checkedChangeListener on our radio group, identifing the language selected
from the radio button's id.
``` radioGroup.setOnCheckedChangeListener { group, checkedId ->
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
```

then we call fun saveLanguage to save the selected identifier to sharedPreferences and fun setLocale to change the app's language.

finishing, we call activity's recreate() fun to recreate the layout with the correct strings.

<h1>In large applications it's recommented to load the locale once and save it to fast memory, avoiding the overhead of having to read the sharedPrefences
each time we want to know the selected language</h1>

#That's enough for string values, what about raw data?

As you can see in DashboardFragment and it's adapter for each book we need a different books.json file dependent on the locale.

We initiate by getting the current language from resources.configuration.locales.

Then we decleare a resourceId equal to our books folder id under raw for the default language
and a localeSpecificResourceId for out books folder depending on the current language other than default.

Now if the localeSpecificResourceId returns NOT 0 that means we have selected a different language, so we select 
the books.json of that folder ID.

in any other case, we use the default resourceId.

```
        val currentLanguage = resources.configuration.locales.get(0).language

        //Retrieving book data based on locale
        val resourceId =
            resources.getIdentifier("books", "raw", "gr.novidea.demos.multilanguagedemo")
        val localeSpecificResourceId = resources.getIdentifier(
            "books_$currentLanguage",
            "raw",
            "gr.novidea.demos.multilanguagedemo"
        )

        val jsonString = if (localeSpecificResourceId != 0) {
            resources.openRawResource(localeSpecificResourceId).bufferedReader()
                .use { it.readText() }
        } else {
            resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        }
```
That's it!

Now you know how to set app's languages, about multiple strings.xml, multiple raw data and how to load and save the selected language!


<div style="display: flex; justify-content: center;">
  <div style="text-align: center; margin-right: 20px;">
    <h3>Dashboard in default language (en)</h3>
    <img src="https://github.com/novidea-dev/Android-PerApp-Language/blob/main/dashboard_en.png" width="250" height="500" style="margin-top: 10px;">
  </div>
  
  <div style="text-align: center;">
    <h3>Description in default language (en)</h3>
    <img src="https://github.com/novidea-dev/Android-PerApp-Language/blob/main/description_en.png" width="250" height="500" style="margin-top: 10px;">
  </div>
</div>

<div style="display: flex; justify-content: center;">
  <div style="text-align: center; margin-right: 20px;">
    <h3>Language selection fragment</h3>
    <img src="https://github.com/novidea-dev/Android-PerApp-Language/blob/main/language_selection.png" width="250" height="500">
  </div>
 
</div>

<div style="display: flex; justify-content: center;">
  <div style="text-align: center; margin-right: 20px;">
    <h3>Dashboard in greek (el)</h3>
   <img src="https://github.com/novidea-dev/Android-PerApp-Language/blob/main/dashboard_gr.png" width="250" height="500">
  </div>
  
  <div style="text-align: center;">
    <h3>Description in greek (el)</h3>
    <img src="https://github.com/novidea-dev/Android-PerApp-Language/blob/main/description_gr.png" width="250" height="500">
  </div>
</div>




