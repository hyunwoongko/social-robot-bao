from generative_model.answer_generator import generate_answer
from hanspell.spell_checker import fix

print("입력 : ", end="")
while True:
    q = fix(input())
    a = generate_answer(q)
    print(a)
