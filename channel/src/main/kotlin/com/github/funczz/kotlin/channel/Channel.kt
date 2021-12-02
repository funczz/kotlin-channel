package com.github.funczz.kotlin.channel

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Channel<V : Any> {

    private val lock = ReentrantLock(true)

    private val receivers: MutableList<Receiver<V>> = mutableListOf()

    fun newSender(): Sender = Sender()

    fun newReceiver(): Receiver<V> = withLock {
        val receiver = Receiver<V>()
        receivers.add(receiver)
        receiver
    }

    fun addReceiver(receiver: Receiver<V>) = withLock {
        receivers.add(receiver)
    }

    fun clearReceiver() = withLock {
        receivers.clear()
    }

    fun removeReceiver(receiver: Receiver<V>) = withLock {
        receivers.remove(receiver)
    }

    /**
     * ファンクションを排他 Lock <code>lock</code> による排他制御下で実行する
     * 非公開メソッド
     * @param function 実行するファンクション
     * @return ファンクションの実行結果を返す
     */
    private inline fun <R> withLock(function: () -> R): R = lock.withLock(function)

    companion object {
        @JvmStatic
        fun <V : Any> new(): Triple<Channel<V>.Sender, Receiver<V>, Channel<V>> {
            val channel = Channel<V>()
            val sender = channel.Sender()
            val receiver = Receiver<V>()
            channel.addReceiver(receiver)
            return Triple(sender, receiver, channel)
        }
    }

    inner class Sender {

        fun send(item: V, f: (V) -> V = { item }) = withLock {
            receivers.forEach {
                it.receive(f(item))
            }
        }

    }

    class Receiver<V : Any> {

        private var function: (V) -> Unit = {}

        fun attach(function: (V) -> Unit) {
            this.function = function
        }

        fun receive(item: V) {
            function(item)
        }

    }

}