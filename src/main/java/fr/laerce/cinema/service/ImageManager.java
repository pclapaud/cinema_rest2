package fr.laerce.cinema.service;

import fr.laerce.cinema.model.Film;
import fr.laerce.cinema.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageManager {
    @Value("${cinema.img.path}")
    String path;

    public int savePhoto(Person p, InputStream fi){
        p.setImagePath(save("p", "personnes", fi));
        return 0;
    }

    public int savePoster(Film f, InputStream fi) {
        f.setImagePath(save("f","affiches", fi));
        return 0;
    }


    private String save(String prefix, String subPath, InputStream fi){
        String fileName = "";
        try(DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(path+"/"+subPath),prefix+"*")){

            for (Path file: dir
            ) {
                if(fileName.compareTo(file.getFileName().toString())<0){
                    fileName = file.getFileName().toString();
                }
            }
            String numStr = fileName.substring(1, fileName.indexOf(".jpg"));

            Integer num = Integer.parseInt(numStr);

            numStr = String.format("%04d",num+1);

            fileName = prefix+numStr+".jpg";

            String filePath = path+"/"+subPath+"/"+fileName;

            Files.copy(fi, new File(filePath).toPath());

        }catch (IOException ioe){
            System.out.println("Erreur sur nom d'image : "+ioe.getMessage());
        }


        return fileName;
    }

    public InputStream retreivePhoto(String fileName){
        return retreiveImage("personnes", fileName);
    }

    public InputStream retreivePoster(String fileName){
        return retreiveImage("affiches", fileName);
    }

    private InputStream retreiveImage(String subPath, String fileName){
        InputStream is = null;
        try {
            is = new FileInputStream(path+"/"+subPath+"/"+fileName);
        } catch (FileNotFoundException fnfe) {
            System.out.println("Erreur récupération de l'image "+fileName+" : "+fnfe.getMessage());
        }
        return is;
    }
}
