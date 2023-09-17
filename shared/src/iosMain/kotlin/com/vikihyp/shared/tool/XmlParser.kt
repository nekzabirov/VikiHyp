package com.vikihyp.shared.tool

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.darwin.NSObject
import platform.Foundation.NSXMLParser
import platform.Foundation.NSXMLParserDelegateProtocol
import platform.Foundation.create

internal fun parseXml(data: ByteArray): HashMap<String, String> {
    var currentName = ""
    var currentValue = ""
    val resourceStrings = hashMapOf<String, String>()

    NSXMLParser(data.toNSData())
        .apply {
            this.delegate = object : NSXMLParserDelegateProtocol, NSObject() {
                override fun parser(parser: NSXMLParser, foundCharacters: String) {
                    if (foundCharacters.isNotEmpty())
                        currentValue = foundCharacters
                }

                override fun parser(
                    parser: NSXMLParser,
                    didStartElement: String,
                    namespaceURI: String?,
                    qualifiedName: String?,
                    attributes: Map<Any?, *>
                ) {
                    currentName = attributes["name"] as? String ?: "no_found"
                }

                override fun parser(
                    parser: NSXMLParser,
                    didEndElement: String,
                    namespaceURI: String?,
                    qualifiedName: String?
                ) {
                    if (didEndElement == "string") {
                        resourceStrings[currentName] = currentValue
                    }
                }
            }
        }
        .parse()

    return resourceStrings
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.toULong()
    )
}