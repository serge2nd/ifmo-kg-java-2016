package ru.ifmo.ctddev.pistyulga.helloudp.util;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ClientServerUtil {
	private ClientServerUtil() {}
	
	public static String buildRequestStr(String prefix, String threadId, int requestNum) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(prefix);
		stringBuilder.append(threadId);
		stringBuilder.append('_').append(requestNum);
		return stringBuilder.toString();
	}
	
	public static String buildAnswerStr(DatagramPacket packet) {
		return "Hello, " + getMessageFromPacket(packet);
	}
	
	public static DatagramPacket buildPacket(InetAddress addr, int port, String ansStr) {
		byte[] ansBuf = ansStr.getBytes();
		DatagramPacket answer = new DatagramPacket(ansBuf, ansBuf.length, addr, port);
		return answer;
	}
	
	public static String getMessageFromPacket(DatagramPacket packet) {
		int messageOffset = packet.getOffset();
		return new String(packet.getData(), messageOffset, messageOffset + packet.getLength());
	}
}
