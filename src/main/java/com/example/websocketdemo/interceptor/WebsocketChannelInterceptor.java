package com.example.websocketdemo.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class WebsocketChannelInterceptor implements ChannelInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;

    @Autowired
    public WebsocketChannelInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.info("WebsocketChannelInterceptor.java preSend()");
        StompHeaderAccessor stompHeaderAccessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        readStompHeaderAccessor(stompHeaderAccessor);

        identifySTOMPCommand(stompHeaderAccessor);

        readMessage(message);

        readMessageChannel(channel);

        return message;
    }

    void readStompHeaderAccessor(StompHeaderAccessor stompHeaderAccessor) {
        try {
            String stompHeaderAccessorString = objectMapper.writeValueAsString(stompHeaderAccessor);
            logger.info("stompHeaderAccessorString: {}", stompHeaderAccessorString);
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to convert StompHeaderAccessor object to JSON string.");
        }
    }

    void identifySTOMPCommand(StompHeaderAccessor stompHeaderAccessor) {
        if (!ObjectUtils.isEmpty(stompHeaderAccessor)) {
            if (!ObjectUtils.isEmpty(stompHeaderAccessor.getCommand())) {
                switch (stompHeaderAccessor.getCommand()) {
                    case CONNECT:
                        logger.info("StompCommand.CONNECT");
                        break;
                    case DISCONNECT:
                        logger.info("StompCommand.DISCONNECT");
                        break;
                    case STOMP:
                        logger.info("StompCommand.STOMP");
                        break;
                    case SUBSCRIBE:
                        logger.info("StompCommand.SUBSCRIBE");
                        break;
                    case UNSUBSCRIBE:
                        logger.info("StompCommand.UNSUBSCRIBE");
                        break;
                    case SEND:
                        logger.info("StompCommand.SEND");
                        break;
                    case ACK:
                        logger.info("StompCommand.ACK");
                        break;
                    case NACK:
                        logger.info("StompCommand.NACK");
                        break;
                    case BEGIN:
                        logger.info("StompCommand.BEGIN");
                        break;
                    case COMMIT:
                        logger.info("StompCommand.COMMIT");
                        break;
                    case ABORT:
                        logger.info("StompCommand.ABORT");
                        break;
                    case CONNECTED:
                        logger.info("StompCommand.CONNECTED");
                        break;
                    case RECEIPT:
                        logger.info("StompCommand.RECEIPT");
                        break;
                    case MESSAGE:
                        logger.info("StompCommand.MESSAGE");
                        break;
                    case ERROR:
                        logger.info("StompCommand.ERROR");
                        break;
                    default:
                        logger.info("No StompCommand detected.");
                        break;
                }
            } else {
                logger.info("Client doesn't use STOMP to connect WebSocket.");
            }
        }
    }

    void readMessage(Message<?> message) {
        try {
            String messageString = objectMapper.writeValueAsString(message);
            logger.info("messageString: {}", messageString);
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to convert Message<?> object to JSON string.");
        }
    }

    void readMessageChannel(MessageChannel messageChannel) {
        try {
            String messageChannelString = objectMapper.writeValueAsString(messageChannel);
            logger.info("messageChannelString: {}", messageChannelString);
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to convert MessageChannel object to JSON string.");
        }
    }
}
