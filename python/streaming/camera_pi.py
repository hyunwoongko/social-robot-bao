import time
import io
import threading
import socket
import struct
from PIL import Image



class Camera(object):
    thread = None  # background thread that reads frames from camera
    frame = None  # current frame is stored here by background thread
    last_access = 0  # time of last client access to the camera

    def initialize(self):
        if Camera.thread is None:
            # start background frame thread
            Camera.thread = threading.Thread(target=self._thread)
            Camera.thread.start()

            # wait until frames start to be available
            while self.frame is None:
                time.sleep(0)

    def get_frame(self):
        Camera.last_access = time.time()
        self.initialize()
        return self.frame

    @classmethod
    def _thread(cls):
        server_socket = socket.socket()
        server_socket.bind(('0.0.0.0', 9893))
        server_socket.listen(0)
        connection = server_socket.accept()[0].makefile('rb')

        while (True) :
                # store frame


            try:
                if(server_socket == None):
                     server_socket = socket.socket()
                     server_socket.bind(('0.0.0.0', 9893))
                     server_socket.listen(0)
                     connection = server_socket.accept()[0].makefile('rb')

                stream = io.BytesIO()
                image_len = struct.unpack('<L', connection.read(struct.calcsize('<L')))[0]



                stream.write(connection.read(image_len))
                # Rewind the stream, open it as an image with PIL and do some
                # processing on it
                stream.seek(0)
                image = Image.open(stream)

                stream.seek(0)
                cls.frame = stream.read()

                # reset stream for next frame
                stream.seek(0)
                stream.truncate()
            except:
                print("bug")
                server_socket = None
                # if there hasn't been any clients asking for frames in
                # the last 10 seconds stop the thread


        cls.thread = None