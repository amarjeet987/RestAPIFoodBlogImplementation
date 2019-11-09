package rivy.assignment.foodblogapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.sothree.slidinguppanel.ScrollableViewHelper
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import rivy.assignment.foodblogapp.R
import rivy.assignment.foodblogapp.REST.RetrofilHandler
import rivy.assignment.foodblogapp.models.FoodItemModel
import rivy.assignment.foodblogapp.models.ParentModel
import rivy.assignment.foodblogapp.utils.FoodImagesViewPagerAdapter
import rivy.assignment.foodblogapp.utils.RecyclerViewAdapterFoodBlog
import rivy.assignment.foodblogapp.utils.RecyclerViewAdapterFoodBlogDisplay

class MainActivity : AppCompatActivity() {

    /** GLOBAL VARS **/
    // to collect disposables so that they can be cleared later
    private var disposables : CompositeDisposable = CompositeDisposable()

    // ArrayList to store the food items so that it can be passed to the recyclerViewAdapter
    private var foodItems : ArrayList<FoodItemModel> = ArrayList()

    // ArrayList to store the links to the images to be displayed as cover photos
    private var coverPhotos : ArrayList<String?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** SETTING UP THE RECYCLERVIEW FOR THE BLOG PAGE **/
        // ArrayList to store the food objects
        val foodItems : ArrayList<FoodItemModel> = ArrayList()
        val recyclerViewAdapter = RecyclerViewAdapterFoodBlog(this, foodItems)
        recyclerViewFoodBlog.layoutManager = LinearLayoutManager(this)
        recyclerViewFoodBlog.adapter = recyclerViewAdapter

        /** SETTING UP RECYCLERVIEW FOR THE BLOG DETAILS PAGE **/
        // ArrayList to store the card objects
        val cardItems : ArrayList<FoodItemModel> = ArrayList()
        val slideAdapter = RecyclerViewAdapterFoodBlogDisplay(this, cardItems)
        slide_up_layout_recyclerView.layoutManager = LinearLayoutManager(this)
        slide_up_layout_recyclerView.adapter = slideAdapter
        slide_up_layout.isNestedScrollingEnabled = false
        slide_up_layout.setScrollableViewHelper(ScrollableViewHelper())

        // making the API request to get the details
        returnFoodBlogObservable()
            .subscribe(object : Observer<ParentModel> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    // collecting disposables
                    disposables.add(d)
                }

                override fun onNext(t: ParentModel) {
                    // update the UI for the blogListPage
                    updateUI(t, recyclerViewAdapter)

                    // update the UI for the slideUp page
                    t.data?.card?.let { updateUISlide(it, slideAdapter) }
                }

                override fun onError(e: Throwable) {
                }
            })

        /** SETTING UP THE SLIDE UP SCREEN **/
        click_slide.setOnClickListener{
            slide_up_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
        collapse.setOnClickListener {
            slide_up_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Clearing all disposables to avoid memory leaks
        disposables.clear()
    }

    override fun onStart() {
        super.onStart()
        // start the loading animation
        loading.startShimmerAnimation()
    }

    /** FUNCTIONS TO HANDLE API **/
    // function to return an Observable to perform the API request
    private fun returnFoodBlogObservable() : Observable<ParentModel> {
        return RetrofilHandler.retrofitClient
            .fetchBlogs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /** FUNCTIONS TO HANDLE THE INITIAL BLOG DISPLAY PAGE **/
    // update the ui
    fun updateUI(data : ParentModel, adapter : RecyclerViewAdapterFoodBlog) {

        // set the blog heading and subheading
        blog_title.text = getString(R.string.blog_heading, data.data?.card_details?.title, data.data?.card_details?.city)
        head_text.text = getString(R.string.blog_heading, data.data?.card_details?.title, data.data?.card_details?.city)

        // collect 4 images for the cover photos
        var i = 0
        for(foodItem in data.data?.card!!) {
            foodItems.add(foodItem)
            i+=1
            if(i == 3) break
        }

        // sort the ArrayList
        fun selector(foodItem: FoodItemModel): Int? = foodItem.card_no
        foodItems.sortBy( { selector(it) } )

        // pass it to the recyclerViewAdapter
        adapter.foodItems = foodItems
        adapter.notifyDataSetChanged()

        // extract data for the cover images
        for(food in data.data.card) {
            coverPhotos.add(food.img)
        }
        initViewPager(coverPhotos)

        // stop animation and hide shimmer layout
        showLoadedData()
    }

    // function to hide loading screen and show the data
    private fun showLoadedData() {
        loading.stopShimmerAnimation()
        loading_complete.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }


    /******************** CONTINUE FROM HERE, ALSO FIX THE TABLAYOUT ISSUE *********/

    /** FUNCTIONS TO HANDLE THE DETAILED BLOG DISPLAY PAGE **/
    fun updateUISlide(details : List<FoodItemModel>, adapter: RecyclerViewAdapterFoodBlogDisplay) {
        adapter.data = details
        adapter.notifyDataSetChanged()
    }

    /** FUNCTIONS TO HANDLE THE DETAILED BLOG DISPLAY PAGE **/
    private fun initViewPager(imageLinks : ArrayList<String?>) {
        val frags : ArrayList<Fragment> = ArrayList()
        for(link in imageLinks) {
            frags.add(ViewPagerFragment.setImageLink(link))
        }
        val viewPagerAdapter = FoodImagesViewPagerAdapter(supportFragmentManager, frags)
        viewPager_img.adapter = viewPagerAdapter

        // handling viewpager page changes
        viewPager_img.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                initPages()
                when(position) {
                    0 -> img1.setBackgroundResource(R.drawable.tab_selected)
                    1 -> img2.setBackgroundResource(R.drawable.tab_selected)
                    2 -> img3.setBackgroundResource(R.drawable.tab_selected)
                    3 -> img4.setBackgroundResource(R.drawable.tab_selected)
                }
            }

        })
    }

    // to initialize all page pointers to default drawable
    private fun initPages() {
        img1.setBackgroundResource(R.drawable.tab_not_selected)
        img2.setBackgroundResource(R.drawable.tab_not_selected)
        img3.setBackgroundResource(R.drawable.tab_not_selected)
        img4.setBackgroundResource(R.drawable.tab_not_selected)
    }


}