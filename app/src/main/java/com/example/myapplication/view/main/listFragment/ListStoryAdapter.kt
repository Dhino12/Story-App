package com.example.myapplication.view.main.listFragment

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentListStoryBinding
import com.example.myapplication.databinding.ItemStoryBinding
import com.example.myapplication.model.Story

class ListStoryAdapter : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var data = mutableListOf<Story>()

    interface OnItemClickCallback {
        fun onItemClicked(story: Story)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun init(story: List<Story>) {
        data.clear()
        data = story.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryAdapter.ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ViewHolder, position: Int) {
        val story = data[position]
        if (data != null) {
            holder.bind(story)
            holder.itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(story)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemStoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(story: Story) {
                    with(binding) {
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