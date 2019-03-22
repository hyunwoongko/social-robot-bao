import api.api_youtube as song


def response(entity):
    keyword_group = entity[0]
    entity_group = entity[1]

    words_list = []

    for k in zip(keyword_group, entity_group):
        if k[1] != 'O':
            words_list.append(k[0])

    return song.get_youtube(' '.join(words_list))
