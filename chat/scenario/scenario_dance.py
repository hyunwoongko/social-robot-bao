import random


def response():
    response_list = ['이 댄스는 어때요', '이 춤 어떤가요', '저 춤 잘추죠?', '댄스 댄스 !', '저 처럼 춤을 춰봐요']
    return random.choice(response_list)
