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
    public ResponseBodyEmitter answerQuestion(@RequestBody QuestionDto dto) {
        var responseFlow = chatbotService.answerQuestion(dto.question());
        var emitter = new ResponseBodyEmitter();

        responseFlow.subscribe(                              // --> Inscreve-se para receber os eventos
                chunk -> {
                    var tokenChunk = chunk.getChoices().get(0).getMessage().getContent();
                    if (tokenChunk != null) {
                        emitter.send(tokenChunk);     // --> Envia o token para o cliente
                    }
                }, emitter::completeWithError,       // --> Envia um erro para o cliente
                emitter::complete                           // --> Completa o fluxo
        );
        return emitter;
    }

    // --> Limpa a conversa
    @GetMapping("limpar")
    public String clearConversation() {
        return PAGE_CHAT;
    }
}
