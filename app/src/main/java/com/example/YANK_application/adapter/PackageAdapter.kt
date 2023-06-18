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
import com.example.YANK_application.data.PackageMenu

class PackageAdapter(
    private val context: Context,
    private val packageMenu: List<PackageMenu>,
    val listener: (PackageMenu) -> Unit
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    class PackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgPackage = view.findViewById<ImageView>(R.id.iv_gambarMenu)
        private val titlePackage = view.findViewById<TextView>(R.id.tv_menuTitle)
        private val Menu = view.findViewById<CardView>(R.id.CVMenu)
        private val price = view.findViewById<TextView>(R.id.TVPrice)

        fun bindView(packageMenu: PackageMenu, listener: (PackageMenu) -> Unit) {
            imgPackage.setImageResource(packageMenu.imgPackage)
            titlePackage.text = packageMenu.titlePackage
            price.text = "Rp. "+packageMenu.pricePackage.toString()


            Menu.setOnClickListener {
                listener(packageMenu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_list_menu, parent, false)
        return PackageViewHolder(view)
    }

    override fun getItemCount(): Int = packageMenu.size

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        holder.bindView(packageMenu[position], listener)
    }
}


