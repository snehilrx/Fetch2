// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.
package org.cef.handler

import org.cef.CefApp
import org.cef.CefApp.CefAppState
import org.cef.callback.CefCommandLine
import org.cef.callback.CefSchemeRegistrar
import org.cef.misc.CefLog

/**
 * An abstract adapter class for managing app handler events.
 * The methods in this class are using a default implementation.
 * This class exists as convenience for creating handler objects.
 */
val unsafeArgs = arrayOf(
    "--remote-allow-origins=*",
    "--disable-web-security",
    "--user-data-dir=cache"
    )
abstract class CefAppHandlerAdapter(args: Array<String>?) : CefAppHandler {
    private var args_: Array<String>?

    init {
        args_ = args?.plus(unsafeArgs)
    }

    fun updateArgs(args: Array<String>?) {
        args_ = args?.plus(unsafeArgs)
    }

    override fun onBeforeCommandLineProcessing(process_type: String, command_line: CefCommandLine) {
        if (process_type.isEmpty() && args_ != null) {
            // Forward switches and arguments from Java to Cef
            var parseSwitchesDone = false
            for (arg in args_!!) {
                if (parseSwitchesDone || arg.length < 2) {
                    command_line.appendArgument(arg)
                    continue
                }
                // Arguments with '--', '-' and, on Windows, '/' prefixes are considered switches.
                val switchCnt =
                    if (arg.startsWith("--")) 2 else if (arg.startsWith("/")) 1 else if (arg.startsWith(
                            "-"
                        )
                    ) 1 else 0
                when (switchCnt) {
                    2 -> {
                        // An argument of "--" will terminate switch parsing with all subsequent
                        // tokens
                        if (arg.length == 2) {
                            parseSwitchesDone = true
                            continue
                        }
                        run {

                            // Switches can optionally have a value specified using the '=' delimiter
                            // (e.g. "-switch=value").
                            val switchVals = arg.substring(switchCnt).split("=".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                            if (switchVals.size == 2) {
                                command_line.appendSwitchWithValue(switchVals[0], switchVals[1])
                            } else {
                                command_line.appendSwitch(switchVals[0])
                            }
                        }
                    }

                    1 -> {
                        val switchVals = arg.substring(switchCnt).split("=".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        if (switchVals.size == 2) {
                            command_line.appendSwitchWithValue(switchVals[0], switchVals[1])
                        } else {
                            command_line.appendSwitch(switchVals[0])
                        }
                    }

                    0 -> command_line.appendArgument(arg)
                }
            }
        }
    }

    override fun onBeforeTerminate(): Boolean {
        // The default implementation does nothing
        return false
    }

    override fun stateHasChanged(state: CefAppState) {
        // The default implementation does nothing
    }

    override fun onRegisterCustomSchemes(registrar: CefSchemeRegistrar) {
        // The default implementation does nothing
    }

    override fun onContextInitialized() {
        // The default implementation does nothing
    }

    override fun onScheduleMessagePumpWork(delay_ms: Long) {
        if (CefApp.getState() == CefAppState.TERMINATED) {
            CefLog.Debug("CefApp is terminated, skip doMessageLoopWork")
            return
        }
        CefApp.getInstance().doMessageLoopWork(delay_ms)
    }

    override fun onBeforeChildProcessLaunch(command_line: String) {
        // The default implementation does nothing
    }
}
