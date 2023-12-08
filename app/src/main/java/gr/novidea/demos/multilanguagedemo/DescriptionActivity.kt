package gr.novidea.demos.multilanguagedemo

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class DescriptionActivity : AppCompatActivity() {

    //Initializing necessary views and variables
    private lateinit var txtBookName: TextView
    private lateinit var txtBookAuthor: TextView
    private lateinit var txtBookPrice: TextView
    private lateinit var txtBookRating: TextView
    private lateinit var imgBookImage: NetworkImageView
    private lateinit var txtBookDesc: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var requestQueue: RequestQueue
    private lateinit var imageLoader: ImageLoader
    private lateinit var bookId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        //Initializing views
        initializeViews()

        //Set up toolbar
        setupToolbar()

        //Get book ID from intent, handle errors if not found
        retrieveBookId()

        //Fetch and process book data based on locale and book ID
        fetchDataAndProcess()
    }

    //Function to initialize views from layout
    private fun initializeViews() {
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        toolbar = findViewById(R.id.toolbar)

        //Initialize Volley RequestQueue and ImageLoader
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, LruBitmapCache())
    }

    //Function to set up toolbar
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.book_detail_title)
    }

    //Function to retrieve book ID from intent
    private fun retrieveBookId() {
        if (intent != null) {
            bookId = intent.getStringExtra("id") ?: "0"
        } else {
            showErrorAndFinish("Error loading book!")
        }
    }

    //Function to fetch and process book data
    private fun fetchDataAndProcess() {
        val currentLanguage = resources.configuration.locales.get(0).language
        val resourceId = resources.getIdentifier("books", "raw", "gr.novidea.demos.multilanguagedemo")
        val localeSpecificResourceId = resources.getIdentifier("books_$currentLanguage", "raw", "gr.novidea.demos.multilanguagedemo")

        val jsonArray = if (localeSpecificResourceId != 0) {
            JSONArray(resources.openRawResource(localeSpecificResourceId).bufferedReader().use { it.readText() })
        } else {
            JSONArray(resources.openRawResource(resourceId).bufferedReader().use { it.readText() })
        }

        processBookData(jsonArray)
    }

    //Function to process book data fetched from JSON
    private fun processBookData(jsonArray: JSONArray) {
        if (jsonArray.length() == 0) {
            showErrorAndFinish("No books found")
            return
        }

        val bookJsonObject = jsonArray.getJSONObject(bookId.toInt() - 1)

        // Load book details into respective views
        loadImage(imgBookImage, bookJsonObject.getString("image"))
        txtBookName.text = bookJsonObject.getString("name")
        txtBookAuthor.text = bookJsonObject.getString("author")
        txtBookPrice.text = bookJsonObject.getString("price")
        txtBookRating.text = bookJsonObject.getString("rating")
        txtBookDesc.text = bookJsonObject.getString("description")
    }

    private fun loadImage(networkImageView: NetworkImageView, imageUrl: String) {
        networkImageView.setErrorImageResId(R.drawable.default_book_cover)
        networkImageView.setDefaultImageResId(R.drawable.default_book_cover)
        networkImageView.setImageUrl(imageUrl, imageLoader)
    }

    //Function to show error message and finish activity
    private fun showErrorAndFinish(errorMessage: String) {
        Toast.makeText(this@DescriptionActivity, errorMessage, Toast.LENGTH_SHORT).show()
        finish()
    }
}
