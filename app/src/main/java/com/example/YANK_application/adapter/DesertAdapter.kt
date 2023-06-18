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
import com.example.YANK_application.data.DesertMenu

class DesertAdapter(
    private val context: Context,
    private val desertList: List<DesertMenu>,
    private val listener: (DesertMenu) -> Unit
) : RecyclerView.Adapter<DesertAdapter.DesertViewHolder>() {

    class DesertViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgDesert = view.findViewById<ImageView>(R.id.iv_gambarMenu)
        private val titleDesert = view.findViewById<TextView>(R.id.tv_menuTitle)
        private val price = view.findViewById<TextView>(R.id.TVPrice)
        private val Menu = view.findViewById<CardView>(R.id.CVMenu)

        fun bindView(desert: DesertMenu, listener: (DesertMenu) -> Unit) {
            imgDesert.setImageResource(desert.imgDesert)
            titleDesert.text = desert.titleDesert
            price.text = "Rp. "+desert.priceDesert.toString()

            Menu.setOnClickListener {
                listener(desert)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesertViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_list_menu, parent, false)
        return DesertViewHolder(view)
    }

    override fun getItemCount(): Int = desertList.size

    override fun onBindViewHolder(holder: DesertViewHolder, position: Int) {
        holder.bindView(desertList[position], listener)
    }
}
