package com.example.YANK_application.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.YANK_application.R
import com.example.YANK_application.data.DrinkMenu

class DrinkAdapter(
    private val context: Context,
    private val drinkMenu: List<DrinkMenu>,
    val listener: (DrinkMenu) -> Unit
) : RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>() {

    class DrinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgDrink = view.findViewById<ImageView>(R.id.iv_gambarMenu)
        private val titleDrink = view.findViewById<TextView>(R.id.tv_menuTitle)
        private val Menu = view.findViewById<CardView>(R.id.CVMenu)
        private val price = view.findViewById<TextView>(R.id.TVPrice)

        fun bindView(drinkMenu: DrinkMenu, listener: (DrinkMenu) -> Unit) {
            imgDrink.setImageResource(drinkMenu.imgDrink)
            titleDrink.text = drinkMenu.titleDrink
            price.text = "Rp. "+drinkMenu.priceDrink.toString()

            Menu.setOnClickListener {
                listener(drinkMenu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_list_menu, parent, false)
        return DrinkViewHolder(view)
    }

    override fun getItemCount(): Int = drinkMenu.size

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bindView(drinkMenu[position], listener)
    }
}
