package ru.ifmo.ctddev.pistyulga.walk;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.ctddev.pistyulga.hash.LowMemoryHasher;
import ru.ifmo.ctddev.pistyulga.hash.MD5LowMemoryHasher;
import ru.ifmo.ctddev.pistyulga.log.LogService;

class Walk {
	private static final Logger LOG = LogService.getLogger();
	
	public static void walk(InputStream pathsInputStream, Writer hashInfoWriter, String encoding)
			throws IOException
	{
		byte[] buf = new byte[1];
		LowMemoryHasher hasher = new MD5LowMemoryHasher();
		
		try(BufferedReader pathsReader =
				new BufferedReader(new InputStreamReader(pathsInputStream, encoding)))
		{
			String filePath;
			while ((filePath = pathsReader.readLine()) != null) {
				filePath = filePath.trim();
				
				if (filePath.length() > 0) {
					String hashStr = MD5LowMemoryHasher.getEmptyHashStr();
					long startTime = System.currentTimeMillis();
					
					LOG.info("Reading " + filePath);
					
					try(BufferedInputStream fileInputStream =
							new BufferedInputStream(new FileInputStream(filePath)))
					{
						while (fileInputStream.read(buf) != -1) {
							hasher.processByte(buf[0]);
						}
						hashStr = hasher.finish().toString();
						
						String elapsedTime = String.format("%.3f", (System.currentTimeMillis() - startTime)/1000.0);
						LOG.fine("Hash is calculated for " + elapsedTime + "s");
					} catch (IOException e) {
						LOG.log(Level.WARNING, "Reading error", e);
					}
					
					hashInfoWriter.write(hashStr.toUpperCase() + " " + filePath + "\n");
				}
			}
		} catch(IOException e) {
			throw e;
		}
	}
}
