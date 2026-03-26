package com.demo.card.config;

import com.demo.card.web.CardController;
import org.springaicommunity.mcp.provider.tool.SyncMcpToolProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpServerConfig {

    @Bean
    public SyncMcpToolProvider syncMcpToolProvider(CardController cardController) {
        return new SyncMcpToolProvider(List.of(cardController));
    }
}
