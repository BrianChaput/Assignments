'''
send and recv functions implementing the chatroom protocol
'''

import struct
import sys
import protocol
import crypt

def send(s, message='', id=None, key=None):
    utf = message

    if not isinstance(message, (bytes, bytearray)):
        utf = message.encode()

    code = protocol.PACKETS[id]

    # Recommended location for symmetric encryption to be implemented as a block cipher
    if key is not None:
        utf = crypt.symmetric_encrypt(key, utf)
        

    payload = struct.pack(
        '>iI{}s'.format(len(utf)),
        code,
        len(utf),
        utf
    )

    s.send(payload)


def recv(s, data):
    if s not in data.msg_buffers:
        data.msg_buffers[s] = b''
        data.recv_len[s] = 0

    try:
        msg = s.recv(1)
    except BaseException:
        del data.msg_buffers[s]
        del data.recv_len[s]

        if s in data.msg_len:
            del data.msg_len[s]

        return 'LOADING_MSG'

    if not msg:
        data.msg_buffers[s] = None
        data.msg_len[s] = 0

        return 'ERROR'

    data.msg_buffers[s] += msg
    data.recv_len[s] += 1

    # Check if we have received the first 8 bytes.
    if s not in data.msg_len and data.recv_len[s] == 8:
        packet_data = struct.unpack('>iI', data.msg_buffers[s])

        code = packet_data[0]
        length = packet_data[1]

        data.msg_buffers[s] = b''
        data.msg_len[s] = length
        data.msg_ids[s] = {v: k for k, v in protocol.PACKETS.items()} [code]



    # Check if the message is done buffering.
    if s in data.msg_len and len(data.msg_buffers[s]) == data.msg_len[s]:
        return 'MSG_CMPLT'

    return 'LOADING_MSG'


def get_msg_from_queue(s, data):
    recv_str = data.msg_buffers[s]
    ret_str = ''

    if recv_str is not None:

        # Recommended spot for decryption of symmetric cipher
        if s in data.symmetric_keys and data.symmetric_keys[s]:
            recv_str = crypt.symmetric_decrypt(data.symmetric_keys[s], recv_str)

        try:
            ret_str = recv_str.decode()
        except BaseException:
            ret_str = recv_str

    del data.msg_buffers[s]
    del data.recv_len[s]
    del data.msg_len[s]

    id = None

    if s in data.msg_ids:
        id = data.msg_ids[s]
        del data.msg_ids[s]

    return id, ret_str

class RECV_DATA:
    msg_buffers = {}
    recv_len = {}
    msg_len = {}
    msg_ids = {}
    symmetric_keys = {}
