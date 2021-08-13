package com.ideaportal.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
	
	
	public boolean saveFile(final MultipartFile file, final File dir) {
        final String filename = file.getOriginalFilename();
        final String path = dir + File.separator + filename;
        final Path filePath = Paths.get(path, new String[0]);
        try {
            final InputStream fileInputStream = file.getInputStream();
            Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch (IOException e) {
           
            return false;
        }
    }
	
	
	

}
