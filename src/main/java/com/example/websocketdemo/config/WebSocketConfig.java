package com.example.websocketdemo.config;

import com.example.websocketdemo.interceptor.WebSocketHttpHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     *
     * Little knowledge to remove all confusions between WebSocket, STOMP and SockJS:
     * WebSocket is a protocol that is used by web and mobile apps to send real time events from backend to front end.
     * Normally, many apps use this technology to make frontend connect to backend to either having group conversations,
     * or getting broadcast messages quickly without keep asking the server REST APIs.
     *
     * HTTP/HTTPS and WebSocket protocols are in 1st layer in 7 main OSI layers, which is Application layer.
     *
     * WebSocket is a bi-directional, full-duplex connection over persistent TCP connection.
     *
     * Unlike normal HTTP or HTTPs are stateless protocols, where no client's information is stored on the server for a period of time,
     * WebSocket is a STATEFUL protocol where you'll have a session with the backend(backend have some information about you), and backend
     * will check whether the client is still alive or not periodically.
     *
     * STOMP.js is a JS library that uses WebSocket connection to subscribe some topics or even send message to a particular topic in the backend.
     * Remember, It's just utilizing WebSocket, it's not doing connect to WebSocket part. It doesn't relate to WebSocket protocol itself at all.
     *
     * SockJS is a JS library that is used by A LOT of web browsers for connecting WebSocket.
     * It will connect to backend's WebSocket using ws or wss protocols.
     * It will connect to backend's WebSocket using http or https protocols, If return status code is 200, it will upgrade from http/https to ws/wss protocol.
     * For example, SockJS will send:
     * GET http://localhost:8080/ws/info?t=1606836349023
     * Then, it will send:
     * GET ws://localhost:8080/ws/209/hyffywlk/websocket
     * When backend return status code 101, the WebSocket has been successfully established.
     *
     */
    /**
     * FRONT END
     * Using web browsers connect WebSocket using SockJS as fallback option, and using STOMP.js to step over WebSocket that SockJS has created.
     * Roughly steps they are using:
     * Step 1: Setup SockJS, using the ones listed in registry.addEndpoint(...)
     * var socket = new SockJS('/ws'); <-- SockJS is an representative object comes from sock.js library.
     * Step 2:
     * var stompClient = Stomp.over(socket); <-- Stomp is an representative object comes from stomp.js library.
     * Step 3: Listen to any topic available that listed in registry.enableSimpleBroker(....).
     * stompClient.subscribe('/topic/public', onMessageReceived);
     * Step 4: Send message to a particular topic listed in registry.setApplicationDestinationPrefixes("/app");
     * stompClient.send("/app/chat.addUser",
     *         {},
     *         JSON.stringify({sender: username, type: 'JOIN'})
     *     )
     *
     */

    /**
     * BACKEND
     * How to setup WebSocket configuration perfectly:
     * Step 1:
     * Override 2 methods(mainly) from "implements WebSocketMessageBrokerConfigurer"
     * registerStompEndpoints(...) and configureMessageBroker(...)
     * Step 2:
     * In registerStompEndpoints(StompEndpointRegistry registry) {....}, add some endpoints for WebSocket clients to connect(either using SockJS by web browser clients or WebSocket IO )
     * registry.addEndpoint("/ws", "/greeting")
     * Step 3:
     * In configureMessageBroker(MessageBrokerRegistry registry) {....}, add some prefixes that your backend is going to use.
     * registry.enableSimpleBroker("/topic", "/queue");
     * Step 4: Set the prefixes for allow the frontend clients to send messages to:
     * registry.setApplicationDestinationPrefixes("/app");
     * Step 5(Optional): Add HandshakeInterceptor class to allow do something before and after handshake.
     * Step 6(Optional): Add WebSocket listener class to allow hear WebSocket connected and disconnected event.
     * Step 7(Optional): Add WebSocket Channel Interceptor to allow use hear STOMP commands sent from frontend clients.
     * Step 8: Create @RestController classes with @MessageMapping(...) that allows frontend clients to send like FRONTEND Step 4.
     * For example:
     * ****Look at FRONTEND Step 4.****
     * to
     *
     * @MessageMapping("/chat.sendMessage") ChatMessage sendMessage(@Payload ChatMessage chatMessage) {....}
     * Step 9(Optional): Add @SendTo(....) to allow send to the topic that frontend clients listening to.
     * For example:
     * ****Look at FRONTEND Step 3.****
     * to
     * @MessageMapping("/chat.addUser")
     * @SendTo("/topic/public") public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {...}
     * NOTE: You may add SimpMessageHeaderAccessor object into the method to send STOMP command back to frontend client.
     */

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * Connect to WebSocket using STOMP over SockJS (Web browser's way):
         * http://localhost:8080/ws
         * http://localhost:8080/greeting
         * or (If using WebSocket)
         * http://localhost:8080/ws/websocket
         * http://localhost:8080/greeting/websocket
         * Follow Step 1 of the Frontend above.
         *
         * IMPORTANT NOTE: If you're using Google Chrome, and using http://websocket.org/echo.html (Note that I'm not using https to reach the website)
         * to test your WebSocket, you MUST add /websocket behind your endpoints below:
         * For example: ws:/localhost:8080/greeting becomes ws:/localhost:8080/greeting/websocket
         * Solution with explanation reference: https://stackoverflow.com/questions/51845452
         */
        registry.addEndpoint("/ws", "/greeting")
                .addInterceptors(new WebSocketHttpHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * registry.enableSimpleBroker(...) is to tell here that everything your
         * frontend client and backend @MessageMapping(...) are set up using
         * the following destination prefixes. Such as your STOMP over SockJS client,
         * when they are listening to
         * (stompClient is the object after )
         * stompClient.subscribe('/topic/public', onMessageReceived);
         *
         *
         */
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
