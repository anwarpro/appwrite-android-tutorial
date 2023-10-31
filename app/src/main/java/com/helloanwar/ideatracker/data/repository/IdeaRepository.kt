package com.helloanwar.ideatracker.data.repository

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.DocumentList
import io.appwrite.services.Databases

class IdeaRepository(
    private val client: Client
) {
    private val database = Databases(client)
    suspend fun getIdeas(): DocumentList<Map<String, Any>> {
        return database.listDocuments(
            databaseId = IDEAS_DATABASE_ID,
            collectionId = IDEAS_COLLECTION_ID,
            queries = listOf(
                Query.orderDesc("\$createdAt")
            )
        )
    }

    suspend fun addIdea(
        idea: Map<String, Any?>
    ) {
        database.createDocument(
            databaseId = IDEAS_DATABASE_ID,
            collectionId = IDEAS_COLLECTION_ID,
            documentId = ID.unique(),
            data = idea
        )
    }

    suspend fun updateIdea(
        idea: Map<String, Any?>,
        documentID: String
    ) {
        database.updateDocument(
            databaseId = IDEAS_DATABASE_ID,
            collectionId = IDEAS_COLLECTION_ID,
            documentId = documentID,
            data = idea
        )
    }

    suspend fun removeIdea(documentID: String) {
        database.deleteDocument(
            databaseId = IDEAS_DATABASE_ID,
            collectionId = IDEAS_COLLECTION_ID,
            documentId = documentID
        )
    }

    companion object {
        const val IDEAS_DATABASE_ID = "65407b610eccb5545839"
        const val IDEAS_COLLECTION_ID = "65407bbf57d421a8276b"
    }
}