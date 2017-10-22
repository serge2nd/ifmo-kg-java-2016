package ru.ifmo.ctddev.pistyulga.helloudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import info.kgeorgiy.java.advanced.hello.HelloClient;
import ru.ifmo.ctddev.pistyulga.common.log.LogService;
import ru.ifmo.ctddev.pistyulga.helloudp.util.ClientServerUtil;
import ru.ifmo.ctddev.pistyulga.helloudp.util.EnumeratedThreadFactory;

public class HelloUDPClient implements HelloClient {
	private static final Logger LOG = LogService.getLogger();
	
	private ExecutorService executor;
	private InetAddress addr;
	private int port;

	@Override
	public void start(String host, int port, String prefix, int requests, int threads) {
		try {
			this.addr = InetAddress.getByName(host);
			this.port = port;
		} catch (UnknownHostException e) {
			LOG.severe("Unknown host: " + host);
			return;
		}
		executor = Executors.newFixedThreadPool(threads, EnumeratedThreadFactory.getInstance(threads));
		
		for (int i = 0; i < threads; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String threadId = Thread.currentThread().getName();
					
					for (int j = 0; j < requests; j++) {
						// Build request datagram
						String requestStr = ClientServerUtil.buildRequestStr(prefix, threadId, j);
						DatagramPacket packet = ClientServerUtil.buildPacket(addr, port, requestStr);
						
						try(DatagramSocket socket = new DatagramSocket()) {
							socket.setSoTimeout(100);
							
							// Trying to send while not success
							while (true) {
								try {
									socket.send(packet);
									break;
								} catch (IOException e) {
									continue;
								}
							}
							
							// Check answer
							printResponse(socket, requestStr);
						} catch (IOException e) {
							j--;
						}
					}
				}
			});
		}
		
		// To prevent early termination
		awaitTermination();
	}
	
	private void printResponse(DatagramSocket socket, String requestStr) throws IOException {
		int recBufSize = socket.getReceiveBufferSize();
		DatagramPacket response = new DatagramPacket(new byte[recBufSize], recBufSize);
		
		socket.receive(response);
		String responseStr = ClientServerUtil.getMessageFromPacket(response);
		
		if (responseStr.equals("Hello, " + requestStr)) {
			System.out.println(responseStr);
		} else {
			throw new IOException(responseStr + " not an answer for " + requestStr);
		}
	}
	
	private void awaitTermination() {
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {}
	}
}
