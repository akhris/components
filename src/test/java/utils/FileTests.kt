package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

internal class FileTests {


    @Test
    fun get_system_dir() {
        val currentUserPath = System.getProperty("user.home")
        println(currentUserPath)
    }

    @Test
    fun read_string_from_file() {
        val path = """${System.getProperty("user.home")}/test.txt"""
        val s = runBlocking {
            withContext(Dispatchers.IO) {
                FileUtils.readText(path)
            }
        }
        println(s)
    }

    @Test
    fun write_string_to_file(){
        val path = """${System.getProperty("user.home")}/test.txt"""
        runBlocking {
            withContext(Dispatchers.IO) {
                FileUtils.writeText(path,"hello, guys")
            }
        }
    }

    @Test
    fun path_append_test(){
        val path = "/home/user"
        val fileName = "fileName.txt"
        val appended = Path(path, fileName)
        println(appended)
    }
}