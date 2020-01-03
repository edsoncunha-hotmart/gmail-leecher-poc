package com.hotmart.pocs.gmailleecher

import com.google.gson.Gson
import java.util.*

fun main() {
    // Notification got from Kibana: https://vpc-logs-2019-08-26-awfmwjlqwokz5y2m35w4tdu324.us-east-1.es.amazonaws.com/_plugin/kibana/app/kibana#/doc/6c032150-cfdc-11e9-9fbf-19897bd8b211/logs_container_daemon_log_api_ads_google_vlc_log_2020_01_03/logs_container_daemon_log_api_ads_google_vlc_log?id=49599990291541186214888404004369533327887666897009771042000000&_g=(refreshInterval:(pause:!t,value:0),time:(from:now-7d,mode:quick,to:now))
    val notificationRawJson = "{\"message\":{\"data\":\"eyJlbWFpbEFkZHJlc3MiOiJ0ZXN0LWhvdG1hcnRhZHMzQGhvdG1hcnQuY29tIiwiaGlzdG9yeUlkIjoxMTg5Nn0=\",\"messageId\":\"918752997874833\",\"message_id\":\"918752997874833\",\"publishTime\":\"2020-01-03T14:26:28.72Z\",\"publish_time\":\"2020-01-03T14:26:28.72Z\"},\"subscription\":\"projects/adspoc-1577964557930/subscriptions/gmail-webhook\"}"

    val gson = Gson()

    // Parse PubSub notification
    val notification = gson.fromJson(notificationRawJson, PubSubNotification::class.java)


    // Parse Gmail notification that was wrapped into PubSub envelope
    val decoder = Base64.getUrlDecoder()
    val dataJson = decoder.decode(notification.message.data).toString(Charsets.UTF_8)
    val gMailNotification = gson.fromJson(dataJson, GMailNotification::class.java)


    // Get e-mail id given a history id
    val messageId = GmailClient.getMessageIdFromHistoryId(gMailNotification.historyId.toBigInteger())

    // Finally, get the e-mail html
    val html = GmailClient.getMessageBody(messageId)

    println(html)
}
