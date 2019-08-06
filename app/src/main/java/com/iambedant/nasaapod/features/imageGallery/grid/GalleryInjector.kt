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
package com.iambedant.nasaapod.features.imageGallery.grid


import com.iambedant.nasaapod.utils.mobius.loopFactory
import com.spotify.mobius.EventSource
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import io.reactivex.ObservableTransformer

fun createController(
    effectHandler: ObservableTransformer<GalleryEffect, GalleryEvent>,
    eventSource: EventSource<GalleryEvent>,
    defaultModel: GalleryModel
): MobiusLoop.Controller<GalleryModel, GalleryEvent> =
    MobiusAndroid.controller(
        createLoop(eventSource, effectHandler),
            defaultModel)


private fun createLoop(
    eventSource: EventSource<GalleryEvent>,
    effectHandler: ObservableTransformer<GalleryEffect, GalleryEvent>)
        : MobiusLoop.Factory<GalleryModel, GalleryEvent, GalleryEffect> =
    loopFactory(::update, effectHandler)
            .init(::init)
            .eventSource(eventSource)
            .logger(AndroidLogger.tag("NasaApodLogger"))
