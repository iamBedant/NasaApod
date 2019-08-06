package com.iambedant.nasaapod.utils.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by @iamBedant on 06,August,2019
 */

class ImmediateSchedulerProvider : ISchedulerProvider {
    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()
}
