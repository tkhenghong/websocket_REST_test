package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.*;

/**
 * This is a listener that hears events from WebSocket system in the application.
 * https://docs.spring.io/spring-framework/docs/4.1.7.RELEASE/spring-framework-reference/html/websocket.html#websocket-stomp-appplication-context-events
 */
@Component
public class WebSocketEventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Detects a user is connecting to the server.
     *
     * @param event: SessionConnectedEvent object that contains details about the event.
     */
    @EventListener
    public void handleWebSocketBrokerAvailabilityListener(BrokerAvailabilityEvent event) {
        logger.info("WebSocket broker available status: {}", event.isBrokerAvailable());
    }

    /**
     * Detects a user is connecting to the server.
     *
     * @param event: SessionConnectedEvent object that contains details about the event.
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        logger.info("A WebSocket client is trying to connect to the server.");
    }

    /**
     * Detects a user has connected to the server successfully.
     *
     * @param event: SessionConnectedEvent object that contains details about the event.
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("A WebSocket client has connected to the server.");
        messagingTemplate.convertAndSend("/topic/public", "Test reply.");
    }

    /**
     * Detects a user has subscribed to a STOMP topic in the server.
     *
     * @param event: SessionSubscribeEvent object that contains details about the event.
     */
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        logger.info("A WebSocket client has subscribed to a STOMP topic in the server.");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (StringUtils.hasText(username)) {
            logger.info("The user that has subscribed to the topic : {}", username);
        }
    }


    /**
     * Detects a user has unsubscribed from a STOMP topic from the server.
     *
     * @param event: SessionUnsubscribeEvent object that contains details about the event.
     */
    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        logger.info("A WebSocket client has unsubscribed a STOMP topic from the server.");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (StringUtils.hasText(username)) {
            logger.info("The user that has unsubscribed from the topic : {}", username);
        }
    }

    /**
     * Detects a user has disconnected from the server.
     *
     * @param event: SessionDisconnectEvent object that contains details about the event.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("A WebSocket client has disconnected from the server.");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (StringUtils.hasText(username)) {
            logger.info("User that has disconnected from the server: {}", username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
