import kotlinx.coroutines.runBlocking

private const val OLLAMA_HOST = "http://localhost:11434"

fun main() = runBlocking {
    val chatService = ChatService(OLLAMA_HOST, "llama2")
    while (true) {
        println(
            """
                Type 'exit' to quit the program.
                Enter your prompt: """.trimIndent()
        )
        val userPrompt = readln()
        if (userPrompt == "exit") {
            break
        }
        chatService.ask(userPrompt) // This will now suspend until the TokenStream completes
    }
}