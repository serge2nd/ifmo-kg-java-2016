package ru.ifmo.ctddev.pistyulga.helloudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import ru.ifmo.ctddev.pistyulga.common.log.LogService;
import ru.ifmo.ctddev.pistyulga.helloudp.util.ClientServerUtil;

public class HelloUDPServer implements HelloServer {
	private static final Logger LOG = LogService.getLogger();
	
	private ExecutorService executor;
	private DatagramSocket socket;
	private Thread worker;
	private volatile boolean isClosed;

	@Override
	public void close() {
		synchronized (this) {
			isClosed = true;
			executor.shutdown();
			socket.close();
		}
	}

	@Override
	public void start(int port, int threads) {
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			LOG.severe("Cannot create socket");
			return;
		}
		
		this.executor = Executors.newFixedThreadPool(threads);
		startReceiving();
	}
	
	public void startReceiving() {
		this.worker = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isClosed) {
					DatagramPacket packet = null;
					try {
						int recBufSize = socket.getReceiveBufferSize();
						packet = new DatagramPacket(new byte[recBufSize], recBufSize);
						socket.receive(packet);
					} catch (IOException e) {
						continue;
					}
					
					executeSendingAnswer(packet);
				}
			}
		});
		worker.start();
	}
	
	private void executeSendingAnswer(DatagramPacket packet) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String answerStr = ClientServerUtil.buildAnswerStr(packet);
					socket.send(ClientServerUtil.buildPacket(
							packet.getAddress(), packet.getPort(), answerStr));
				} catch (IOException e) {}
			}
		});
	}
}
