package rivy.assignment.foodblogapp.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import rivy.assignment.foodblogapp.R

class RecyclerViewAdapterFoodBlogDetailsImages(var mContext : Context, var links : List<String>?) :
    RecyclerView.Adapter<RecyclerViewAdapterFoodBlogDetailsImages.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.blog_details_viewpager_individual_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return links!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(links!!.isNotEmpty()) {
            // set the image
            PicassoImageDisplayHandler.displayImage(mContext, links?.get(position), holder.foodImg)
        }
    }

    class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        val foodImg = itemview.findViewById<ImageView>(R.id.food_image)
    }
}