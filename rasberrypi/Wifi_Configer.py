"""
Search for a specific wifi ssid and connect to it.
written by kasramvd.
"""
import os


class Finder:
    def __init__(self, *args, **kwargs):
        self.server_name = kwargs['server_name']
        self.password = kwargs['password']
        self.interface_name = kwargs['interface']
        self.main_dict = {}

    def run(self):
        command = """sudo iwlist wlan0 scan | grep -ioE 'ssid:"(.*{}.*)'"""
        result = os.popen(command.format(self.server_name))
        result = list(result)

        if "Device or resource busy" in result:
                return None
        else:
            ssid_list = [item.lstrip('SSID:').strip('"\n') for item in result]
            print("Successfully get ssids {}".format(str(ssid_list)))

        for name in ssid_list:
            try:
                result = self.connection(name)
            except Exception as exp:
                print("Couldn't connect to name : {}. {}".format(name, exp))
            else:
                if result:
                    print("Successfully connected to {}".format(name))

    def connection(self, name):
        try:
            print("wpa_passphrase {} {} >> /etc/wpa_supplicant/wpa_supplicant.conf".format(name,
        self.password))
            os.system("sudo chmod 777 /etc/wpa_supplicant/wpa_supplicant.conf")
            os.system("wpa_passphrase {} {} >> /etc/wpa_supplicant/wpa_supplicant.conf".format(name,
        self.password))
            os.system("sudo /etc/init.d/networking restart")    
        except:
            raise
        else:
            return True

if __name__ == "__main__":
    # Server_name is a case insensitive string, and/or regex pattern which demonstrates
    # the name of targeted WIFI device or a unique part of it.
    server_name = "Hello"
    password = "seven8529741"
    interface_name = "wlan0" # i. e wlp2s0  
    F = Finder(server_name=server_name,
               password=password,
               interface=interface_name)
    F.run()