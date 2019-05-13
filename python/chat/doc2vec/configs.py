# Author : Hyunwoong
# When : 5/13/2019
# Homepage : github.com/gusdnd852

# doc2vec parameters
import multiprocessing


class Config:
    cores = multiprocessing.cpu_count()
    vector_size = 300
    window_size = 4
    word_min_count = 1
    sampling_threshold = 1e-5
    negative_size = 5
    train_epoch = 1300
    dm = 1  # {0:dbow, 1:dmpv}
    worker_count = cores  # number of parallel processes

    # root_path = './'
    root_path = './chat/doc2vec/'
    train_path = root_path + "train_doc2vec.csv"
    test_path = root_path + 'test_doc2vec.csv'
    model_path = root_path + 'model/'
    modelfile = model_path + "doc2vec_model"