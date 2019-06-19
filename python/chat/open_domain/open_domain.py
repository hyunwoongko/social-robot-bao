# Author : Hyunwoong
# When : 5/28/2019
# Homepage : github.com/gusdnd852
import random
import urllib

import requests
from fake_useragent import UserAgent
from selenium.webdriver.firefox.options import Options

options = Options()
options.headless = True


def get_emotion(user_input) -> float:
    query = urllib.parse.quote(user_input)
    agent = UserAgent()
    header = {
        'User-Agent': agent.random,
        'referer': 'https://demo.pingpong.us'
    }
    proxies = {
        'http': 'socks5://127.0.0.1:9150',
        'https': 'socks5://127.0.0.1:9150'
    }
    url = "https://demo.pingpong.us/api/emoji.php?custom=basic&query=" + query
    response = requests.get(url, proxies=proxies, headers=header)
    txt = response.text.strip()
    print(txt)
    for i, sentiment in enumerate(txt.split('model_score')):
        if i == 2:
            sentiment = sentiment.replace('": ', '')
            sentiment = sentiment.replace('"', '')
            sent = sentiment.split(',')[0]
            return sent
    return 0.0


def exception(text):
    if '네 안녕' in text:
        return '안녕'
    else:
        return text


def open_domain(user_input):
    query = urllib.parse.quote(user_input)
    agent = UserAgent()
    header = {
        'User-Agent': agent.random,
        'referer': 'https://demo.pingpong.us'
    }
    proxies = {
        'http': 'socks5://127.0.0.1:9150',
        'https': 'socks5://127.0.0.1:9150'
    }
    url = "https://demo.pingpong.us/api/reaction.php?custom=basic&query=" + query
    response = requests.get(url, proxies=proxies, headers=header)
    txt = response.text.strip()
    answer_list = []
    txt = txt.replace('[', '')
    txt = txt.replace(']', '')
    abuse = ''
    for i, msg in enumerate(txt.split('message')):
        if 'Abusive' in msg:
            abuse = '#'
        if i == 0:
            continue
        for n in msg.split('('):
            res = n.replace('": "', '')
            if '}' not in res:
                answer_list.append(res.rstrip())
    ans = random.choice(answer_list)
    if abuse == '#':
        ans += abuse
    ans = exception(ans)
    return ans
