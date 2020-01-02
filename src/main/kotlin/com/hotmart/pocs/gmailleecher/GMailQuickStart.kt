package com.hotmart.pocs.gmailleecher

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.math.BigInteger
import java.util.*


object GmailClient {
    private const val APPLICATION_NAME = "Gmail API Java Quickstart"
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private const val TOKENS_DIRECTORY_PATH = "tokens"
    private const val CREDENTIALS_FILE_PATH = "/credentials.json"

    private val decoder = Base64.getUrlDecoder()

    private val service = buildService()

    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential { // Load client secrets.
        val stream = GmailClient::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")

        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(stream))


        val flow = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, listOf(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY))
                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()
        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    fun getMessageBody(messageId: String) : String {
        val user = "me"
        return getMessageBody(user, messageId)
    }

    private fun getMessageBody(user: String, messageId: String): String {
        val message = service.users().messages().get(user, messageId).execute()
        return message.getHtml()
    }

    private fun buildService(): Gmail {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        return Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    private fun Message.getHtml() = this.payload.parts.joinToString { it.body.data.base64Decode() }

    private fun String.base64Decode() = decoder.decode(this).toString(Charsets.UTF_8)

    fun getMessageIdFromHistoryId(historyId: BigInteger): String {
        val user = "me"
        return getFirstHistory(user, historyId)
    }


    // a partir do historyId, devolve o ID da mensagem que foi adicionada
    private fun getFirstHistory(userId: String, startHistoryId: BigInteger) : String {
        val response = service.users()
                .history()
                .list(userId)
                .setStartHistoryId(startHistoryId)
                .setHistoryTypes(listOf("messageAdded"))
                .execute()

        return response.history.first().messagesAdded.first().message.id
    }
}


