package com.pcb.audy.global.socket.handler;

import static com.pcb.audy.global.response.ResultCode.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.response.BasicResponse;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SocketErrorHandler extends StompSubProtocolErrorHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(
            Message<byte[]> clientMessage, Throwable ex) {
        if (ex.getCause() instanceof GlobalException e) {
            return errorMessage(BasicResponse.error(e.getResultCode()));
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> errorMessage(BasicResponse<?> response) {
        String message = getMessage(response);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);
        accessor.setMessage(message);

        return MessageBuilder.createMessage(
                message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }

    private String getMessage(BasicResponse<?> response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return INTERNAL_SERVER_ERROR.getMessage();
        }
    }
}
