package org.danilo.softway_inc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// --> Controlador de chat
@Controller
@RequestMapping({"/", "chat"})
public class ChatController {

        private static final String PAGE_CHAT = "chat";

        // --> Carrega a página do chat
        @GetMapping
        public String loadChatbotPage() {
            return PAGE_CHAT;
        }

        // --> Responde a pergunta
        @PostMapping
        @ResponseBody
        public String answerQuestion(@RequestBody PerguntaDto dto) {
            return dto.pergunta();
        }

        // --> Limpa a conversa
        @GetMapping("limpar")
        public String clearConversation() {
            return PAGE_CHAT;
        }
}
