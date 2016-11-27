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
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	private ZipUtil() {}
	
	public static final int DEFAULT_BUF_SIZE = 8192;
	
	public static void addRecursively(Path src, Path dest, ZipOutputStream zipOut) throws IOException {
		addRecursively(src, dest, zipOut, DEFAULT_BUF_SIZE);
	}
	
	public static void addRecursively(Path src, Path dest, ZipOutputStream zipOut, int bufferSize) throws IOException {
		String destStr = src.toString();
		Path fileNamePath = src.getFileName();
		
		if (Files.isDirectory(src)) {
			if (!destStr.endsWith(File.separator)) {
				destStr += File.separator;
			}
			
			zipOut.putNextEntry(new ZipEntry(destStr));
			zipOut.closeEntry();
			
			try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(src)) {
				for (Iterator<Path> it = dirStream.iterator(); it.hasNext(); ) {
					addRecursively(it.next(), dest.resolve(fileNamePath), zipOut, bufferSize);
				}
			}
		} else if (Files.exists(src)) {
			String destFileName = dest.resolve(fileNamePath).toString();
			zipOut.putNextEntry(new ZipEntry(destFileName));
			
			writeContent(new BufferedInputStream(new FileInputStream(src.toString())),
					zipOut, bufferSize);
			zipOut.closeEntry();
		} else {
			throw new FileNotFoundException(src.toString());
		}
	}
	
	public static void writeContent(InputStream inputStream, ZipOutputStream zipOut) throws IOException {
		writeContent(inputStream, zipOut, DEFAULT_BUF_SIZE);
	}
	
	public static void writeContent(InputStream inputStream, ZipOutputStream zipOut, int bufferSize) throws IOException {
		try(InputStream in = inputStream) {
			byte[] buffer = new byte[bufferSize];
			int count = in.read(buffer);
			while(count > 0) {
				zipOut.write(buffer, 0, count);
				count = in.read(buffer);
			}
		} catch (IOException e) {
			throw e;
		}
	}
}
