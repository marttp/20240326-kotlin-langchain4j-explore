import dev.langchain4j.memory.ChatMemory
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.ollama.OllamaStreamingChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.TokenStream
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import java.time.Duration

class ChatService(modelUrl: String, modelName: String) : UserStreamCommunication, ModelCommunication {

    private val languageModel: StreamingChatLanguageModel = connectModel(modelUrl, modelName)
    private val assistant: ModelCommunication

    init {
        // Memorize for 10 messages continuously
        val chatMemory: ChatMemory = MessageWindowChatMemory.withMaxMessages(10)
        this.assistant =
            AiServices.builder(ModelCommunication::class.java)
                // Alternative of .chatLanguageModel() which support streaming response
                .streamingChatLanguageModel(this.languageModel)
                .chatMemory(chatMemory)
                .build()
    }

    override suspend fun ask(prompt: String) = coroutineScope {
        val tokenStream: TokenStream = chatWithModel(prompt)
        val deferred = CompletableDeferred<Unit>()
        tokenStream.onNext { token -> print(token) }
            .onComplete {
                println()
                deferred.complete(Unit)
            }
            .onError {
                it.printStackTrace()
                deferred.completeExceptionally(it)
            }
            .start()
        deferred.await()
    }

    override fun chatWithModel(message: String): TokenStream {
        return assistant.chatWithModel(message)
    }

    companion object {
        private fun connectModel(url: String, modelName: String): StreamingChatLanguageModel {
            return OllamaStreamingChatModel.builder()
                .baseUrl(url)
                .modelName(modelName)
                .timeout(Duration.ofHours(1))
                .build()
        }
    }
}
