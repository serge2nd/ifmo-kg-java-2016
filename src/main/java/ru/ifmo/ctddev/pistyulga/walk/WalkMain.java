package ru.ifmo.ctddev.pistyulga.walk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.ctddev.pistyulga.common.hash.LowMemHasher;
import ru.ifmo.ctddev.pistyulga.common.hash.MD5LowMemHasher;
import ru.ifmo.ctddev.pistyulga.common.log.LogService;

public class WalkMain {
	private static final Logger LOG = LogService.getLogger();
	
	private static final String ENCODING = "UTF-8";
	
	private static void checkArgs(String[] args) {
		final int N_ARGS = 2;
		final String USAGE = "Usage: <program> <file_list_path> <output_file>";
		if (args.length != N_ARGS) {
			System.out.println(USAGE);
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		checkArgs(args);
		
		String pathsFilePath = args[0],
				outputFilePath = args[1];
		
		File pathsFile = new File(pathsFilePath);
		if (!pathsFile.isFile()) {
			LOG.severe("File '" + pathsFilePath + "' not found");
			System.exit(1);
		}
		
		LOG.info("Processing file list...");
		LowMemHasher hasher = new MD5LowMemHasher();
								//new FNV1ALowMemHasher(Size._128BIT);
		
		try(InputStream pathsInputStream = new FileInputStream(pathsFile);
			Writer hashInfoWriter = new PrintWriter(outputFilePath, ENCODING))
		{
			Walk.walk(hasher, pathsInputStream, hashInfoWriter, ENCODING);
		} catch(IOException e) {
			LOG.log(Level.SEVERE,"I/O error while reading file paths: " + e.getMessage(), e);
			System.exit(1);
		}
		
		LOG.finest("Done.");
	}
}
