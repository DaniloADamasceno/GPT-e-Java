package br.com.danilo.softway_inc.web.controller;

import br.com.danilo.softway_inc.domain.service.ChatbotService;
import br.com.danilo.softway_inc.web.dto.QuestionDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;


// --> Controlador de chat
@Controller
@RequestMapping({"/", "chat"})
public class ChatController {

    private static final String PAGE_CHAT = "chat";
    private ChatbotService chatbotService;

    //! -------------------------------------------   CONSTRUTOR   -------------------------------------------------
    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    //!--------------------------------------------   METHODS   ----------------------------------------------------
    // --> Carrega a página do chat
    @GetMapping
    public String loadChatbotPage() {
        return PAGE_CHAT;
    }

    // --> Responde a question
    @PostMapping
    @ResponseBody
    public String answerQuestion(@RequestBody QuestionDto dto) {
        return chatbotService.answerQuestion(dto.question());
    }

    // --> Limpa a conversa
    @GetMapping("limpar")
    public String clearConversation() {
        return PAGE_CHAT;
    }
}
