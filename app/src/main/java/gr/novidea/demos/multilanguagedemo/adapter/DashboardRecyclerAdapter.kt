package gr.novidea.demos.multilanguagedemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley
import gr.novidea.demos.multilanguagedemo.LruBitmapCache
import gr.novidea.demos.multilanguagedemo.model.Book
import gr.novidea.demos.multilanguagedemo.R
import gr.novidea.demos.multilanguagedemo.DescriptionActivity

class DashboardRecyclerAdapter(private val context: Context, private val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    // Volley RequestQueue and ImageLoader
    private var requestQueue: RequestQueue
    private var imageLoader: ImageLoader

    init {
        // Initialize Volley RequestQueue and ImageLoader
        requestQueue = Volley.newRequestQueue(context)
        imageLoader = ImageLoader(requestQueue, LruBitmapCache())
    }

    // View Holder for RecyclerView
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBookName: TextView = view.findViewById(R.id.txtBookName)
        val textBookAuthor: TextView = view.findViewById(R.id.txtBookAuthor)
        val textBookPrice: TextView = view.findViewById(R.id.txtBookPrice)
        val textBookRating: TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage: NetworkImageView = view.findViewById(R.id.imgBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        // Inflate layout for each item in RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        // Bind data to views for each item
        val book = itemList[position]
        holder.textBookName.text = book.bookName
        holder.textBookAuthor.text = book.bookAuthor
        holder.textBookPrice.text = book.bookPrice
        holder.textBookRating.text = book.bookRating

        // Load image using Volley
        loadImage(holder.imgBookImage, book.bookImage)

        // Set click listener for item to navigate to DescriptionActivity with book ID
        holder.llContent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("id", book.id)
            context.startActivity(intent)
        }
    }

    // Function to load the image using Volley
    private fun loadImage(networkImageView: NetworkImageView, imageUrl: String) {
        networkImageView.setErrorImageResId(R.drawable.default_book_cover)
        networkImageView.setDefaultImageResId(R.drawable.default_book_cover)
        networkImageView.setImageUrl(imageUrl, imageLoader)
    }
}
