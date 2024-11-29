package video

import exceptions.OsNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.util.Locale
import java.util.regex.Pattern

object HlsHelper {

    enum class OS(val data: String) {
        MAC("mac"), LINUX("linux");

    }

    enum class ARCH(val data: String) {
        X86("x86"), AARCH64("aarch64")
    }

    suspend fun downloadHLS(url: String, outputFilePath: String, onFirstSegmentWritten: () -> Unit) {
        val exe = pickExecutable()
        val headers = listOf(
            "-h", "Origin: https://vidco.pro",
            "-h", "Sec-Ch-Ua: \"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"",
            "-h", "Sec-Fetch-Dest: empty",
            "-h", "Sec-Fetch-Mode: cors",
            "-h", "Sec-Fetch-Site: cross-site",
            "-h", "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
        )

        val command = mutableListOf<String>().apply {
            add(exe)
            add("-b")
            add("-v")
            add("-o")
            add(outputFilePath)
            addAll(headers)
            add(url)
        }

        withContext(Dispatchers.IO) {
            val process = Runtime.getRuntime()
                .exec(command.toTypedArray())


            interceptOutput(process.errorReader(), onFirstSegmentWritten)
            interceptOutput(process.inputReader(), onFirstSegmentWritten)

            // Wait for the process to complete
            val exitCode = process.waitFor()
            println("Process exited with code: $exitCode")
        }
    }

    private fun interceptOutput(
        bufferedReader: BufferedReader?,
        onFirstSegmentWritten: () -> Unit
    ) {
        bufferedReader.use { reader ->
            reader?.forEachLine { line ->
                // Perform regex matching and other operations on each line
                println(line)
                val totalDurationPattern = "\"t_d\":(\\d+)"
                val downloadedDurationPattern = "\"d_d\":(\\d+)"
                val downloadSizePattern = "\"d_s\":(\\d+)"

                val totalDuration = extractValue(line, totalDurationPattern)
                val downloadedDuration = extractValue(line, downloadedDurationPattern)
                val downloadSize = extractValue(line, downloadSizePattern)


                if ((downloadedDuration.toIntOrNull() ?: 0)  > 30) {
                    // Execute callback function on UI thread
                    runBlocking {
                        withContext(Dispatchers.Main) {
                            onFirstSegmentWritten()
                        }
                    }
                }
            }
        }
    }

    private fun pickExecutable(): String {
        // check os
        val os = os()

        var osFlag = ""
        for (osFound in OS.entries) {
            if (os.contains(osFound.data, true)) {
                osFlag = osFound.data
            }
        }
        val arch = arch()
        var archFlag = ""
        for (archFound in ARCH.entries) {
            if (arch.contains(archFound.data, true)) {
                archFlag = archFound.data
            }
        }

        if (osFlag.isEmpty() || archFlag.isEmpty()) {
            throw OsNotFoundException()
        } else {
            return "lib/hlsdl-$osFlag-$arch"
        }
    }


    fun extractValue(input: String, pattern: String): String {
        val matcher = Pattern.compile(pattern).matcher(input)
        return if (matcher.find()) matcher.group(1) else "N/A"
    }

    private fun os() = System
            .getProperty("os.name", "generic")
            .lowercase(Locale.ENGLISH)

    private fun arch() = System
        .getProperty("os.arch")
        .lowercase(Locale.ENGLISH)
}