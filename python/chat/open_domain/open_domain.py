# Author : Hyunwoong
# When : 5/28/2019
# Homepage : github.com/gusdnd852
import random
import urllib

from fake_useragent import UserAgent
from selenium import webdriver
from selenium.webdriver.firefox.firefox_profile import FirefoxProfile
from selenium.webdriver.firefox.options import Options

options = Options()
options.headless = True
ua = UserAgent()
options.add_argument('user-agent=' + ua.random)


def get_emotion(user_input) -> float:
    query = urllib.parse.quote(user_input)
    profile = FirefoxProfile(r"C:\Tor\Browser\TorBrowser\Data\Browser\profile.default")
    profile.set_preference("general.useragent.override", ua.random)

    driver = webdriver.Firefox(firefox_options=options, firefox_profile=profile,
                               executable_path=r'chat/open_domain/fox.exe')
    driver.get("https://demo.pingpong.us/api/emoji.php?custom=basic&query=" + query)
    txt = driver.page_source
    driver.quit()
    for i, sentiment in enumerate(txt.split('model_score')):
        if i == 2:
            sentiment = sentiment.replace('": ', '')
            sentiment = sentiment.replace('"', '')
            sent = sentiment.split(',')[0]
            return sent
    return 0.0


def open_domain(user_input) -> str:
    query = urllib.parse.quote(user_input)
    profile = FirefoxProfile(r"C:\Tor\Browser\TorBrowser\Data\Browser\profile.default")
    profile.set_preference("general.useragent.override", ua.random)
    driver = webdriver.Firefox(firefox_options=options, firefox_profile=profile,
                               executable_path=r'chat/open_domain/fox.exe')
    driver.get("https://demo.pingpong.us/api/reaction.php?custom=basic&query=" + query)
    txt = driver.page_source
    driver.quit()
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


def exception(text):
    if '네 안녕' in text: return '안녕'
    else: return text;
