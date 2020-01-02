package com.hotmart.pocs.gmailleecher

//import org.springframework.boot.autoconfigure.SpringBootApplication
//
//@SpringBootApplication
//class GmailLeecherApplication

fun main(args: Array<String>) {
	val historyId = 11400

	val messageId = GmailClient.getMessageIdFromHistoryId(historyId.toBigInteger())

	val html = GmailClient.getMessageBody(messageId)

	println(html)

	//runApplication<GmailLeecherApplication>(*args)
}
