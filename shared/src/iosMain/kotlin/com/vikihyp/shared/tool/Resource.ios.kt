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

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun readBytes(path: String): ByteArray {
    val absolutePath = NSBundle.mainBundle.resourcePath + "/" + path
    val contentsAtPath: NSData? = NSFileManager.defaultManager().contentsAtPath(absolutePath)
    if (contentsAtPath != null) {
        val byteArray = ByteArray(contentsAtPath.length.toInt())
        byteArray.usePinned {
            memcpy(it.addressOf(0), contentsAtPath.bytes, contentsAtPath.length)
        }
        return byteArray
    } else {
        throw IllegalStateException(path)
    }
}

@Composable
actual fun fontResources(font: String): Font {
    return androidx.compose.ui.text.platform.Font(font, readBytes("font/${font}.ttf"))
}

@Composable
actual fun stringResource(key: String): String {
    TODO("Not yet implemented")
}

@Composable
actual fun stringResource(key: String, vararg args: Any): String {
    TODO("Not yet implemented")
}