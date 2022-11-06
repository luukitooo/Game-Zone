package com.lukaarmen.gamezone.ui.tabs.leagues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.databinding.ItemLeagueBinding
import com.lukaarmen.gamezone.databinding.ItemLeagueSavedBinding
import com.lukaarmen.gamezone.model.League

class LeagueAdapter : ListAdapter<League, RecyclerView.ViewHolder>(LeagueItemCallback) {

    companion object {
        const val IS_SAVED = 1
        const val NOT_SAVED = 2
    }

    var onItemClickListener: ((League) -> Unit)? = null

    var onItemLongClickListener: ((League) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IS_SAVED -> SavedLeagueViewHolder(
                ItemLeagueSavedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> LeagueViewHolder(
                ItemLeagueBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedLeagueViewHolder -> holder.bind()
            is LeagueViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).isSaved) {
            true -> IS_SAVED
            else -> NOT_SAVED
        }
    }

    inner class SavedLeagueViewHolder(private val binding: ItemLeagueSavedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val league = getItem(adapterPosition)
            Glide.with(binding.ivLeague)
                .load(league.imageUrl)
                .placeholder(R.drawable.ic_error)
                .into(binding.ivLeague)
            binding.apply {
                tvTitle.text = league.name
                root.setOnClickListener {
                    onItemClickListener?.invoke(league)
                }
                root.setOnLongClickListener {
                    onItemLongClickListener?.invoke(league)
                    true
                }
            }
        }
    }

    inner class LeagueViewHolder(private val binding: ItemLeagueBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val league = getItem(adapterPosition)
            Glide.with(binding.ivLeague)
                .load(league.imageUrl)
                .placeholder(R.drawable.ic_error)
                .into(binding.ivLeague)
            binding.apply {
                tvTitle.text = league.name
                root.setOnClickListener {
                    onItemClickListener?.invoke(league)
                }
                root.setOnLongClickListener {
                    onItemLongClickListener?.invoke(league)
                    true
                }
            }
        }
    }

    private object LeagueItemCallback : DiffUtil.ItemCallback<League>() {
        override fun areItemsTheSame(oldItem: League, newItem: League): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: League, newItem: League): Boolean {
            return oldItem == newItem
        }
    }

}