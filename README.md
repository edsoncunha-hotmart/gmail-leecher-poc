# Prova de conceito - webhook do Google Ads

Embora o Google Ads não disponibilize um webhook que notifique eventos de campanha, ele envia e-mails com esse tipo de conteúdo. Na caixa de entrada da MCC, é possível ver e-mails sobre criação e término de campanhas e reprovação de anúncios.

O GMail, por sua vez, dispõe de um mecanismo de notificações de chegada de e-mails, por meio do Google Pub/Sub. 

Com isso, a cada notificação, é possível analisar o conteúdo do e-mail e tomar ações caso aquele conteúdo seja de interesse (um aviso de reprovação de campanha, por exemplo).

Os passos essenciais para isso são os seguintes:
1. Configurar o PubSub do GMail para enviar notificações para uma URL de interesse: https://developers.google.com/gmail/api/guides/push
    - Cadastrar um tópico no Google Pub/Sub: https://console.cloud.google.com/cloudpubsub/topic/detail/gmail-notifications?folder=&organizationId=&project=adspoc-1577964557930
    - Permitir que a conta de serviço do GMail (gmail-api-push@system.gserviceaccount.com) gere notificações nesse tópico 
    - Criar uma inscrição no tópico, do tipo PUSH, que fará POST em uma determinada URL: https://console.cloud.google.com/cloudpubsub/subscription/detail/gmail-webhook?folder=&organizationId=&project=adspoc-1577964557930
2. Colocar no ar a URL configurada no passo 1: https://raw.githubusercontent.com/Hotmart-Org/api-ads/master/google/src/main/kotlin/com/hotmart/ads/google/rest/MailNotificationsController.kt
3. Converter o conteúdo da notificação e a partir dele, recuperar o conteúdo HTML do e-mail, como demonstrado no arquivo [GmailLeecherApplication.kt](src/main/kotlin/com/hotmart/pocs/gmailleecher/GmailLeecherApplication.kt)
4. Com base no conteúdo do e-mail, gerar eventos para a aplicação, de modo que ela atualize as campanhas.
