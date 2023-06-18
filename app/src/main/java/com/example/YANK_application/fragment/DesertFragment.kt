package com.example.YANK_application.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.YANK_application.adapter.DesertAdapter
import com.example.YANK_application.data.DesertMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

class DesertFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_desert, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_desertMenu)
        val desertMenuList = listOf(
            DesertMenu(
                R.drawable.desert1,
                "Ice Cream",
                25000
            ),
            DesertMenu(
                R.drawable.desert2,
                "Brownies Fudgy",
            25000
            ),
            DesertMenu(
                R.drawable.desert3,
                "Fruit Salad",
            20000
            )
        )



        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

//        if (meja == "") {
            recyclerView.adapter = DesertAdapter(requireContext(), desertMenuList) { desert ->

                val intent = Intent(requireContext(), DeskripsiMenuActivity::class.java)
                intent.putExtra("judul", desert.titleDesert)
                intent.putExtra("gambar", desert.imgDesert)
                intent.putExtra("price", desert.priceDesert)
                requireContext().startActivity(intent)
            }
//        } else {
//            recyclerView.adapter = DesertAdapter(requireContext(), desertMenuList) { desert ->
//                // Implement your click listener action here
//                val intent = Intent(requireContext(), DeskripsiMenu2Activity::class.java)
//                intent.putExtra("judul", desert.titleDesert)
//                intent.putExtra("gambar", desert.imgDesert)
//                requireContext().startActivity(intent)
//            }
//        }




        return view
    }
}
