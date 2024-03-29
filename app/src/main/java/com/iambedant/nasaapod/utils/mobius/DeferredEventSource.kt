/*
 * -\-\-
 * --
 * Copyright (c) 2017-2018 Spotify AB
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -/-/-
 */
package com.iambedant.nasaapod.utils.mobius

import com.spotify.mobius.EventSource
import com.spotify.mobius.disposables.Disposable
import com.spotify.mobius.functions.Consumer
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class DeferredEventSource<E> : EventSource<E> {
    private val events = LinkedBlockingQueue<E>()

    override fun subscribe(eventConsumer: Consumer<E>): Disposable {
        val run = AtomicBoolean(true)
        val t = Thread {
            while (run.get()) {
                try {
                    val e = events.take()
                    if (run.get()) {
                        eventConsumer.accept(e)
                    }
                } catch (e: InterruptedException) {
                }

            }
        }
        t.start()
        return Disposable {
            run.set(false)
            t.interrupt()
        }
    }

    @Synchronized
    fun notifyEvent(e: E) {
        events.offer(e)
    }
}
