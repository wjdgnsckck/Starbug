package starbug.service.staff;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EncryptService {

    // 병철 // 암호화 복호화 기능만 구현
    private  AesBytesEncryptor encryptor;

    // 암호화
    public String encryptIdnum(String idnum){
        byte[] encrypt = encryptor.encrypt(idnum.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    // 복호화
    public String decryptIdnum(String encryptString){
        byte[] decryptBytes = stringToByteArray(encryptString);
        byte[] decrypt = encryptor.decrypt(decryptBytes);
        return new String(decrypt, StandardCharsets.UTF_8);


    }

    // byte에서 string 으로 변환
    public String byteArrayToString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte abyte : bytes) {
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }
    // string 에서 byte 로 변환
    public byte[] stringToByteArray(String byteString){
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        for (String s : split) {
            buffer.put((byte) Integer.parseInt(s));
        }
        return buffer.array();
    }


}
