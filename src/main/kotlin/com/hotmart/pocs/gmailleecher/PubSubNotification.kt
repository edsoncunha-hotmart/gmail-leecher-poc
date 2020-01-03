package com.hotmart.pocs.gmailleecher

import java.time.LocalDateTime

data class PubSubNotification (val message: PubSubMessage, val subscription: String)

data class PubSubMessage(val data: String, val messageId: String)