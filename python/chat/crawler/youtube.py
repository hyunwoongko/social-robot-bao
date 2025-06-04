import random
import urllib
from urllib.request import urlopen

import bs4


def get_youtube(text, language='en'):
    # TODO: Adapt YouTube search for the given language.
    # This might involve changing search query suffixes (e.g., 'song' vs 'canción')
    # or using YouTube API features for region/language if available.
    # For now, using default behavior (likely Korean suffix).
    if language == 'es':
        # Placeholder for Spanish specific logic
        pass # Using default for now
    elif language == 'pt':
        # Placeholder for Portuguese specific logic
        pass # Using default for now
    # Existing logic...
    # 'text' parameter was 'song_name'
    # The suffix "노래 mp3 듣기" is Korean for "song mp3 listening"
    # This should ideally be localized or made generic.
    suffix = ' 노래 mp3 듣기' # Default Korean suffix
    if language == 'es':
        suffix = ' cancion mp3 escuchar' # Example for Spanish
    elif language == 'pt':
        suffix = ' musica mp3 ouvir' # Example for Portuguese

    parsed_song = urllib.parse.quote(text + suffix) # song_name -> text
    url = 'https://www.youtube.com/results?sp=EgIYAQ%253D%253D&search_query=' + parsed_song
    html_doc = urlopen(url)
    soup = bs4.BeautifulSoup(html_doc, 'html.parser')
    link = soup.findAll('div', attrs={'class': 'yt-lockup-dismissable'})
    idx = random.randint(0, 8)
    link = 'https://www.youtube.com/' + link[idx].find('h3').find('a')['href']

    # _filename = 'song'
    # YouTube(link).streams.first().download(filename=_filename)
    # mp4 다운로드 코드 
    return link
