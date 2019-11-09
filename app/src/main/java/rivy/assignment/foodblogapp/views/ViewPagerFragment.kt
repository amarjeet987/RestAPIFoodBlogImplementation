package rivy.assignment.foodblogapp.views
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.viewpager_layout.view.*
import rivy.assignment.foodblogapp.R
import rivy.assignment.foodblogapp.utils.PicassoImageDisplayHandler

class ViewPagerFragment : Fragment() {

    // Variable for imageLink
    private var imgLink : String? = null

    // to set the imageLink while instantiating the fragment
    companion object {
        fun setImageLink(link : String?) : ViewPagerFragment {
            val frag = ViewPagerFragment()
            if(link != null) {
                val bundle = Bundle()
                bundle.putString("imageLink", link)
                frag.arguments = bundle
            }
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // check if the ViewPagerImageObject exists
        if(arguments != null) {
            arguments?.getString("imageLink")?.let {
                imgLink = it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate the layout
        return inflater.inflate(R.layout.viewpager_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set the image to the ImageView after the view gets created
        context?.let { PicassoImageDisplayHandler.displayImage(it, imgLink, view.foodImg) }
    }
}