import urllib
from urllib.request import urlopen

import bs4


def get_youtube_video(song_name):
    parsed_song = urllib.parse.quote(song_name + ' mp3 듣기')
    url = 'https://www.youtube.com/results?sp=EgIYAQ%253D%253D&search_query=' + parsed_song
    html_doc = urlopen(url)
    soup = bs4.BeautifulSoup(html_doc, 'html.parser')
    link = soup.findAll('div', attrs={'class': 'yt-lockup-dismissable'})
    link = 'https://www.youtube.com/' + link[0].find('h3').find('a')['href']
    # _filename = 'song'
    # YouTube(link).streams.first().download(filename=_filename)
    # mp4 다운로드 코드 
    return link


get_youtube_video('shape of you')
