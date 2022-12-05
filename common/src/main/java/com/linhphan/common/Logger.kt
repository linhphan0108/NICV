package com.linhphan.common

import android.util.Log

/**
 * @author linhphan
 */
object Logger {

    private val enable = BuildConfig.DEBUG

    fun v(tag: String?, msg: String) {
        if (enable){
            println("VERBOSE: $tag: $msg")
        }
    }

    fun v(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            println("VERBOSE: $tag: $msg")
            tr?.printStackTrace()
        }
    }

    fun d(tag: String?, msg: String) {
        if (enable){
            println("DEBUG: $tag: $msg")
        }
    }

    fun d(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            println("ERROR: $tag: $msg")
            tr?.printStackTrace()
        }
    }

    fun i(tag: String?, msg: String) {
        if (enable){
            println("INFO: $tag: $msg")
        }
    }

    fun i(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            println("INFO: $tag: ${tr?.message}")
            tr?.printStackTrace()
        }
    }

    fun w(tag: String?, msg: String) {
        if (enable){
            println("WARN: $tag: $msg")
        }
    }

    fun w(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            println("WARN: $tag: $msg")
            tr?.printStackTrace()
        }
    }

    fun w(tag: String?, tr: Throwable?) {
        if (enable){
            println("WARN: $tag: ${tr?.message}")
        }
    }

    fun e(tag: String?, msg: String) {
        if (enable){
            println("ERROR: $tag: $msg")
        }
    }

    fun e(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            println("ERROR: $tag: $msg")
            tr?.printStackTrace()
        }
    }

    fun wtf(tag: String?, msg: String?) {
        if (enable){
            println("wtf: $tag: $msg")
        }
    }

    fun wtf(tag: String?, tr: Throwable) {
        if (enable){
            println("wtf: $tag: ${tr.stackTraceToString()}")
        }
    }

    fun wtf(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            println("wtf: $tag: $msg")
            println("wtf: $tag: ${tr?.stackTraceToString()}")
        }
    }
}