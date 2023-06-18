package com.example.YANK_application.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.YANK_application.fragment.DesertFragment
import com.example.YANK_application.fragment.DrinkFragment
import com.example.YANK_application.fragment.PackageFragment

class FragmentPageAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PackageFragment()
            }
            1 -> {
                DrinkFragment()
            }
            else -> {
                DesertFragment()
            }
        }
    }

}