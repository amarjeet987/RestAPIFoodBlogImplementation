package rivy.assignment.foodblogapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FoodImagesViewPagerAdapter(fm: FragmentManager, frags : ArrayList<Fragment>) : FragmentStatePagerAdapter(fm) {

    val fragments = frags

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}