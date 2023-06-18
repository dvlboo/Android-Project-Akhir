package com.example.YANK_application.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.YANK_application.R
import com.example.YANK_application.activity.DeskripsiMenuActivity
//import com.example.YANK_application.activity.DeskripsiMenuActivity
import com.example.YANK_application.adapter.PackageAdapter
import com.example.YANK_application.data.PackageMenu

class PackageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_package, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_packageMenu)
        val packageMenuList = listOf(
            PackageMenu(
                R.drawable.menu1,
                "Normal Delights Package",
                75000
            ),
            PackageMenu(
                R.drawable.menu2,
                "Deluxe Experience Package",
                80000
            ),
            PackageMenu(
                R.drawable.menu3,
                "Gourmet Journey Package",
                88000
            ),
            PackageMenu(
                R.drawable.menu4,
                "Premium Indulgence Package",
                95000
            ),
            PackageMenu(
                R.drawable.menu5,
                "Supreme Feast Package",
                99000
            ),
            PackageMenu(
                R.drawable.menu6,
                "Ultimate Extravaganza Package",
                105000
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = PackageAdapter(requireContext(), packageMenuList) { packageMenu ->
            // Implement your click listener action here
            val intent = Intent(requireContext(), DeskripsiMenuActivity::class.java)
            intent.putExtra("judul", packageMenu.titlePackage)
            intent.putExtra("gambar", packageMenu.imgPackage)
            intent.putExtra("price", packageMenu.pricePackage)
            requireContext().startActivity(intent)
        }
        return view
    }
}
