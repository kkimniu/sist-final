package io.cavia.trader.module.game.chat.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();


    @Override
    //클라이언트가 WebSocket 연결을 성공적으로 맺었을 때 호출됨
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String queryStr = session.getUri().getQuery();
        String nickname = queryStr.substring(queryStr.indexOf("=") + 1);
        sessions.put(0, session);
        session.sendMessage(new TextMessage(nickname + "님 연결성공"));
    }

    @Override
    //클라이언트가 메시지를 서버로 전송했을 때 호출됨
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String playload = (String) message.getPayload();
        String queryStr = session.getUri().getQuery();
        String nickname = queryStr.substring(queryStr.indexOf("=") + 1);

        for (WebSocketSession s : sessions.values()) {
            s.sendMessage(new TextMessage(nickname + ":" + playload));
        }
    }

    @Override
    // WebSocket 통신 중 오류가 발생했을 때 호출됨
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    // 클라이언트가 WebSocket 연결을 끊었을 때 호출됨
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println(session.getId() + "번째 사람 연결 끊김");
        sessions.remove(session.getId());
    }

    @Override
    //클라이언트가 메시지를 여러 조각으로 나눠서 보낼 수 있게 허용할지 여부
    public boolean supportsPartialMessages() {
        return false;
    }
}
