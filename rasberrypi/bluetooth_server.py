import os
import subprocess
import select
import serial
import time
import json
import re
import Command
import threading

wpa_supplicant_conf = "/etc/wpa_supplicant/wpa_supplicant.conf"
sudo_mode = "sudo "

command =""

class SerialComm:
    
    @classmethod
    def __getInstance(cls):
        return cls.__instance

    @classmethod
    def instance(cls, *args, **kargs):
        cls.__instance = cls(*args, **kargs)
        cls.instance = cls.__getInstance
        return cls.__instance

    def __init__(self):
        self.port = serial.Serial("/dev/rfcomm0", baudrate=9600, timeout=1)

    def read_serial(self):
        res = self.port.read(50)
        if len(res):
            return res.splitlines()
        else:
            return []

    def send_serial(self, text):
        self.port.write(text)

    def is_json(self, mJson):
        try:
            print(type(mJson),mJson)
            json_object = json.loads(mJson)
            if isinstance(json_object, int):
                return False

            if len(json_object) == 0:
                
                return False
        except ValueError as e:
            print(type(mJson),mJson)
            return False
        return True

    def isValidCommand(self, command, invalidCommand):
        if command not in invalidCommand:
            if re.match("^[a-zA-Z0-9. -]+$", command):
                return True

        return False

    def readExecuteSend(self, shell, ble_comm, ble_line):

        json_object = json.loads(ble_line)
        ip_address = ble_comm.wifi_connect(
            json_object['SSID'], json_object['PWD'])
        if ip_address == "<Not Set>":
            print("Fail to connect to Internet")
            # send back fail to configure wifi
            callback_message = {'result': "FAIL", 'IP': ip_address}
            callback_json = json.dumps(callback_message)
            ble_comm.send_serial(callback_json)
            return False

        else:
            #isConnected = True
            print("connect to Internet! your ip_address: " + ip_address)
            # send back configure wifi succesfully
            callback_message = {'result': "SUCCESS", 'IP': ip_address}
            callback_json = json.dumps(callback_message)
            ble_comm.send_serial(callback_json)

            return True

    def wifi_connect(self, ssid, psk):
        # write wifi config to file
        cmd = ['pirateship', 'wifi', ssid, psk]
        pirateship_wifi_output = subprocess.check_output(cmd)
        self.send_serial(pirateship_wifi_output)

        p = subprocess.check_output(['ifconfig', 'wlan0'])
        ip_address = "<Not Set>"

        for l in out.split('\n'):
            if l.strip().startswith("inet addr:"):
                ip_address = l.strip().split(' ')[1].split(':')[1]

        return ip_address


class ShellWrapper:
    def __init__(self):
        self.ps = subprocess.Popen(
            ['bash'],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            stdin=subprocess.PIPE)

    def execute_command(self, command):
        self.ps.stdin.write(command + "\n")

    def get_output(self):
        timeout = False
        time_limit = .5
        lines = []
        while not timeout:
            poll_result = select.select(
                [self.ps.stdout, self.ps.stderr], [], [], time_limit)[0]
            if len(poll_result):

                for p in poll_result:
                    lines.append(p.readline())
            else:
                timeout = True

        if(len(lines)):
            return lines
        else:
            return None



def start():
    global command
    
    shell = ShellWrapper()
    invalidCommand = []
    ble_comm = None
    isConnected = False
    print("startserver")
    

    while True:
      
        try:
            ble_comm = SerialComm().instance()
            out = ble_comm.read_serial()
            for ble_line in out:
                print(ble_line.decode('utf-8'))
                command =ble_line.decode('utf-8')
                Command.StartCommand(command)
                
        except serial.SerialException:
            print("waiting for connection")
            ble_comm = None
            isConnected = False
            time.sleep(3)
            


server_thread = threading.Thread(target=start)
server_thread.daemon = True
server_thread.start()
