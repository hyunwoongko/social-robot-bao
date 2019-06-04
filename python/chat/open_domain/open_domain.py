# Author : Hyunwoong
# When : 5/28/2019
# Homepage : github.com/gusdnd852

import random

import emoji
import requests

from chat.util.hanspell.spell_checker import fix


def open_domain(user_name, user_input):
    user_name = str(int(hash(user_name) / 100000000000000))  # 유저 별로 고유 해시값을 4자리 수로 구함

    urls = [
        'https://builder.pingpong.us/api/builder/5cec5046e4b08517dba4a104/integration/v0.2/custom/' + user_name,
        'https://builder.pingpong.us/api/builder/5cf0c92ae4b0650704a22957/integration/v0.2/custom/' + user_name
    ]

    auths = [
        ('key', 'e5458bea6fcd5f4d0622cb8e07858f2b'),
        ('key', '0ae53003f4d99cdce7545e5f6269a7fc')
    ]

    rand = random.randint(0, len(urls) - 1)
    headers = {'Content-type': 'application/json'}
    data = '{"request":{"query" : "' + user_input + '"}}'
    data = data.encode('utf-8')
    response = requests.post(url=urls[rand], headers=headers, data=data, auth=auths[rand])
    answer_list = []
    mode = True
    print(response)
    print(urls[rand])
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
        answer_list.append(' , ')
    res = ''.join(answer_list)
    res = emoji.get_emoji_regexp().sub(u'', res)
    return fix(res)

