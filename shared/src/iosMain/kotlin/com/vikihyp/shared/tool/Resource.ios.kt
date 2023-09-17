package com.vikihyp.shared.tool

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.posix.memcpy

private val resourcesCache = hashMapOf<String, ByteArray>()

private val stringsCache by lazy {
    parseXml(readBytes("values/string.xml"))
}

@OptIn(ExperimentalForeignApi::class)
private fun readBytes(path: String): ByteArray {
    if (resourcesCache.contains(path))
        return resourcesCache[path]!!

    val absolutePath = NSBundle.mainBundle.resourcePath + "/compose-resources/" + path

    val contentsAtPath: NSData? = NSFileManager.defaultManager().contentsAtPath(absolutePath)

    if (contentsAtPath != null) {
        val byteArray = ByteArray(contentsAtPath.length.toInt())
        byteArray.usePinned {
            memcpy(it.addressOf(0), contentsAtPath.bytes, contentsAtPath.length)
        }
        return byteArray.also { resourcesCache[path] = it }
    } else {
        throw IllegalStateException(path)
    }
}

//TODO(Find way to use compose resource stream)
/*@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun fontResources(font: String): Font {
    val state: MutableState<LoadState<ByteArray>> =
        remember(font) { mutableStateOf(LoadState.Loading()) }

    val resource = resource(font)

    LaunchedEffect(font) {
        state.value = try {
            LoadState.Success(resource.readBytes())
        } catch (e: Exception) {
            LoadState.Error(e)
        }
    }

    return when (state.value) {
        is LoadState.Success -> (state.value as LoadState.Success<ByteArray>).value.let {
            androidx.compose.ui.text.platform.Font(
                font, it
            )
        }
        else -> Font(0)
    }
}*/

@Composable
actual fun fontResources(font: String): Font {
    return androidx.compose.ui.text.platform.Font(font, readBytes("font/${font}.ttf"))
}

@Composable
actual fun stringResource(key: String): String {
    return (stringsCache[key] ?: "Nek no").replace("\\n", "\n")
}

@Composable
actual fun stringResource(key: String, vararg args: Any): String {
    return stringResource(key).let {
        var s = it
        args.forEach { v ->
            s = s.replaceFirst(
                when (v) {
                    is Number -> "%d"
                    else -> "%s"
                },
                v.toString()
            )
        }
        s
    }
}