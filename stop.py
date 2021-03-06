from googleapiclient.discovery import build
from httplib2 import Http
from oauth2client import file, client, tools

SCOPES = 'https://www.googleapis.com/auth/gmail.readonly'

def main():
   
    store = file.Storage('token.json')
    creds = store.get()
    if not creds or creds.invalid:
        flow = client.flow_from_clientsecrets('src/main/resources/credentials.json', SCOPES)
        creds = tools.run_flow(flow, store)
    service = build('gmail', 'v1', http=creds.authorize(Http()))

    request = {
    'labelIds': ['INBOX'],
    'topicName': 'projects/adspoc-1577964557930/topics/gmail-notifications'
    }

    service.users().stop(userId='me').execute()

if __name__ == '__main__':
    main()