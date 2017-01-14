package ru.ifmo.ctddev.pistyulga.helloudp;

public class HelloUDPServerMain {
	private static void checkArgs(String[] args) {
		final String USAGE = "Usage: <program> <port> <nThreads>";
		final int NARGS = 2;
		if (args.length != NARGS) {
			System.out.println(USAGE);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		checkArgs(args);
		
		int port = Integer.parseInt(args[0]),
			threads = Integer.parseInt(args[1]);
		
		try (HelloUDPServer server = new HelloUDPServer()) {
			server.start(port, threads);
		}
	}
}
