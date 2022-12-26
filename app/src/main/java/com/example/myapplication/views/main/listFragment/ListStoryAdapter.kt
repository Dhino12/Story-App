package com.example.myapplication.views.main.listFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemStoryBinding
import com.example.myapplication.model.Story

class ListStoryAdapter : PagingDataAdapter<Story, ListStoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: Story, view: View)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

//    fun init(story: List<Story>) {
//        datas.clear()
//        datas = story.toMutableList()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            Log.e("adapter", data.name)
            holder.bind(data)
            holder.itemView.setOnClickListener{ view ->
                onItemClickCallback.onItemClicked(data, view)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(story: Story) {
                    with(binding) {
                        Log.e("adapter", story.name)
                        tvName.text = story.name
                        tvDescStory.text = story.description
                        tvDate.text = story.createdAt

                        Glide.with(itemView)
                            .load(story.photoUrl)
                            .into(ivStory)
                    }
                }
            }
}