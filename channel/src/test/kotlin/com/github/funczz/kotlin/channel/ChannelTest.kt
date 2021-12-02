package io.kotlintest.provided.com.github.funczz.kotlin.channel

import com.github.funczz.kotlin.channel.Channel
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.*

internal class ChannelTest : StringSpec() {

    init {

        "addReceiver" {
            var result1 = ""
            val ch1 = Channel<String>()
            val sender1 = ch1.newSender()
            val receiver1 = Channel.Receiver<String>()
            ch1.addReceiver(receiver1)
            receiver1.attach {
                result1 = it
            }
            var result2 = ""
            val ch2 = Channel<String>()
            val sender2 = ch2.newSender()
            val receiver2 = Channel.Receiver<String>()
            ch2.addReceiver(receiver2)
            receiver2.attach {
                result2 = it
            }

            sender1.send("hello world.")
            sender2.send("HELLO WORLD.")

            result1 shouldBe "hello world."
            result2 shouldBe "HELLO WORLD."
        }

        "newReceiver" {
            var result1 = ""
            val ch1 = Channel<String>()
            val sender1 = ch1.newSender()
            val receiver1 = ch1.newReceiver()
            receiver1.attach {
                result1 = it
            }
            var result2 = ""
            val ch2 = Channel<String>()
            val sender2 = ch2.newSender()
            val receiver2 = ch2.newReceiver()
            receiver2.attach {
                result2 = it
            }

            sender1.send("hello world.")
            sender2.send("HELLO WORLD.")

            result1 shouldBe "hello world."
            result2 shouldBe "HELLO WORLD."
        }

        "removeReceiver" {
            var result1 = ""
            var result2 = ""
            val ch = Channel<String>()
            val sender = ch.newSender()
            val receiver1 = Channel.Receiver<String>()
            ch.addReceiver(receiver1)
            receiver1.attach {
                result1 = it
            }
            val receiver2 = Channel.Receiver<String>()
            ch.addReceiver(receiver2)
            receiver2.attach {
                result2 = it
            }

            ch.removeReceiver(receiver2)
            sender.send("hello world.")

            result1 shouldBe "hello world."
            result2 shouldBe ""
        }

        "clearReceiver" {
            var result1 = ""
            var result2 = ""
            val ch = Channel<String>()
            val sender = ch.newSender()
            val receiver1 = Channel.Receiver<String>()
            ch.addReceiver(receiver1)
            receiver1.attach {
                result1 = it
            }
            val receiver2 = Channel.Receiver<String>()
            ch.addReceiver(receiver2)
            receiver2.attach {
                result2 = it
            }

            ch.clearReceiver()
            sender.send("hello world.")

            result1 shouldBe ""
            result2 shouldBe ""
        }

        "Channel.new" {
            val expected = Optional.of("hello world.")
            var actual = Optional.empty<String>()

            val (sender, receiver, _) = Channel.new<String>()
            receiver.attach {
                actual = Optional.of(it)
            }

            sender.send("hello world.")

            actual shouldBe expected
        }

    }
}