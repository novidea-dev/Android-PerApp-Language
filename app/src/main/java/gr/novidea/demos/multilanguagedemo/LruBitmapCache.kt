package gr.novidea.demos.multilanguagedemo

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class LruBitmapCache(maxSize: Int = DEFAULT_CACHE_SIZE) : LruCache<String, Bitmap>(maxSize),
    ImageLoader.ImageCache {

    companion object {
        private const val DEFAULT_CACHE_SIZE = 4 * 1024 * 1024
    }

    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.byteCount / 1024
    }

    override fun getBitmap(url: String): Bitmap? {
        return get(url)
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }
}