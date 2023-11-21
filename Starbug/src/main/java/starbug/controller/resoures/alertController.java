package starbug.controller.resoures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import starbug.controller.parcel.AlramController;

import java.util.List;

@Component
public class alertController {

    @Autowired
    private AlramController alramController;



    // 매일 오후 8시에 실행되는 스케줄러 메서드
    @Scheduled(cron = "0 53 13 * * *")
    public void myScheduledMethod( ) throws Exception {
        System.out.println("InventoryController.myScheduledMethod");
        List<WebSocketSession> starBug= alramController.getResouresList();
        TextMessage date = new TextMessage("마감 시간입니다. 재고 로그 추가해주세요.");
        System.out.println("starBug = " + starBug);

        // 모든 관리자에게 메시지 전송
        for (WebSocketSession session : starBug) {
            if (session.isOpen()) {
                System.out.println("session = " + session);
                alramController.handleMessage(session, date);
            }
        }

    }
}
