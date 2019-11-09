package rivy.assignment.foodblogapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.blog_details_page_individual_item.view.*
import rivy.assignment.foodblogapp.models.FoodItemModel
import android.animation.LayoutTransition
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.cardview.widget.CardView
import rivy.assignment.foodblogapp.R
import java.util.*


class RecyclerViewAdapterFoodBlogDisplay(var mContext : Context, var data : List<FoodItemModel>) :
    RecyclerView.Adapter<RecyclerViewAdapterFoodBlogDisplay.ViewHolder>() {

    // global element to hold the currently expanded element
    var globalHolder : ViewHolder? = null

    // helper variables
    var counter : Int = 1
    var concatString : String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.blog_details_page_individual_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(data.isNotEmpty()) {
            // head
            holder.head.text = data[position].title

            // change the "best dishes" heading
            holder.dishes_head.text = mContext.getString(R.string.dishes, data[position].title?.toUpperCase(Locale.getDefault()))

            // subhead
            if(data[position].desc?.length!! > 0) {
                holder.subhead.text = data[position].desc
            } else {
                holder.subhead.visibility = View.GONE
            }

            // display the headImage
            PicassoImageDisplayHandler.displayImage(mContext, data[position].img, holder.headImg)

            // expand
            holder.expand.setOnClickListener {
                expandAndCollapse(holder)
            }

            // about
            if(data[position].details?.about?.isEmpty()!!) {
                holder.itemview.about.visibility = View.GONE
            } else {
                holder.about.text = data[position].details?.about?.get(0)
            }

            // where
            if(data[position].details?.where == null) {
                holder.itemview.about.visibility = View.GONE
            } else {
                for(location in data[position].details?.where!!) {
                    concatString += "$counter. ${location.name}"
                    if(location.distance != null) {
                        concatString += " (${location.distance}Km from City Centre)"
                    }

                    if(counter != data[position].details?.where?.size) {
                        concatString+="\n"
                    }

                    counter+=1
                }
                holder.where.text = concatString

            }

            concatString = ""
            counter = 1

            // dishes
            if(data[position].details?.dishes?.size == 0) {
                holder.itemview.best_dishes.visibility = View.GONE
            } else {
                for(location in data[position].details?.dishes!!) {
                    concatString += "$counter. $location"

                    if(counter != data[position].details?.dishes?.size) {
                        concatString+="\n"
                    }

                    counter+=1

                }
                holder.dishes.text = concatString
            }

            concatString = ""
            counter = 1

            // img recyclerView
            if(!data[position].details?.images.isNullOrEmpty()) {
                holder.imgRecView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                val adapter = RecyclerViewAdapterFoodBlogDetailsImages(mContext, data[position].details?.images)
                holder.imgRecView.adapter = adapter
            } else {
                holder.itemview.photos.visibility = View.GONE
            }

        }
    }

    // function to handle expanding and collapsing of the elements
    private fun expandAndCollapse(holder : ViewHolder) {
        // enable transitions during expanding and collapsing elements
        if(globalHolder!=null) {
            if(globalHolder?.adapterPosition != holder.adapterPosition) {
                globalHolder!!.midview.visibility = View.GONE
                globalHolder?.expanded = false
            }
        }

        if(holder.expanded) {
            holder.midview.visibility = View.GONE
            holder.expanded = false
            if(globalHolder?.adapterPosition == holder.adapterPosition) {
                globalHolder = null
            }
        } else {
            TransitionManager.beginDelayedTransition(holder.parentCard, AutoTransition())
            holder.midview.visibility = View.VISIBLE
            globalHolder = holder
            holder.expanded = true
        }
    }

    class ViewHolder(val itemview : View) : RecyclerView.ViewHolder(itemview) {
        val head = itemview.findViewById(R.id.foodName_head) as TextView
        val subhead = itemview.findViewById(R.id.foodname_subhead) as TextView
        val headImg = itemview.findViewById(R.id.foodImage_head) as ImageView
        val about = itemview.findViewById(R.id.about_section) as TextView
        val where = itemview.findViewById(R.id.where_section) as TextView
        val dishes_head = itemview.findViewById(R.id.dishes_head) as TextView
        val dishes = itemview.findViewById(R.id.best_dishes_section) as TextView
        val imgRecView = itemview.findViewById(R.id.recyclerview_food_images) as RecyclerView
        val expand = itemview.findViewById(R.id.expand) as ImageView
        val midview = itemview.findViewById(R.id.mid_card) as LinearLayout
        val parentCard = itemview.findViewById(R.id.parent_card) as CardView
        var expanded : Boolean = false
    }
}