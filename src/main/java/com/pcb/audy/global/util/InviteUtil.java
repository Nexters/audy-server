package com.pcb.audy.global.util;

import static com.pcb.audy.global.response.ResultCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.course.dto.request.CourseInviteRedisReq;
import com.pcb.audy.global.exception.GlobalException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InviteUtil {

    @Value("${course-invite-key}")
    private String key;

    private final ObjectMapper objectMapper;

    public String encryptCourseInviteReq(CourseInviteRedisReq courseInviteRedisReq) {
        try {
            // 객체를 JSON 문자열로 변환
            String json = objectMapper.writeValueAsString(courseInviteRedisReq);

            // AES 키 생성 및 암호화
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(json.getBytes());

            // 암호화된 데이터를 Base64 문자열로 변환 (필요한 경우)
            String base64Encoded = Base64.getEncoder().encodeToString(encryptedBytes);

            // Base64 인코딩 문자열을 URL 인코딩
            return URLEncoder.encode(base64Encoded, "UTF-8");
        } catch (Exception e) {
            throw new GlobalException(FAILED_ENCRYPT);
        }
    }

    public CourseInviteRedisReq decryptCourseInviteReq(String encryptedData) {
        try {
            String sanitizedData = encryptedData.replaceAll("\\s", "");
            log.info(sanitizedData);

            String decodedData = URLDecoder.decode(sanitizedData, "UTF-8");
            log.info(decodedData);

            // Base64 인코딩된 문자열을 바이트 배열로 변환
            byte[] decodedBytes = Base64.getDecoder().decode(decodedData);

            // AES 복호화
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            // CourseInviteReq 객체로 변환
            log.info(
                    String.valueOf(
                            objectMapper.readValue(decryptedBytes, CourseInviteRedisReq.class).getCourseId()));
            return objectMapper.readValue(decryptedBytes, CourseInviteRedisReq.class);
        } catch (Exception e) {
            log.error("exception msg", e);
            throw new GlobalException(FAILED_DECRYPT);
        }
    }
}
