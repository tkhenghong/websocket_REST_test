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

    /**
     * A intercept method to determine that message from frontend client should be retrieved or not.
     *
     * @param channel: MessageChannel object that contain the channel information of the frontend client.
     * @return boolean value.
     * If true, the server will receive the message from the frontend client.
     * If false, the server will reject the message from the frontend client.
     */
    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.info("WebsocketChannelInterceptor.java preReceive()");
        return true;
    }

    /**
     * Read the content of the StompHeaderAccessor object directly by convert it into JSON string.
     *
     * @param stompHeaderAccessor: StompHeaderAccessor object, which is the header of the message, if the client is using STOMP command.
     */
    void readStompHeaderAccessor(StompHeaderAccessor stompHeaderAccessor) {
        try {
            String stompHeaderAccessorString = objectMapper.writeValueAsString(stompHeaderAccessor);
            logger.info("stompHeaderAccessorString: {}", stompHeaderAccessorString);
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to convert StompHeaderAccessor object to JSON string.");
        }
    }

    /**
     * Self made method to check the STOMP command of the message(Message<?> object).
     *
     * @param stompHeaderAccessor: StompHeaderAccessor object, which is the header of the message, if the client is using STOMP command.
     */
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

    /**
     * Read the Message<?> object directly by convert it into JSON string.
     *
     * @param message Message<?> object which mainly has a header and a payload.
     */
    void readMessage(Message<?> message) {
        try {
            String messageString = objectMapper.writeValueAsString(message);
            logger.info("messageString: {}", messageString);
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to convert Message<?> object to JSON string.");
        }
    }

    /**
     * Read the MessageChannel object directly by convert it into JSON string.
     *
     * @param messageChannel: MessageChannel object which contains information about the channel that the message is sending to.
     */
    void readMessageChannel(MessageChannel messageChannel) {
        try {
            String messageChannelString = objectMapper.writeValueAsString(messageChannel);
            logger.info("messageChannelString: {}", messageChannelString);
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to convert MessageChannel object to JSON string.");
        }
    }
}
