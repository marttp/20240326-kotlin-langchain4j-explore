interface UserStreamCommunication {

    suspend fun ask(prompt: String)
}
