package com.example.myapplication.view.main.listFragment

import android.text.Layout
import android.util.Log
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
    private var datas = mutableListOf<Story>()

    interface OnItemClickCallback {
        fun onItemClicked(story: Story)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun init(story: List<Story>) {
        datas.clear()
        datas = story.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = datas[position]
        if (datas != null) {
            holder.bind(story)
            holder.itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(story)
            }
        }
    }

    override fun getItemCount(): Int = datas.size

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