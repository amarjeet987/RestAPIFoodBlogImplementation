package rivy.assignment.foodblogapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import rivy.assignment.foodblogapp.models.FoodItemModel
import android.graphics.drawable.BitmapDrawable
import android.graphics.*
import rivy.assignment.foodblogapp.R


class RecyclerViewAdapterFoodBlog(val mContext : Context, var foodItems : ArrayList<FoodItemModel>) : RecyclerView.Adapter<RecyclerViewAdapterFoodBlog.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_food_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // set the food image
        if(foodItems.size > 0) {

            // set the image
            PicassoImageDisplayHandler.displayImage(mContext, foodItems[position].img, holder.foodImg)

            // set the heading
            holder.heading.text = foodItems[position].title
            holder.subHeading.text = foodItems[position].desc
        }
    }

    class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        val foodImg = itemview.findViewById(R.id.food_img_blog) as ImageView
        val heading = itemview.findViewById(R.id.foodname_head) as TextView
        val subHeading = itemview.findViewById(R.id.foodname_subhead) as TextView
    }


}