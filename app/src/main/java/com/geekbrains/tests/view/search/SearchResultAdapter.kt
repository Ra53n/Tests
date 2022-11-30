package com.geekbrains.tests.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.tests.R
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.view.search.SearchResultAdapter.SearchResultViewHolder
import kotlinx.android.synthetic.main.list_item.view.*

internal class SearchResultAdapter(private val onLongItemClick: (String) -> Unit) :
    RecyclerView.Adapter<SearchResultViewHolder>() {

    private var results: List<SearchResult> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        return SearchResultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, null),
            onLongItemClick
        )
    }

    override fun onBindViewHolder(
        holder: SearchResultViewHolder,
        position: Int
    ) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun updateResults(results: List<SearchResult>) {
        this.results = results
        notifyDataSetChanged()
    }

    internal class SearchResultViewHolder(
        itemView: View,
        private val onLongItemClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(searchResult: SearchResult) {
            itemView.repositoryName.text = searchResult.fullName
            itemView.repositoryName.setOnClickListener {
                Toast.makeText(itemView.context, searchResult.fullName, Toast.LENGTH_SHORT).show()
            }
            itemView.repositoryName.setOnLongClickListener {
                searchResult.fullName?.let(onLongItemClick)
                true
            }
        }
    }
}

