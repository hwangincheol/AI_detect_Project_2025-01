package edu.mbcai.pybootai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController // 파이썬과 통신하는 비동기화 컨트롤러 역할 담당
public class RestRegController {

    @Autowired  // 생성자 자동 주입(객체 사용)
    private WebClient webClient;    // 방금 생성한 config 컨트롤러 객체 생성

    @PostMapping("/java_service")
    public String serviceRequest(@RequestParam("file") MultipartFile file, @RequestParam("message") String message) {
        // 파일 처리 코드
        try {
            System.out.println("Received file: " + file.getOriginalFilename());

            // AI 서버로 이미지 전송
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();  // 멀티파트 폼 데이터를 구성
            bodyBuilder.part("message", message);  // 메시지 추가
            bodyBuilder.part("file", file.getResource());  // 받은 파일 추가

            String result = webClient.post().uri("/detect")   // AI 서버의 /detect 엔드포인트로 요청
                    .contentType(MediaType.MULTIPART_FORM_DATA) // 파일 전송에 맞는 콘텐츠 타입 설정
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))  // 폼 데이터 요청 본문에 설정
                    .retrieve()  // 요청 실행
                    .bodyToMono(String.class)   // 응답 본문을 String으로 변환
                    .block();  // 비동기 처리 결과를 동기적으로 기다림

            return result;  // AI 서버의 결과 반환
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"File processing failed.\"}";
        }
    }

    /*@PostMapping("/java_service")
    public String serviceRequest(MultipartFile file, String message) {

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();  // 멀티파트 폼 데이터를 구성
        bodyBuilder.part("message", message);   //폼 데이터
        bodyBuilder.part("file", file.getResource());   //폼 데이터, 파일
        String result = webClient.post().uri("/detect")   //Post 방식으로 요청, 엔드포인트는 /detect
                .contentType(MediaType.MULTIPART_FORM_DATA) //파일이 전송되므로
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                //폼 데이터를 요청 본문으로 설정
                .retrieve() //요청을 싱행하고 응답을 받음
                .bodyToMono(String.class)   //본문을 String 타입으로 변환
                .block();   //비동기처리를 동기적으로 블록해서 결과를 반환

        return result;
    }   // http://localhost:80/java_service 요청 post 처리*/

    // 1. 자바 rest 컨트롤러로 텍스트와 이미지를 비동기 방식으로 전송
    // 2. ai 서버에서 이미지를 받아 객체 탐지를 수행
    // 3. ai 서버에서 이미지를 base64 인코딩 문자열로 변환
    // 4. rest 컨트롤러에서 비동기 방식으로 텍스트와 이미지를 변환
    // 5. 비동기 요청한뷰 페이지에서 결과를 화면에 출력
    
}
