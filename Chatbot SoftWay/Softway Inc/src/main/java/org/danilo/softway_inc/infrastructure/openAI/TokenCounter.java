package org.danilo.softway_inc.infrastructure.openAI;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.stereotype.Component;

// --> Contador de Tokens
@Component
public class TokenCounter {

    private final Encoding encoding;

    public TokenCounter(Encoding encoding) {
        var registry = Encodings.newDefaultEncodingRegistry();
        this.encoding = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);  // --> Modelo de linguagem GPT-3.5 Turbo
    }

    public int countTokens(String message) {
        return encoding.countTokens(message);
    }
}
