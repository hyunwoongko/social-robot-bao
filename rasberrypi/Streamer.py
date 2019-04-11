import io
import socket
import struct
import time
import picamera
import threading
from PIL import Image

# Connect a client socket to my_server:8000 (change my_server to the
# hostname of your server)

def saveImage(stream):
    time.sleep(0.5)
    image=Image.open(stream)
    image.save("face.mjpeg")
    print("saveImage")    


def stream():
 
 
    client_socket = socket.socket()
    print("loop")
    client_socket.connect(('192.168.219.179', 9892))
    #client_socket.connect(('192.168.219.179/eye/user',80))
    # Make a file-like object out of the connection
    #connection = client_socket.makefile('wb')
    while(True):
     try:
        with picamera.PiCamera() as camera:
            if(client_socket == None):
                client_socket = socket.socket()
                client_socket.connect(('192.168.219.179', 38000))
               # client_socket.connect(('http://192.168.219.179/eye/userid',80))
            connection = client_socket.makefile('wb')
    
            camera.resolution = (640, 480)
            # Start a preview and let the camera warm up for 2 seconds
            camera.start_preview()
            # Note the start time and construct a stream to hold image data
            # temporarily (we could write it directly to connection but in this
            # case we want to find out the size of each capture first to keep
            # our protocol simple)
            
            start = time.time()
            stream = io.BytesIO()
            for foo in camera.capture_continuous(stream, 'jpeg'):
                # Write the length of the capture to the stream and flush to
                # ensure it actually gets sent
                
                image=Image.open(stream)
                image.save("face.jpg")
                #stream.seek(0)
                #stream.seek(0)
                #stream.truncate()
                try:
    #print("test"+stream)
                    connection.write(struct.pack('<L', stream.tell()))
                    connection.flush()
                    # Rewind the stream and send the image data over the wire
                    stream.seek(0)
                    connection.write(stream.read())
                    # If we've been capturing for more than 30 seconds, quit
                    
                    # Reset the stream for the next capture
                    stream.seek(0)
                    stream.truncate()
                except:
                   pass

    # Write a length of zero to the stream to signal we're done
        connection.write(struct.pack('<L', 0))
        
     except:
         connection = None
         client_socket =None
         print("exception")


streaming = threading.Thread(target=stream)
#face_thread = threading.Thread(target=faceDetect,args=(emotion_judge,emotion_late))
#face_thread.start()
streaming.start()
