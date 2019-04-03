import pandas as pd

file = pd.read_csv('train_intent.csv')
intent_df = file['intent']
intent = ['인사']
idx = 0
print('\'' + intent[0] + '\'' + ' : ' + str(idx) + ',')
for q, i in file.values:
    if i not in intent:
        intent.append(i)
        idx += 1
        print('\'' + i + '\'' + ' : ' + str(idx) + ',')
