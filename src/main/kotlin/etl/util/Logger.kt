package etl.util

import java.time.LocalDateTime
import kotlin.reflect.KClass

class Logger private constructor(private val name: String) {

    fun info(msg: String) {
        log("[${timestamp()}] [$name] [INFO] $msg")
    }

    fun warn(msg: String) {
        log("[${timestamp()}] [$name] [WARN] $msg")
    }

    fun error(msg: String) {
        log("[${timestamp()}] [$name] [ERROR] $msg")
    }

    private fun log(message: String) {
        println(message) // console
        logFile?.appendText(message + "\n") // file
    }

    companion object {
        private val cache = mutableMapOf<String, Logger>()

        //file is optional (can be turned on/off)
        var logFile: java.io.File? = null

        fun forClass(clazz: KClass<*>): Logger {
            val name = clazz.simpleName ?: "Unknown"
            return cache.getOrPut(name) { Logger(name) }
        }

        private fun timestamp(): String =
            LocalDateTime.now().toString()
    }
}