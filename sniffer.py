#Once I had a dream and this is it.
import socket
from struct import unpack

s = socket.socket(socket.AF_PACKET, socket.SOCK_RAW, socket.ntohs(0x0003))

while True:
    packet = s.recvfrom(65565)
    packet = packet[0]
    ethernet = unpack('!6s6sH', packet[:14]) #The two 6-long strings are the two MAC addresses and the last unsigned short is the EtherType field.
    if socket.ntohs(ethernet[2]) == 8:  #Determine if IP packet or not
        extract_version = packet[14:14+1]
        ip_version_and_ihl = unpack('!B',extract_version)
        ip_version_and_ihl = socket.ntohs(ip_version_and_ihl[0])
        ip_version = ip_version_and_ihl >> 4+8
        if ip_version == 4:  #Only IPv4 packets are examined
            ihl = ip_version_and_ihl & 0b0000111100000000
            ihl = ihl >> 8
            ihl_in_bytes = ihl * 32 / 8
            extract_everything_else = packet[14:14+1+1+2+2+2+1+1+2+4+4]
            ip_header = unpack('!BBHHHBBH4s4s', extract_everything_else)
            total_length = ip_header[2]
            protocol = ip_header[6]
            source_ip = socket.inet_ntoa(ip_header[8])
            dest_ip = socket.inet_ntoa(ip_header[9])
            if protocol == 6: #TCP
                extract_tcp_header = packet[34:34+2+2+4+4+1+1+2+2+2]
                tcp_header = unpack('!HH4s4sBBHHH', extract_tcp_header)
                source_port = tcp_header[0]
                dest_port = tcp_header[1]
                sequence_number = tcp_header[2]
                data_offset = tcp_header[4]
                data_offset = data_offset & 0b11110000
                data_offset = data_offset >> 4
                data_offset = (data_offset * 4) - 20
                control_bits = tcp_header[5]
                ack_bit, syn_bit, fin_bit = False, False, False
                if control_bits & 0b00010000 != 0:
                    ack_bit = True
                if control_bits & 0b00000010 != 0:
                    syn_bit = True
                if control_bits & 0b00000001 != 0:
                    fin_bit = True
                try:
                    extract_http = packet[54+data_offset:54+data_offset+23]
                    http_header = unpack('!23s',extract_http)
                    if ('GET' in http_header[0]) & (('/audio/' in http_header[0]) | ('/head/' in http_header[0]) | ('/image/' in http_header[0])):
                        extract_http = packet[54 + data_offset:54 + data_offset + 200]
                        http_header = unpack('!200s', extract_http)
                        http_header_string = http_header[0]
                        http_header_string = http_header_string[http_header_string.find('GET ')+4:]
                        http_header_string = http_header_string[:http_header_string.find('Host:')-11]
                        http_header_string = http_header_string.replace('\n','')
                        #print "Packet start"
                        #print http_header[0]
                        print str(http_header_string)
                        #print "Packet end"
                    extract_ssl_record_layer = packet[54+data_offset:54+data_offset+5]
                    ssl_record_layer = unpack('!BHH',extract_ssl_record_layer)
                    if (ssl_record_layer[0] == 22) & (hex(int(ssl_record_layer[1])) == "0x301"):
                        extract_type = packet[59+data_offset:59+data_offset+1]
                        ssl_message_type = unpack('!B',extract_type)
                        if ssl_message_type[0] == 1:
                            extract_handshake_message = packet[59+data_offset:59+data_offset+39]
                            handshake_message = unpack('!B3sH32sB', extract_handshake_message)
                            session_id_length =  handshake_message[4]
                            ssl_lengths = session_id_length
                            extract_handshake_message = packet[98+data_offset+session_id_length:98+data_offset+session_id_length+2]
                            handshake_message = unpack('!H',extract_handshake_message)
                            cipher_suites_length = handshake_message[0]
                            ssl_lengths += cipher_suites_length
                            extract_handshake_message = packet[100+data_offset+ssl_lengths:100+data_offset+ssl_lengths+1]
                            handshake_message = unpack('!B',extract_handshake_message)
                            compression_methods_length = handshake_message[0]
                            ssl_lengths += compression_methods_length
                            extract_handshake_message = packet[101+data_offset+ssl_lengths:101+data_offset+ssl_lengths+2]
                            handshake_message = unpack('!H',extract_handshake_message)
                            extensions_length = handshake_message[0]
                            packet_size = 103+data_offset+ssl_lengths
                            #ssl_lengths += extensions_length
                            server_name_found = False
                            while packet_size < packet_size+extensions_length and not server_name_found:
                                extract_handshake_message = packet[packet_size:packet_size+4]
                                handshake_message = unpack('!HH',extract_handshake_message)
                                print handshake_message[0]
                                if handshake_message[0] == 0:
                                    server_name_found = True
                                else:
                                    packet_size += 4+handshake_message[1]
                            extract_handshake_message = packet[packet_size+4:packet_size+4+2+1+2]
                            handshake_message = unpack('!HBH',extract_handshake_message)
                            name_length = handshake_message[2]
                            extract_handshake_message = packet[packet_size+4+2+1+2:packet_size+4+2+1+2+name_length]
                            handshake_message = unpack('!'+str(name_length)+'s',extract_handshake_message)
                            print handshake_message[0]
                except Exception as e:
                    pass
                    #print e
            elif protocol == 17: #UDP
                extract_udp_header = packet[34:34+2+2+2+2]
                udp_header = unpack('!HHHH', extract_udp_header)
                source_port = udp_header[0]
                dest_port = udp_header[1]
                header_length = udp_header[2]
                print source_port, dest_port, header_length



