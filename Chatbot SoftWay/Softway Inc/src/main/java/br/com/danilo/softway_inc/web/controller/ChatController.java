package br.com.danilo.softway_inc.web.controller;

import br.com.danilo.softway_inc.domain.service.ChatbotService;
import br.com.danilo.softway_inc.web.dto.QuestionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
    public String loadChatbotPage(Model model) {
        var historyMessages = chatbotService.loadHistory(); // --> Carrega o histórico de mensagens

        model.addAttribute("historico", historyMessages); // --> Adiciona o histórico de mensagens no modelo e retorna a página

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
        chatbotService.clearConversation();
        return "redirect:/chat";
    }
}
