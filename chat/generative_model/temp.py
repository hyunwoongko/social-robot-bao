import pandas as pd

count = 0
msg = ''
data = pd.read_csv('data_in/ChatBotData.csv')

for i in data['A'].values:
    if len(i) > count:
        count = len(i)
        msg = i

print(count)
print(msg)