package com.multiplatform.webview.web

import coil3.annotation.InternalCoilApi
import coil3.util.MimeTypeMap
import com.multiplatform.webview.setting.WebSettings
import dev.datlag.kcef.KCEFResourceRequestHandler
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.browser.CefRequestContext
import org.cef.misc.BoolRef
import org.cef.network.CefRequest
import org.cef.network.CefResponse
import org.cef.network.CefURLRequest

const val JS = """
    console.log("FRAME");
    if (player != undefined) {
        var item = btoa(JSON.stringify(player.getConfig()))
        if (item != undefined) {
            window.cefQuery({request: JSON.stringify({callbackId: 0,methodName: "Hack",params: item})});
        }
    }
"""


internal fun createModifiedRequestContext(settings: WebSettings): CefRequestContext {


    return CefRequestContext.createContext { browser, frame, request, isNavigation, isDownload, requestInitiator, disableDefaultHandling ->
        object : KCEFResourceRequestHandler(
            getGlobalDefaultHandler(browser, frame, request, isNavigation, isDownload, requestInitiator, disableDefaultHandling),
        ) {

            override fun onResourceLoadComplete(
                browser: CefBrowser?,
                frame: CefFrame?,
                request: CefRequest?,
                response: CefResponse?,
                status: CefURLRequest.Status?,
                receivedContentLength: Long
            ) {
                super.onResourceLoadComplete(
                    browser,
                    frame,
                    request,
                    response,
                    status,
                    receivedContentLength
                )

                if (request?.url?.contains("player.php") == true) {
                    frame?.executeJavaScript(JS, "", 0)
                }
            }

            override fun onProtocolExecution(
                browser: CefBrowser?,
                frame: CefFrame?,
                request: CefRequest?,
                allowOsExecution: BoolRef?
            ) {
                super.onProtocolExecution(browser, frame, request, BoolRef(true))
            }

            override fun onBeforeResourceLoad(
                browser: CefBrowser?,
                frame: CefFrame?,
                request: CefRequest?,
            ): Boolean {
                if (request != null) {
                    settings.customUserAgentString?.let(request::setUserAgentString)
                }
                val url = request?.url
                val mimeType = getMimeType(url = url)
                if (containsBlockedKeyword(url)
                    || mimeType?.startsWith("font") == true
                    || mimeType?.startsWith("image") == true
                    || mimeType?.startsWith("stylesheet") == true
                ) {
                    return true
                }
                return super.onBeforeResourceLoad(browser, frame, request)
            }
        }
    }
}

internal fun CefRequest.setUserAgentString(userAgent: String) {
    setHeaderByName("User-Agent", userAgent, true)
}

@OptIn(InternalCoilApi::class)
private fun getMimeType(url: String?): String? {
    return MimeTypeMap.getMimeTypeFromUrl(url ?: return null)
}

private fun containsBlockedKeyword(url: String?): Boolean {
    val blockedKeywords = listOf(
        //"css",
        "doctorenticeflashlights.com",
        "kickassanime.disqus.com",
        "simplewebanalysis",
        "google",
        "manifest",
        "/api/show/"
    )

    for (keyword in blockedKeywords) {
        if (url?.contains(keyword, ignoreCase = true) == true) {
            return true
        }
    }
    return false
}