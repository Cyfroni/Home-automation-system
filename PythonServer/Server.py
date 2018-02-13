# coding=utf-8
# import the library
import os
import socket
import sys

if os.name != 'nt':
    from pyA20.gpio import gpio
    from pyA20.gpio import port

    OUTS = [port.PG7, port.PG6, port.PA20, port.PG9, port.PA10, port.PA9, port.PG8, port.PA8]


    def light(i):
        gpio.output(OUTS[i], gpio.LOW)


    def dark(i):
        gpio.output(OUTS[i], gpio.HIGH)


    def init():
        gpio.init()
        for i in range(8):
            gpio.setcfg(OUTS[i], gpio.OUTPUT)
            dark(i)


    init()
    host = "192.168.0.101"
else:
    def light(i):
        print("Light:", i)


    def dark(i):
        print("Dark:", i)


    host = "192.168.0.13"

soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # Create a socket object
port = 8080
server_address = (host, port)
print('starting up on %s port %s' % server_address, sys.stderr)
soc.bind(server_address)  # Bind to the port
soc.listen(1)  # Now wait for client connection.

while True:
    # Wait for a connection
    print('waiting for a connection', sys.stderr)
    connection, client_address = soc.accept()  # Establish connection with client.
    try:
        print('connection from', client_address, sys.stderr)
        connection.sendall(b'1')
        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(16).decode("ascii").split('\x00')
            del data[0]

            if data[0] == 'B':
                if data[2] == 'D':
                    dark(int(data[1]))
                else:
                    light(int(data[1]))
            elif "".join(data) == "SHD":
                # connection.sendall(b'1')
                connection.close()
                if os.name == "nt":
                    exit(0)
                else:
                    os.system("sudo shutdown -h now")

            connection.sendall(b'0')

    except Exception as err:
        print('Error:', err, sys.stderr)
    finally:
        # Clean up the connection
        print('end of connection with', client_address, sys.stderr)
        connection.close()
