package com.example.myapplication.views.main.listFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentListStoryBinding
import com.example.myapplication.model.Story
import com.example.myapplication.preference.User
import com.example.myapplication.preference.UserPreference
import com.example.myapplication.views.detail.DetailActivity
import com.example.myapplication.views.main.addStory.AddStoryActivity
import com.example.myapplication.viewmodel.StoryPagerViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class ListStoryFragment : Fragment() {
    private lateinit var rvStory: RecyclerView
    private lateinit var userModel: User
    private lateinit var userPreference: UserPreference
    private lateinit var binding: FragmentListStoryBinding
    private var rvAdapter: ListStoryAdapter = ListStoryAdapter()
    private lateinit var factory: ViewModelFactory
    private val viewModel: StoryPagerViewModel by viewModels {
        factory
    }
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

        userPreference = UserPreference(requireActivity())
        userModel = userPreference.getUser()

        token = userModel.token.toString()
        Log.e("list", token)
        factory = ViewModelFactory.getInstance(requireActivity(), token)

        rvStory = binding.rvStory

        binding.fabAddStory.setOnClickListener{
            val intent = Intent(activity, AddStoryActivity::class.java)
            intent.putExtra("extra_token_upload", token)
            startActivity(intent)
        }

        getStory()
    }

    private fun getStory() {
        rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = rvAdapter.withLoadStateFooter(footer = StoryLoadingAdapter{ rvAdapter.retry() })
        }
        viewModel.getStory(token).observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            rvAdapter.submitData(lifecycle, it)
            rvAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                override fun onItemClicked(story: Story, view: View) {
                    showSelectedStory(story, view)
                }
            })
        }
    }

    private fun showSelectedStory(story: Story, view: View) {
        val ivStory = view.findViewById<ImageView>(R.id.iv_story)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvDescStory = view.findViewById<TextView>(R.id.tvDescStory)
        val optionCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            (activity as Activity),
            Pair(ivStory, "profile"),
            Pair(tvDescStory, "desc"),
            Pair(tvName, "name"),
        )
        Intent(activity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.KEY_NAME, story.name)
            it.putExtra(DetailActivity.KEY_DATE, story.createdAt)
            it.putExtra(DetailActivity.KEY_DESC, story.description)
            it.putExtra(DetailActivity.KEY_IMAGE, story.photoUrl)

            startActivity(it, optionCompat.toBundle())
        }
    }
}