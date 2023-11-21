package starbug.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class FileService {

    // 0. 파일 경로
    private String fileRootPath = "C:\\Users\\504\\Desktop\\starbug\\build\\resources\\main\\static\\static\\media\\";

    // 1. 업로드
    public String fileUpload(MultipartFile multipartFile) {

        if(multipartFile.isEmpty()){ // 파일 없다면  실패
            return null;
        }

        String fileName =
                UUID.randomUUID().toString()+"_"+ // UUID 이용한 파일 식별자 만들기
                        multipartFile.getOriginalFilename().replaceAll("_", "-"); // 만일 식별을 위해 _를 제거 하고 - 변경
        File file = new File(fileRootPath + fileName);
        try{
            multipartFile.transferTo(file);
            return fileName;
        }catch(Exception e){
            System.out.println("업로드 실패 " + e);
            return null;
        }
    }

    // 2. 다운로드
    @Autowired
    private HttpServletResponse response;

    public void fileDownload(String uuidFile){
        String downloadFilePath = fileRootPath + uuidFile;
        String fileName = uuidFile.split("_")[1]; // _ 기준으로 쪼갠 후 뒷자리 파일명만 호출
        try{
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));
            File file = new File(downloadFilePath);
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[(int)file.length()];
            fin.read(bytes);
            BufferedOutputStream fout = new BufferedOutputStream(response.getOutputStream());
            fout.write(bytes);
            fout.flush(); fout.close(); fin.close();
            System.out.println("파일 불러오기 성공");
        }catch(Exception e){
            System.out.println("파일 불러오기 실패");

        }

    }
}
