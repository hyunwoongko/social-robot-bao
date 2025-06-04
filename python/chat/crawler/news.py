import random
import re
from urllib.request import Request, urlopen

import bs4
import requests
from bs4 import BeautifulSoup


def get_keyword_news(keyword, language='en'):
    # TODO: Adapt keyword news search for the given language.
    # This might involve using language-specific news search engines/APIs
    # or translating keywords if the API doesn't support language parameters.
    # For now, using default behavior (Naver search, likely Korean).
    if language == 'es':
        # Placeholder for Spanish specific logic (e.g., use Google News with region)
        # raw = requests.get('https://news.google.com/search?q=' + keyword + '&hl=es', headers={'User-Agent': 'Mozilla/5.0'}).text
        pass # Using default for now
    elif language == 'pt':
        # Placeholder for Portuguese specific logic
        pass # Using default for now
    # Existing logic...

    journal_articles = {}
    result = []

    # Current implementation uses Naver, which is Korean.
    # This part needs to be conditional on language if other sources are used.
    search_url_base = 'https://search.naver.com/search.naver?&where=news&query='

    for i in range(3):
        raw = requests.get(
            search_url_base + keyword + '&start=' + str(i * 10 + 1),
            headers={'User-Agent': 'Mozilla/5.0'}).text
        html = BeautifulSoup(raw, 'html.parser')
        articles = html.select('.type01 > li')

        for article in articles:
            journal = article.select_one('span._sp_each_source').text
            title = article.select_one('a._sp_each_title').text

            if journal in journal_articles.keys():
                journal_articles[journal].append(title)
            else:
                journal_articles[journal] = [title]

    items = list(journal_articles.items())
    random.shuffle(items)

    idx = 1
    for item in items:
        if '포토' not in item[1][0] and len(result) < 5:
            res = re.sub('\"', ' ', item[1][0])
            res = re.sub('\'', ' ', res)
            res = re.sub('“', ' ', res)
            res = re.sub('”', ' ', res)
            res = re.sub('‘', ' ', res)
            res = re.sub('’', ' ', res)
            res = re.sub('\\[', ' ', res)
            res = re.sub('\\]', ' ', res)
            res = re.sub(' {2}', ' ', res)
            result.append(str(idx) + '번째 뉴스 , ' + res)
            idx += 1

    return ' , '.join(result)


def get_news():
    url = 'https://news.naver.com/'
    req = Request(url)
    page = urlopen(req)
    html = page.read()
    soup = bs4.BeautifulSoup(html, 'html.parser')
    result = []
    idx = 1
    raw1 = soup.find_all('div', {'class': 'newsnow'})[0].find_all('strong')
    raw2 = soup.find_all('div', {'class': 'newsnow'})[1].find_all('strong')
    for i in raw1:
        txt = i.text
        txt = re.sub('동영상기사', '', txt)
        txt = re.sub('\n', '', txt)
        txt = re.sub('\"', ' ', txt)
        txt = re.sub('\'', ' ', txt)
        txt = re.sub('“', ' ', txt)
        txt = re.sub('”', ' ', txt)
        txt = re.sub('‘', ' ', txt)
        txt = re.sub('’', ' ', txt)
        txt = re.sub('\\[', ' ', txt)
        txt = re.sub('\\]', ' ', txt)
        txt = re.sub(' {2}', ' ', txt)
        if txt.strip() != '':
            result.append(str(idx) + '번째 뉴스 , ' + txt)
            idx += 1

    for i in raw2:
        txt = i.text
        txt = re.sub('동영상기사', '', txt)
        txt = re.sub('\n', '', txt)
        txt = re.sub('\"', ' ', txt)
        txt = re.sub('\'', ' ', txt)
        txt = re.sub('“', ' ', txt)
        txt = re.sub('”', ' ', txt)
        txt = re.sub('‘', ' ', txt)
        txt = re.sub('’', ' ', txt)
        txt = re.sub('\\[', ' ', txt)
        txt = re.sub('\\]', ' ', txt)
        txt = re.sub(' {2}', ' ', txt)
        if txt.strip() != '':
            result.append(str(idx) + '번째 뉴스 , ' + txt)
            idx += 1
    result = ' , '.join(result)
    return result
