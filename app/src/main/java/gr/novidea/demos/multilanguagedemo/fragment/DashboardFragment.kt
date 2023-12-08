package gr.novidea.demos.multilanguagedemo.fragment

import gr.novidea.demos.multilanguagedemo.adapter.DashboardRecyclerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.novidea.demos.multilanguagedemo.R
import gr.novidea.demos.multilanguagedemo.model.Book
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class DashboardFragment : Fragment() {

    //Initializing necessary variables
    private lateinit var recyclerDashboard: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: DashboardRecyclerAdapter

    //ArrayList to store book information
    private val bookInfoList = arrayListOf<Book>()

    //Comparator for sorting books based on ratings and names
    private val ratingComparator = Comparator<Book> { book1, book2 ->
        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            book1.bookName.compareTo(book2.bookName, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflating the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //Enabling options menu
        setHasOptionsMenu(true)

        //Initializing views
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        //Initializing RecyclerView's layout manager
        layoutManager = LinearLayoutManager(activity)

        //Getting current language
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

        try {
            //Parsing JSON data and populating bookInfoList
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val bookJsonObject = jsonArray.getJSONObject(i)
                val bookObject = Book(
                    bookJsonObject.getString("id"),
                    bookJsonObject.getString("name"),
                    bookJsonObject.getString("author"),
                    bookJsonObject.getString("rating"),
                    bookJsonObject.getString("price"),
                    bookJsonObject.getString("image"),
                    ""
                )
                bookInfoList.add(bookObject)
            }

            //Initializing and setting up RecyclerView Adapter
            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)
            recyclerDashboard.adapter = recyclerAdapter
            recyclerDashboard.layoutManager = layoutManager

        } catch (e: JSONException) {
            //Handling JSON parsing exception
            Toast.makeText(
                activity as Context,
                "Error while loading data",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }

    //Inflating options menu for sorting
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    //Handling item selection in the options menu
    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sort) {
            //Sorting the bookInfoList based on ratings and names
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()
            //Notifying adapter about the data change
            recyclerAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}
