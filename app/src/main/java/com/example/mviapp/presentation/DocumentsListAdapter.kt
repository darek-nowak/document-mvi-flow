package com.example.mviapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mviapp.R
import com.example.mviapp.data.CvDocumentInfo
import kotlinx.android.synthetic.main.document_info_item.view.*
import javax.inject.Inject

class DocumentsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onItemClicked: (CvDocumentInfo) -> Unit
    private val items: MutableList<CvDocumentInfo> = mutableListOf()

    fun setItems(items: List<CvDocumentInfo>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DocumentInfoItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.document_info_item, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DocumentInfoItemViewHolder).bind(items[position].name)
        holder.itemView.setOnClickListener { onItemClicked(items[position]) }
    }
}

class DocumentInfoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val documentItem = view.item

    fun bind(name: String) {
        documentItem.text = name
    }
}