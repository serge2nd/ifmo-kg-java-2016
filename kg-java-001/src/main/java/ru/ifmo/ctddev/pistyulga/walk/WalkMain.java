package ru.ifmo.ctddev.pistyulga.walk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.ctddev.pistyulga.hash.MD5LowMemoryHasher;
import ru.ifmo.ctddev.pistyulga.log.LogService;

public class WalkMain {
	private static final Logger LOG = LogService.getLogger(WalkMain.class.getName());
	
	private static final String ENCODING = "UTF-8";
	
	private static void checkArgs(String[] args) {
		final int N_ARGS = 2;
		final String USAGE = "Usage: <program> <file_list_path> <output_file>";
		if (args.length != N_ARGS) {
			System.out.println(USAGE);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		/*checkArgs(args);
		String pathsFilePath = args[0],
				outputFilePath = args[1];
		
		File pathsFile = new File(pathsFilePath);
		if (!pathsFile.isFile()) {
			LOG.severe("File '" + pathsFilePath + "' not found");
			System.exit(1);
		}
		
		LOG.info("Reading file list...");
		
		try(InputStream pathsInputStream = new FileInputStream(pathsFile);
			Writer checksumWriter = new PrintWriter(outputFilePath, ENCODING))
		{
			Walk.walk(pathsInputStream, checksumWriter, ENCODING);
		} catch(IOException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}*/
		
		System.out.println(new MD5LowMemoryHasher()
								.processByte((byte)0x6D)
								.processByte((byte)0x64)
								.processByte((byte)0x35)
								.finish());
	}
}
