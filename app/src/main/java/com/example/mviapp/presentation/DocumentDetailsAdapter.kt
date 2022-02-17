package com.example.mviapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mviapp.R
import com.example.mviapp.data.DocumentDisplayItem
import kotlinx.android.synthetic.main.document_big_item.view.*
import kotlinx.android.synthetic.main.document_extra_big_item.view.*
import kotlinx.android.synthetic.main.document_header.view.*
import kotlinx.android.synthetic.main.document_item.view.*
import kotlinx.android.synthetic.main.document_paragraph_item.view.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

private const val VIEW_TYPE_EXTRA_BIG_ITEM = 0
private const val VIEW_TYPE_BIG_ITEM = 1
private const val VIEW_TYPE_ITEM = 2
private const val VIEW_TYPE_PARAGRAPH_ITEM = 3
private const val VIEW_TYPE_HEADER = 4


class DetailsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<DocumentDisplayItem> = mutableListOf()

    fun setItems(items: List<DocumentDisplayItem>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when(items[position]) {
        is DocumentDisplayItem.ExtraBigItem -> VIEW_TYPE_EXTRA_BIG_ITEM
        is DocumentDisplayItem.BigItem -> VIEW_TYPE_BIG_ITEM
        is DocumentDisplayItem.Item -> VIEW_TYPE_ITEM
        is DocumentDisplayItem.ParagraphItem -> VIEW_TYPE_PARAGRAPH_ITEM
        is DocumentDisplayItem.Header -> VIEW_TYPE_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EXTRA_BIG_ITEM -> DocumentExtraBigItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.document_extra_big_item, parent, false)
            )
            VIEW_TYPE_BIG_ITEM -> DocumentBigItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.document_big_item, parent, false)
            )
            VIEW_TYPE_ITEM -> DocumentItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.document_item, parent, false)
            )
            VIEW_TYPE_HEADER -> DocumentHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.document_header, parent, false)
            )
            VIEW_TYPE_PARAGRAPH_ITEM -> DocumentParagraphItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.document_paragraph_item, parent, false)
            )
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val name = items[position].name
        when (holder) {
            is DocumentExtraBigItemViewHolder -> holder.bind(name)
            is DocumentBigItemViewHolder -> holder.bind(name)
            is DocumentItemViewHolder -> holder.bind(name)
            is DocumentHeaderViewHolder -> holder.bind(name)
            is DocumentParagraphItemViewHolder -> holder.bind(name)
        }
    }
}

class DocumentExtraBigItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val documentItem = view.extraBigItem
    fun bind(name: String) {
        documentItem.text = name
    }
}
class DocumentBigItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val documentItem = view.bigItem
    fun bind(name: String) {
        documentItem.text = name
    }
}

class DocumentParagraphItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val documentItem = view.paragraphItem
    fun bind(name: String) {
        documentItem.text = name
    }
}

class DocumentItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val documentItem = view.item
    fun bind(name: String) {
        documentItem.text = name
    }
}

class DocumentHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val header = view.header
    fun bind(name: String) {
        header.text = name
    }
}