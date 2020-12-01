package com.example.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.*;

// This is used to filter which type of SimpMessageType is allowed to connect with WebSocket. This is an authorization filter.
// https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/websocket.html#websocket-authorization
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // This is within Spring Boot Security for WebSocket.
        messages
                .nullDestMatcher().authenticated()
                .simpTypeMatchers(CONNECT).permitAll()
                .simpTypeMatchers(SUBSCRIBE, MESSAGE, UNSUBSCRIBE, DISCONNECT).authenticated()
                .simpDestMatchers("/secured/**").authenticated()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
