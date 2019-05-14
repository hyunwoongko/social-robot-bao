# Author : Hyunwoong
# When : 5/13/2019
# Homepage : github.com/gusdnd852

# doc2vec parameters
import multiprocessing


class Config:
    cores = multiprocessing.cpu_count()
    vector_size = 300
    window_size = 8
    word_min_count = 1
    sampling_threshold = 1e-5
    negative_size = 6
    train_epoch = 3000
    learning_rate = 0.02
    dm = 1  # {0:dbow, 1:dmpv}
    worker_count = cores  # number of parallel processes

    # root_path = './'
    root_path = './chat/doc2vec/'
    train_path = root_path + "train_doc2vec.csv"
    test_path = root_path + 'test_doc2vec.csv'
    model_path = root_path + 'model/'
    modelfile = model_path + "doc2vec_model"
