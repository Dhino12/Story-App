package com.example.myapplication.utls

import com.example.myapplication.model.LoginUser
import com.example.myapplication.model.PostStory
import com.example.myapplication.model.Stories
import com.example.myapplication.model.Story
import com.example.myapplication.response.ResponseLogin
import com.example.myapplication.response.ResponseRegister

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "id + ${i.toString()}",
                "name + $i",
            "description + $i",
                i.toString(),
                "created + $i",
                i.toDouble().toString(),
                i.toDouble().toString()
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyLoginResponse(): ResponseLogin {
        return ResponseLogin(
            LoginUser(
                "name",
                "id",
                "token"
            ),
            false,
            "token"
        )
    }

    fun generateDummyRegisterResponse(): ResponseRegister {
        return ResponseRegister(
            false,
            "success"
        )
    }

    fun generateDummyStoryWithMapsResponse(): Stories {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "id + ${i.toString()}",
                "name + $i",
                "description + $i",
                i.toString(),
                "created + $i",
                i.toDouble().toString(),
                i.toDouble().toString()
            )
            items.add(story)
        }
        return Stories(
            false,
            "success",
            items
        )
    }

    fun generateDummyAddNewStoryResponse(): PostStory {
        return PostStory(
            false,
            "success"
        )
    }
}