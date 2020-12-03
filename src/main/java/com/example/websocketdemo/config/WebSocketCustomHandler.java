package com.example.websocketdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * This is a class that is used to handle all override events below.
 * You may add "extends TextWebSocketHandler" after the WebSocketCustomHandler(this), although this is .
 * Reference: https://docs.spring.io/spring-framework/docs/4.1.7.RELEASE/spring-framework-reference/html/websocket.html#websocket-server-handler
 */
public class WebSocketCustomHandler implements WebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Event after the frontend client has connected the server successfully.
     *
     * @param session: WebSocketSession object that contains the info about the frontend client.
     * @throws Exception Any possible exceptions related from unable to send messages to WebSocketSession object, to parsing error and etc.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("afterConnectionEstablished()");
    }

    /**
     * Event when a message is fired from/to the frontend client.
     *
     * @param session: WebSocketSession object that contains the info about the frontend client.
     * @param message: The message retreived from the frontend clients.
     * @throws Exception Any possible exceptions related from unable to send messages to WebSocketSession object, to parsing error and etc.
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("handleMessage()");
        session.sendMessage(message); // Send back to the session.
    }

    /**
     * Event when any error happens within the WebSocket connections.
     *
     * @param session:   WebSocketSession object that contains the info about the frontend client.
     * @param exception: Exception object's throwable which may contain details of the exception.
     * @throws Exception Any possible exceptions related from unable to send messages to WebSocketSession object, to parsing error and etc.
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info("handleTransportError()");
    }

    /**
     * Event when a WebSocket session is closed by the frontend client or server itself.
     *
     * @param session:     WebSocketSession object that contains the info about the frontend client.
     * @param closeStatus: A CloseStatus object that contains the reason of why the WebSocket session is closed by frontend client or server itself.
     * @throws Exception Any possible exceptions related from unable to send messages to WebSocketSession object, to parsing error and etc.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("afterConnectionClosed()");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
