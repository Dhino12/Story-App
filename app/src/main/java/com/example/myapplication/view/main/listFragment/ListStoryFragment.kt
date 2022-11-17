package com.example.myapplication.view.main.listFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentListStoryBinding
import com.example.myapplication.model.Story
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.view.detail.DetailActivity
import com.example.myapplication.viewmodel.StoryViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class ListStoryFragment : Fragment() {
    private lateinit var rvStory: RecyclerView
    private lateinit var userModel: User
    private lateinit var userPreference: UserPreference
    private lateinit var binding: FragmentListStoryBinding
    private lateinit var adapter: ListStoryAdapter
    private lateinit var factory: ViewModelFactory
    private var viewModel: StoryViewModel? = null
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory((activity as Activity))
        )[StoryViewModel::class.java]

        rvStory = binding.rvStory

        userPreference = UserPreference(requireActivity())
        userModel = userPreference.getUser()

        token = userModel.token.toString()

        getStory()

    }

    private fun getStory() {
        val authToken = "Bearer $token"

        adapter = ListStoryAdapter()
        rvStory.adapter = adapter
        rvStory.layoutManager = LinearLayoutManager(activity)
        rvStory.setHasFixedSize(true)
        viewModel?.getStory(authToken)

        viewModel?.apply {
            loading.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = it
            }
            error.observe(viewLifecycleOwner) {
                if(it.isNotEmpty()) {
                    Toast.makeText(activity, "ERROR $it", Toast.LENGTH_SHORT).show()
                }
            }
            storyList.observe(viewLifecycleOwner) {
                adapter.apply {
                    init(it)
                    notifyDataSetChanged()
                }
                adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(story: Story) {
                        showSelectedStory(story)
                    }
                })
            }
        }
    }

    private fun showSelectedStory(story: Story) {
        Intent(activity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.KEY_NAME, story.name)
            it.putExtra(DetailActivity.KEY_DATE, story.createdAt)
            it.putExtra(DetailActivity.KEY_DESC, story.description)
            it.putExtra(DetailActivity.KEY_IMAGE, story.photoUrl)
            startActivity(it)
        }
    }
}