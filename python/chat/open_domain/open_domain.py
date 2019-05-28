# Author : Hyunwoong
# When : 5/28/2019
# Homepage : github.com/gusdnd852

import emoji
import requests


def open_domain(user_name, user_input):
    user_name = str(int(hash(user_name) / 100000000000000))  # 유저 별로 고유 해시값을 4자리 수로 구함
    url = 'https://builder.pingpong.us/api/builder/5cec5046e4b08517dba4a104/integration/v0.2/custom/' + user_name
    headers = {'Content-type': 'application/json'}
    data = '{"request":{"query" : "' + user_input + '"}}'
    data = data.encode('utf-8')
    auth = ('key', 'e5458bea6fcd5f4d0622cb8e07858f2b')
    response = requests.post(url=url, headers=headers, data=data, auth=auth)
    answer_list = []
    mode = True
    for i in response.json()['response']['replies']:
        try:
            for c in i['text']:
                if c == '(':
                    mode = False
                if mode:
                    answer_list.append(c)
                if c == ')':
                    mode = True
        except:
            pass
        answer_list.append(' ')
    res = ''.join(answer_list)
    res = emoji.get_emoji_regexp().sub(u'', res)
    return res
