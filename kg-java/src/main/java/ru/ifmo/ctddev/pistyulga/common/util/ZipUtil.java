package ru.ifmo.ctddev.pistyulga.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ru.ifmo.ctddev.pistyulga.common.lang.util.CharPool;

public class ZipUtil {
	/** Private constructor for this static class */
	private ZipUtil() {}
	
	private static final Path EMPTY_PATH = Paths.get("");
	
	/** Default size of a byte buffer for transfer of the file bytes to {@link ZipOutputStream} */
	public static final int DEFAULT_BUF_SIZE = 8192;
	
	public static void addRecursively(Path src, Path dest, ZipOutputStream zipOut) throws IOException {
		addRecursively(src, dest, zipOut, DEFAULT_BUF_SIZE);
	}
	
	public static void addRecursively(Path src, Path dest, ZipOutputStream zipOut, int bufferSize) throws IOException {
		Path normDest = dest.normalize();
		String destStr = normDest.toString()
				.replace(File.separatorChar, CharPool.FS_NAME_SEPARATOR);
		
		if (Files.isDirectory(src)) {
			if (!normDest.equals(EMPTY_PATH)) {
				int destStrLen = destStr.length();
				
				if (destStr.charAt(destStrLen - 1) != CharPool.FS_NAME_SEPARATOR) {
					destStr += CharPool.FS_NAME_SEPARATOR;
				}
				
				zipOut.putNextEntry(new ZipEntry(destStr));
				zipOut.closeEntry();
			}
			
			try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(src)) {
				for (Iterator<Path> it = dirStream.iterator(); it.hasNext(); ) {
					Path next = it.next();
					Path nextDest = normDest.resolve(next.getFileName());
					addRecursively(next, nextDest, zipOut, bufferSize);
				}
			}
		} else if (Files.exists(src)) {
			zipOut.putNextEntry(new ZipEntry(destStr));
			writeContent(new BufferedInputStream(new FileInputStream(src.toString())),
					zipOut, bufferSize);
			zipOut.closeEntry();
		} else {
			throw new FileNotFoundException(src.toString());
		}
	}
	
	/**
	 * Equivalent to {@link #writeContent(InputStream, ZipOutputStream, int)
	 * 		writeContent(InputStream, ZipOutputStream, DEFAULT_BUF_SIZE)}
	 * @param inputStream
	 * @param zipOut
	 * @throws IOException if I/O error has occurred
	 */
	public static void writeContent(InputStream inputStream, ZipOutputStream zipOut) throws IOException {
		writeContent(inputStream, zipOut, DEFAULT_BUF_SIZE);
	}
	
	/**
	 * @param inputStream - an input stream
	 * @param zipOut - ZIP output stream
	 * @param bufferSize - size of a byte buffer for transfer of the bytes from stream
	 * @throws IOException if I/O error has occurred
	 */
	public static void writeContent(InputStream inputStream, ZipOutputStream zipOut, int bufferSize) throws IOException {
		try(InputStream in = inputStream) {
			byte[] buffer = new byte[bufferSize];
			int count = in.read(buffer);
			while(count > 0) {
				zipOut.write(buffer, 0, count);
				count = in.read(buffer);
			}
		}
	}
}
