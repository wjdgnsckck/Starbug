package starbug.service.member;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MessageSendingService {
/*     @Value("${coolsms.api.key}")
     private String apiKey;

     @Value("${coolsms.api.secret}")
     private String apiSecret;*/

    @Value("${coolsms.fromnumber}") // 발신자번호를 가져옴
    private String fromNumber;

    private final DefaultMessageService messageService;

    public MessageSendingService() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        // coolSMS 라이브러리 통신
        this.messageService = NurigoApp.INSTANCE.initialize("apiKey", "apiSecretKey", "https://api.coolsms.co.kr");
    }

    public void sendSMS(List<Map<String,String>> messages ) {
        System.out.println("messages = " + messages);
        ArrayList<Message> messageList = new ArrayList<>();

        for (int i = 0 ; i < messages.size(); i++) {    //messages의 길이만큼 for문 실행
            Message coolsms = new Message();    // coolsms 데이터를 담아둘 객체생성 (1명의 정보)

            coolsms.setFrom(fromNumber);    // 보낸사람 번호 설정
            coolsms.setTo(messages.get(i).get("memberphone"));     // 받는 사람 번호 설정
            coolsms.setText(messages.get(i).get("messageText"));    // 보낼 문자내용 설정
            //System.out.println("fromNumber : "+fromNumber);

            messageList.add(coolsms); // list에 문자전송 양식의 데이터 추가
        }

        try {
            // send 메소드로 단일 Message 객체를 넣어도 동작합니다!
            // 세 번째 파라미터인 showMessageList 값을 true로 설정할 경우 MultipleDetailMessageSentResponse에서 MessageList를 리턴하게 됩니다!
            //MultipleDetailMessageSentResponse response = this.messageService.send(messageList, false, true);

            // 중복 수신번호를 허용하고 싶으실 경우 위 코드 대신 아래코드로 대체해 사용해보세요!
            // 데이터 전송하여 API 통신 실행 함수 호출
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList,true);

            System.out.println(response);

            //return response;
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
       // return null;
    }



    public MessageListResponse getMessageList() {
        // 검색 조건이 있는 경우에 MessagListRequest를 초기화 하여 getMessageList 함수에 파라미터로 넣어서 검색할 수 있습니다!.
        // 수신번호와 발신번호는 반드시 -,* 등의 특수문자를 제거한 01012345678 형식으로 입력해주셔야 합니다!
        MessageListRequest request = new MessageListRequest();
        MessageListResponse response = this.messageService.getMessageList(request);
        System.out.println(response);

        return response;
    }

}