import io
import socket
import struct
import time
import picamera
import argparse
import logging
from PIL import Image
import threading

########################################################################################################################
logging.basicConfig(level=logging.INFO, datefmt='%m/%d/%Y %I:%M:%S %p', format='%(asctime)s - %(name)s - %(message)s')
########################################################################################################################
width = 640             # Frame width
height = 480            # Frame height
fps = 20                # Camera FPS
ip =  "192.168.219.179"#"27.116.98.204"  192.168.219.179   # WebSocket (computer) server ip address
port = 9892             # WebSocket (computer) server port
vflip = 1               # Flip frame vertically (0-False, 1-True)
hflip = 0               # Flip frame horizontally (0-False, 1-True)
timeout = 1             # Timeout for camera warmup in seconds
########################################################################################################################
parser = argparse.ArgumentParser(description='Streaming video from Raspberry Pi.')
parser.add_argument("-width", type=int, default=width, help="Width of frame.")
parser.add_argument("-height", type=int, default=height, help="Height of frame.")
parser.add_argument("-fps", type=int, default=fps, help="FPS from cam.")
parser.add_argument("-ip", type=str, default=ip, help="WebSocket (computer) server ip address.")
parser.add_argument("-port", type=int, default=port, help="WebSocket (computer) port.")
parser.add_argument("-vflip", type=int, default=vflip, help="Flip frame vertically.")
parser.add_argument("-hflip", type=int, default=hflip, help="Flip frame horizontally.")
parser.add_argument("-timeout", type=int, default=timeout, help="Timeout for camera warmup in seconds.")
args = vars(parser.parse_args())
########################################################################################################################
def stream():
        while True:
            try:
                client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                client_socket.connect((args["ip"], args["port"]))
                connection = client_socket.makefile('wb')
                logging.info("Connected to server successfully.")

                with picamera.PiCamera() as camera:
                    logging.info("Starting broadcast to "+str(args["ip"])+".")
                    vflipV = True if args["vflip"] == 1 else False
                    hflipV = True if args["hflip"] == 1 else False
                    camera.vflip = vflipV
                    camera.hflip = hflipV
                    camera.resolution = (args["width"], args["height"])
                    camera.framerate = args["fps"]
                    time.sleep(args["timeout"])
                    start = time.time()
                    stream = io.BytesIO()

                    try:
                        for foo in camera.capture_continuous(stream, 'jpeg', use_video_port=True):
                                image=Image.open(stream)
                                image.save("face.jpg")
                                connection.write(struct.pack('<L', stream.tell()))
                                connection.flush()
                                stream.seek(0)
                                connection.write(stream.read())
                                stream.seek(0)
                                stream.truncate()
                        connection.write(struct.pack('<L', 0))
                    finally:
                        pass

            except BrokenPipeError:
                time.sleep(args["timeout"])
                logging.error("Cant send frame to "+str(args["ip"])+" and reconnect for "+str(args["timeout"])+"s.")
            except ConnectionRefusedError:
                time.sleep(args["timeout"])
                logging.error("Cant connect to server, retry for "+str(args["timeout"])+"s.")
            except Exception:
                pass


streaming = threading.Thread(target=stream)
#face_thread = threading.Thread(target=faceDetect,args=(emotion_judge,emotion_late))
#face_thread.start()
streaming.start()
