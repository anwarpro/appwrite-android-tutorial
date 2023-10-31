package com.helloanwar.ideatracker

import android.content.Context
import com.helloanwar.ideatracker.data.repository.IdeaRepository
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.Session
import io.appwrite.models.User
import io.appwrite.services.Account

object Appwrite {
    lateinit var client: Client
    lateinit var account: Account
    lateinit var ideaRepository: IdeaRepository

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject("654078c16d6872584c53")

        account = Account(client)
        ideaRepository = IdeaRepository(client)
    }

    suspend fun onLogin(
        email: String,
        password: String,
    ): Session {
        return account.createEmailSession(
            email,
            password,
        )
    }

    suspend fun onRegister(
        name: String,
        email: String,
        password: String,
    ): User<Map<String, Any>> {
        return account.create(
            userId = ID.unique(),
            email = email,
            password = password,
            name = name
        )
    }

    suspend fun onLogout() {
        account.deleteSession("current")
    }
}