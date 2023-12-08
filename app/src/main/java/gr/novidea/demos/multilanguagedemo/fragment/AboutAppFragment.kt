package gr.novidea.demos.multilanguagedemo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gr.novidea.demos.multilanguagedemo.R

class AboutAppFragment : Fragment() {
    //We don't need anything for this nearly blank screen
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }
}
