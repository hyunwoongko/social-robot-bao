import os

styles = os.listdir('./styles')
styles_names = [i.split('.')[0] for i in styles]
