import argparse
import socket
import select
import queue
import sys
import LNP
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes

# Load client private key
with open("alice.cert", "rb") as key_file:
    client_private_key = serialization.load_pem_private_key(
        key_file.read(),
        password=None,
    )

# Function to decrypt the symmetric key using client's private key
def decrypt_symmetric_key(encrypted_key):
    symmetric_key = client_private_key.decrypt(
        encrypted_key,
        padding.OAEP(
            mgf=padding.MGF1(algorithm=hashes.SHA256()),
            algorithm=hashes.SHA256(),
            label=None
        )
    )
    return symmetric_key


def get_args():
    '''
    Gets command line arguments.
    '''

    parser = argparse.ArgumentParser()

    parser.add_argument(
        "--port",
        metavar='p',
        dest='port',
        help="port number",
        type=int,
        default=42069
    )

    parser.add_argument(
        "--ip",
        metavar='i',
        dest='ip',
        help="IP address for client",
        default='127.0.0.1'
    )

    return parser.parse_args()

#Main method
def main():
    '''
    uses a select loop to process user and server messages. Forwards user input to the server.
    '''

    args = get_args()
    server_addr = args.ip
    port = args.port

    server = socket.socket()
    server.connect((server_addr, port))

    data = LNP.RECV_DATA()

    inputs = [server, sys.stdin]
    outputs = [server]
    message_queue = queue.Queue()

    waiting_accept = True
    username = ''

    while server in inputs:

        #wait for a stream to become readable, writable, or have an error
        readable, writable, exceptional = select.select(inputs, outputs, inputs)

        if server in readable:
            s = server
            ###
            ### Process server messages
            ###
        
            # This point may iterate multiple times until the message is completely read 
            #    since LNP.recv, receives a few bytes at a time.
            code = LNP.recv(s, data)
            # This will not happen until the message is switched to MSG_COMPLETE when then
            #    it is read from the buffer.
            match code:
                case "ERROR": # Probably the server crashed
                    print("Error: Server disconnected")
                    return 1 # Exit the program
                case "LOADING_MSG": # message is still being read...
                    pass
                case "MSG_CMPLT": # message is fully loaded and ready to be processed
                    code_id, msg = LNP.get_msg_from_queue(s, data)
                    
                    if code_id is not None: # If not a message
                        print("\tcode_id: " + code_id)

                    match code_id:
                        case None: # on message
                            if msg: # Read message from server
                                #If username exists, add message prompt to end of message
                                if username != '':
                                    sys.stdout.write('\r' + msg + '\n')
                                    sys.stdout.write("> " + username + ": ")

                                #If username hasn't been set, print message
                                else:
                                    sys.stdout.write(msg)
                                    print("\t#Empty username, How?")
                                    exit(2)
                                sys.stdout.flush()

                        case "ACCEPT":
                            waiting_accept = False
                            sys.stdout.write(msg)
                            sys.stdout.flush()

                        case "USERNAME-INVALID" | "USERNAME-TAKEN":
                            sys.stdout.write(msg)
                            sys.stdout.flush()

                        case "USERNAME-ACCEPT":
                            print("\t#Username-Accepted") # You have joined the chat w/ a username
                            username = msg

                        case "NO_MSG" | "EXIT":
                            sys.stdout.write(msg + '\n')
                            sys.stdout.flush()
                            inputs.remove(s)
                            if s in writable:
                                writable.remove(s)
                    
        ###
        ### Process user input
        ###
        if sys.stdin in readable:

            msg = sys.stdin.readline()

            if not waiting_accept:
                msg = msg.rstrip()
                if msg:
                    message_queue.put(msg)
                if not ((username == '') or (msg == "exit()")):
                    sys.stdout.write("> " + username + ": ")
                    sys.stdout.flush()

        ###
        ### Send messages to server
        ###
        for s in writable:

            try:
                msg = message_queue.get_nowait()
            except queue.Empty:
                msg = None

	 #if there is a message to send
            if msg:

	     #if exit message, send the exit code
                if msg == "exit()":
                    LNP.send(s, '', "EXIT")
                    outputs.remove(s)
                    inputs.remove(s)
	     #otherwise just send the message
                else:
                    LNP.send(s, msg)

        for s in exceptional:
            print("Disconnected: Server exception")
            inputs.remove(s)

    server.close()

if __name__ == '__main__':
    main()
