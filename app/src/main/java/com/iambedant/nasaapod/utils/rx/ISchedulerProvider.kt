package com.iambedant.nasaapod.utils.rx

import io.reactivex.Scheduler

/**
 * Created by @iamBedant on 06,August,2019
 */

interface ISchedulerProvider {
    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}