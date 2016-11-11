package ru.ifmo.ctddev.pistyulga.walk;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.ctddev.pistyulga.hash.HashUtil;
import ru.ifmo.ctddev.pistyulga.hash.LowMemHasher;
import ru.ifmo.ctddev.pistyulga.hash.MD5LowMemHasher;
import ru.ifmo.ctddev.pistyulga.log.LogService;

public class Walk {
	private static final Logger LOG = LogService.getLogger();
	
	private Walk() {}
	
	/**
	 * Reads file paths from {@code pathsInputStream} and writes file hashes to {@code hashInfoWriter}.
	 * If given file is a directory, all files and directories in it are being considered recursively.
	 * @param hasher - an implementation of hash algorithm
	 * @param pathsInputStream - a stream containing file paths
	 * @param hashInfoWriter - a destination of hash info
	 * @param encoding - a charset name in which file paths are read
	 * @throws IOException - when I/O error occurred while reading file path or writing hash info
	 */
	public static void walk(
			LowMemHasher hasher, InputStream pathsInputStream,
			Writer hashInfoWriter, String encoding)
			throws IOException
	{
		try(BufferedReader pathsReader =
				new BufferedReader(new InputStreamReader(pathsInputStream, encoding)))
		{
			String filePath;
			while ((filePath = pathsReader.readLine()) != null) {
				String hashStr = getFileHash(hasher, filePath, hashInfoWriter);
				if (hashStr != null) {
					writeHashInfo(filePath, hashStr, hashInfoWriter);
				}
			}
		} catch(IOException e) {
			throw e;
		}
	}
	
	public static String getFileHash(LowMemHasher hasher, String filePath, Writer hashInfoWriter) {
		filePath = filePath.trim();
		
		if (filePath.length() > 0) {
			String hashStr = MD5LowMemHasher.getEmptyHashStr();
			long startTime = System.nanoTime();
			
			File file = new File(filePath);
			if (file.isDirectory()) {
				Path path = Paths.get(filePath);
				
				try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(path))
				{
					walk(hasher, dirStream, hashInfoWriter);
				} catch (IOException | SecurityException e) {
					LOG.log(Level.WARNING, "Cannot read dir " + file.getPath(), e);
					return hashStr;
				}
				
				return null;
			}
			
			LOG.info("Reading " + filePath);
			
			try(BufferedInputStream fileInputStream =
					new BufferedInputStream(new FileInputStream(file)))
			{
				hashStr = HashUtil.fromStream(hasher.clear(), fileInputStream)
							.finish().toString();
				
				String elapsedTime = String.format("%.3f", (System.nanoTime() - startTime)/1.0e9);
				LOG.fine("Hash is calculated for " + elapsedTime + "s");
			} catch (IOException e) {
				LOG.log(Level.WARNING, "I/O error: " + filePath, e);
			}
			
			return hashStr;
		}
		
		return null;
	}
	
	private static void walk(LowMemHasher hasher, DirectoryStream<Path> directoryStream, Writer hashInfoWriter)
			throws IOException
	{
		for (Path path : directoryStream) {
			String hashStr = getFileHash(hasher, path.toString(), hashInfoWriter);
			if (hashStr != null) {
				writeHashInfo(path.toString(), hashStr, hashInfoWriter);
			}
		}
	}
	
	private static void writeHashInfo(String filePath, String hashStr, Writer hashInfoWriter)
			throws IOException
	{
		hashInfoWriter.write(hashStr.toUpperCase());
		hashInfoWriter.write(" ");
		hashInfoWriter.write(filePath);
		hashInfoWriter.write("\n");
	}
}
