package com.juss.live.skin.simple.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadFileFromAssertUtil {
	public static void copyFromAssets(Context context, String assetsPath,
			String destPath) {
		try {
			String[] fileNames = context.getAssets().list(assetsPath);
			if (fileNames.length > 0) {
				for (String f : fileNames) {
					// judge if is directory
					try {
						InputStream is = context.getAssets().open(
								assetsPath + File.separator + f);

						// f is a file, copy it
						File dir = new File(destPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}

						File file = new File(dir, f);
						if (!file.exists()) {
							file.createNewFile();
						}

						FileOutputStream fos = new FileOutputStream(file);
						byte[] data = new byte[4096];
						while (true) {
							int count = is.read(data);
							if (count < 0) {
								break;
							}
							fos.write(data, 0, count);
						}

						fos.flush();
						fos.close();
						is.close();
					} catch (IOException e) {
						// is a directory
						copyFromAssets(context,
								assetsPath + File.separator + f, destPath
										+ File.separator + f);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void deleteFile(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f:files){
				deleteFile(f);
			}
			file.delete();
		}else{
			file.delete();
		}
	}
}