package ru.ifmo.ctddev.pistyulga.walk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.logging.Logger;

import ru.ifmo.ctddev.pistyulga.log.LogService;
import ru.ifmo.ctddev.pistyulga.md5.MD5LowMemoryHasher;


class Walk {
	private static final Logger LOG = LogService.getLogger(Walk.class.getName());
	
	public static void walk(InputStream pathsInputStream, Writer hashInfoWriter, String encoding)
			throws IOException
	{
		
		try(BufferedReader pathsReader =
				new BufferedReader(new InputStreamReader(pathsInputStream, encoding)))
		{
			String filePath;
			while ((filePath = pathsReader.readLine()) != null) {
				String md5Hash = MD5LowMemoryHasher.getEmptyHashStr();
				
				try(FileInputStream fileInputStream = new FileInputStream(filePath))
				{
					
				} catch (IOException | SecurityException e) {
					
				}
			}
		} catch(IOException e) {
			throw e;
		}
	}
}
