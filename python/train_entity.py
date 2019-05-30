# Author : Hyunwoong
# When : 5/30/2019
# Homepage : github.com/gusdnd852
from chat.entity.alarm.entity_recognizer import get_alarm_entity
from chat.util.hanspell.spell_checker import fix
from chat.util.tokenizer import tokenize

res = get_alarm_entity("오늘 알람 맞출래", True)

print("입력해주세요 : ")
while True:
    q = fix(tokenize(fix(input())))
    res = get_alarm_entity(q, False)
    print(res)
