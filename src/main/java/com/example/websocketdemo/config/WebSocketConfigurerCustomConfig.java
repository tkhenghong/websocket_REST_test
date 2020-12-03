package com.example.websocketdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;

/**
 * This is a class for create WebSocket broker that similar to WebSocketConfig class but with much simpler configurations.
 * NOTE: You can run this class same with WebSocketMessageBrokerConfigurerCustomConfig class.
 * NOTE: The paths below can't be shared with the ones in WebSocketMessageBrokerConfigurerCustomConfig class unless .
 */
@Configuration
@EnableWebSocket
public class WebSocketConfigurerCustomConfig implements WebSocketConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<String> webSocketBrokerEndpointList;

    public WebSocketConfigurerCustomConfig(@Value("#{'${websocket.broker.endpoint.list}'.split(',')}") List<String> webSocketBrokerEndpointList) {
        this.webSocketBrokerEndpointList = webSocketBrokerEndpointList;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), webSocketBrokerEndpointList.toArray(new String[0])).withSockJS();
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new WebSocketCustomHandler();
    }
}
