import numpy as np
import tensorflow as tf
from tensorflow.contrib import rnn

# Training Parameters
learning_rate = 0.001
training_steps = 400
batch_size = 1
display_step = 200

# Network Parameters
num_input = 3  # horizontal vector
timesteps = 3  # vertical vector
num_hidden = 128  # hidden layer num of features 128
num_classes = 3  # total classes

# tf Graph input
X = tf.placeholder("float", [None, timesteps, num_input])
Y = tf.placeholder("float", [None, num_classes])

train_data = [
    ['피자 주문 할께', 'B-MENU O O'],
    ['페파로니 주문 해줘', 'B-MENU O O'],
    ['콜라 로 해줘', 'B-DRINK O O']
]


def make_data():
    # Convert Data
    batch_x = np.array([[1, 0, 0, 0, 1, 0, 0, 0, 1]])
    batch_y = np.array([[1, 0, 0]])
    batch_x = batch_x.reshape((batch_size, timesteps, num_input))

    return batch_x, batch_y


# 그래프 초기화
weights = {
    # Hidden layer weights => 2*n_hidden because of forward + backward cells
    'out': tf.Variable(tf.random_normal([2 * num_hidden, num_classes]))
}
biases = {
    'out': tf.Variable(tf.random_normal([num_classes]))
}

# Prepare data shape to match `rnn` function requirements
# Current data input shape: (batch_size, timesteps, n_input)
# Required shape: 'timesteps' tensors list of shape (batch_size, num_input)
# Unstack to get a list of 'timesteps' tensors of shape (batch_size, num_input)
x = tf.unstack(X, timesteps, 1)

# Define lstm cells with tensorflow
# Forward direction cell
lstm_fw_cell = rnn.BasicLSTMCell(num_hidden, forget_bias=1.0)
# Backward direction cell
lstm_bw_cell = rnn.BasicLSTMCell(num_hidden, forget_bias=1.0)

# Get lstm cell output
outputs, _, _ = rnn.static_bidirectional_rnn(lstm_fw_cell, lstm_bw_cell, x, dtype=tf.float32)

# Linear activation, using rnn inner loop last output
logits = tf.matmul(outputs[-1], weights['out']) + biases['out']
prediction = tf.nn.softmax(logits)

# Define loss and optimizer
loss_op = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=logits, labels=Y))
optimizer = tf.train.GradientDescentOptimizer(learning_rate=learning_rate)
train_op = optimizer.minimize(loss_op)

# Evaluate model (with test logits, for dropout to be disabled)
correct_pred = tf.equal(tf.argmax(prediction, 1), tf.argmax(Y, 1))
accuracy = tf.reduce_mean(tf.cast(correct_pred, tf.float32))

# Initialize the variables (i.e. assign their default value)
init = tf.global_variables_initializer()

# Start training
with tf.Session() as sess:
    # Run the initializer
    sess.run(init)

    for step in range(1, training_steps + 1):
        batch_x, batch_y = make_data()
        # Run optimization op (backprop)
        sess.run(train_op, feed_dict={X: batch_x, Y: batch_y})
        if step % display_step == 0 or step == 1:
            # Calculate batch loss and accuracy
            loss, acc = sess.run([loss_op, accuracy], feed_dict={X: batch_x,
                                                                 Y: batch_y})
            print("Step " + str(step) + ", Minibatch Loss= " + \
                  "{:.4f}".format(loss) + ", Training Accuracy= " + \
                  "{:.3f}".format(acc))

    print("Optimization Finished!")

    # Calculate accuracy for 128 mnist test images
    test_data = np.array([[1, 0, 0], [0, 1, 0], [0, 0, 1]])
    test_label = [[1, 0, 0]]
    test_data = test_data.reshape((batch_size, timesteps, num_input))

    print("Testing Accuracy:", sess.run(accuracy, feed_dict={X: test_data, Y: test_label}))
