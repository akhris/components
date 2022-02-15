package utils

import com.akhris.domain.core.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.charset.Charset

object FileUtils {


    suspend fun writeText(filePath: String, text: String) = withContext(Dispatchers.IO) {
        log("write file on thread: ${Thread.currentThread()}")
        File(filePath).writeText(text, Charset.defaultCharset())
    }

    suspend fun readText(filePath: String): String = withContext(Dispatchers.IO) {
        log("read file on thread: ${Thread.currentThread()}")
        File(filePath).readText(charset = Charset.defaultCharset())
    }

    suspend fun isFileExists(filePath: String): Boolean = withContext(Dispatchers.IO) {
        File(filePath).exists()
    }

}