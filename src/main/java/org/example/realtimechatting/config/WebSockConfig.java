package org.example.realtimechatting.config;

import lombok.RequiredArgsConstructor;
import org.example.realtimechatting.ChatWebSockChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSockConfig implements WebSocketConfigurer {
    private final ChatWebSockChatHandler chatWebSockChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSockChatHandler, "/ws/chat")
                .setAllowedOrigins("*");
    }

}
