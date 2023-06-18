package com.example.YANK_application.fragment

import android.annotation.SuppressLint
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
import com.example.YANK_application.adapter.DrinkAdapter
import com.example.YANK_application.data.DrinkMenu

class DrinkFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drink, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_drinkMenu)
        val drinkMenuList = listOf(
            DrinkMenu(
                R.drawable.minum1,
                "Mojito",
                25000
            ),
            DrinkMenu(
                R.drawable.minum2,
                "Margarita",
                34000
            ),
            DrinkMenu(
                R.drawable.minum3,
                "Whiskey Sout",
                65000
            ),
            DrinkMenu(
                R.drawable.minum4,
                "Piña Colada",
                28000
            ),
            DrinkMenu(
                R.drawable.minum5,
                "Heineken",
                35000
            ),
            DrinkMenu(
                R.drawable.minum6,
                "Soju",
                45000
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = DrinkAdapter(requireContext(), drinkMenuList) { drinkMenu ->
            // Implementasikan tindakan saat diklik di sini
            val intent = Intent(requireContext(), DeskripsiMenuActivity::class.java)
            intent.putExtra("judul", drinkMenu.titleDrink)
            intent.putExtra("gambar", drinkMenu.imgDrink)
            intent.putExtra("price", drinkMenu.priceDrink)

            requireContext().startActivity(intent)
        }
        return view
    }
}
