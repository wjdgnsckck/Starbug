package starbug.controller.parcel;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AlramController extends TextWebSocketHandler {

    // 소켓과 연동된 클라이언트 소켓들을 저장하는 리스트
    private static List<WebSocketSession> contextList = new ArrayList<>();

    public List<WebSocketSession> getResouresList (){
        return contextList;
    }


    // 1. 클라이언트 소켓과 연동 성공 했을떄
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println( "session 들어오는건가" + session );
        contextList.add( session );
    }

    // 2. 연동 오류 발생했을때
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println( "session" + session + " , exception = " + exception );
    }

    // 3. 소켓과 연동 끊겼을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println( "session" + session + " , status = " + status );
        contextList.remove( session );
    }
    // 4. 메세지 받았을때
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println( "session" + session + " , message = " + message );
        // * 서버가 클라이언트로부터 받은 메시지를 접속명단에 있는 모든 세션들에게 전달
        for( WebSocketSession sessions : contextList ){
            sessions.sendMessage( message );
        }
    }
}
