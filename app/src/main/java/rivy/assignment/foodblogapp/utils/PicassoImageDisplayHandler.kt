package rivy.assignment.foodblogapp.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

class PicassoImageDisplayHandler {
    companion object {
        fun displayImage(mContext : Context, imageLink : String?, imageView : ImageView) {
            Picasso.with(mContext)
                .load(imageLink)
                .centerCrop()
                .fit()
                .into(imageView, object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                        val bitmapRounded = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
                        val canvas = Canvas(bitmapRounded)
                        val paint = Paint()
                        paint.isAntiAlias = true
                        paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                        canvas.drawRoundRect(RectF(0.0f, 0.0f, bitmap.width.toFloat(), bitmap.height.toFloat()), 10.0f, 10.0f, paint)
                        imageView.setImageBitmap(bitmapRounded)
                    }
                    override fun onError() {
                        //do smth when there is picture loading error
                    }
                })
            return
        }
    }
}