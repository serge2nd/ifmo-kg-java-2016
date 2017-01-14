package ru.ifmo.ctddev.pistyulga.helloudp;

public class HelloUDPClientMain {
	private static void checkArgs(String[] args) {
		final String USAGE = "Usage: <program> <host> <port> <prefix> <nRequests> <nThreads>";
		final int NARGS = 5;
		if (args.length != NARGS) {
			System.out.println(USAGE);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		checkArgs(args);
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String prefix = args[2];
		int requests = Integer.parseInt(args[3]);
		int threads = Integer.parseInt(args[4]);
		
		HelloUDPClient client = new HelloUDPClient();
		client.start(host, port, prefix, requests, threads);
	}
}
