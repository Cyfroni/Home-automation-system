import socket  # Import socket module
import sys

soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # Create a socket object
host = "192.168.43.211"  # Get local machine name
port = 8080  # Reserve a port for your service.
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

        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(16).decode("ascii").split('\x00')
            del data[0]

            if data[0] == 'B':
                if data[2] == 'D':
                    print("Dark ", data[1])
                else:
                    print("Light ", data[1])
            else:
                print(data[0])
            connection.sendall(b'0')
    except Exception as err:
        print('Error:', err, sys.stderr)
    finally:
        # Clean up the connection
        print('end of connection with', client_address, sys.stderr)
        connection.close()
