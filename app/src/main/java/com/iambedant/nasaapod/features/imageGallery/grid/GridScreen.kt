package com.iambedant.nasaapod.features.imageGallery.grid

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.iambedant.nasaapod.NasaApodApp
import com.iambedant.nasaapod.R
import com.iambedant.nasaapod.data.model.ApodUI
import com.iambedant.nasaapod.data.repository.IGalleryRepository
import com.iambedant.nasaapod.features.imageGallery.CurrentData
import com.iambedant.nasaapod.features.imageGallery.detail.DetailScreen
import com.iambedant.nasaapod.utils.gone
import com.iambedant.nasaapod.utils.invisible
import com.iambedant.nasaapod.utils.mobius.DeferredEventSource
import com.iambedant.nasaapod.utils.mobius.onAccept
import com.iambedant.nasaapod.utils.rx.ISchedulerProvider
import com.iambedant.nasaapod.utils.visible
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class GridScreen : AppCompatActivity(), IMobiusGalleryView, Connectable<GalleryModel, GalleryEvent> {

    private val adapter by lazy {
        ImageAdapter(
            listOf(),
            ::imageTapped
        )
    }

    @Inject
    lateinit var repository: IGalleryRepository
    @Inject
    lateinit var schedulerProvider: ISchedulerProvider


    private val eventSource = DeferredEventSource<GalleryEvent>()
    private lateinit var mController: MobiusLoop.Controller<GalleryModel, GalleryEvent>
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as NasaApodApp).createonImageGalleryComponent()?.inject(this)

        layoutManager = GridLayoutManager(this, 2)

        rv.layoutManager = layoutManager
        rv.adapter = adapter
        mController = createController(
            createEffectHandler(this, repository, schedulerProvider),
            eventSource,
            resolveDefaultModel()
        )
        mController.connect(this)
        mController.start()
    }

    private fun resolveDefaultModel() =
        CurrentData.model?.copy(currentItemPosition = CurrentData.currentPosition) ?: GalleryModel()

    private fun imageTapped(apod: ApodUI) {
        eventSource.notifyEvent(ClickEvent(apod))
    }


    override fun connect(output: Consumer<GalleryEvent>): Connection<GalleryModel> {
        addUiListeners(output)
        return onAccept<GalleryModel> { render(it) }
            .onDispose {

            }
    }

    private fun render(it: GalleryModel) {
        with(it) {
            if (isError) {
                ivError.visible()
                rv.invisible()
                progressBar.gone()
            } else {
                ivError.gone()
                rv.visible()
                progressBar.visible()
            }

            if (loading) {
                progressBar.visible()
                rv.gone()
            } else {
                rv.visible()
                progressBar.invisible()
            }
            display(it = listOfImages)
            CurrentData.model = this
        }
    }

    private fun addUiListeners(output: Consumer<GalleryEvent>) {
//        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                output.accept(UpdateFirstVisibleItemEvent(layoutManager.findFirstVisibleItemPosition()))
//            }
//        })
    }

    override fun updateClickedItem(clickedItem: Int) {
        CurrentData.selectedPosition = clickedItem
    }

    override fun scrollToPosition(currentItemPosition: Int) {
        rv.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                rv.removeOnLayoutChangeListener(this)
                val viewAtPosition = layoutManager.findViewByPosition(currentItemPosition)
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    rv.post { layoutManager.scrollToPosition(currentItemPosition) }
                }
            }
        })
    }

    override fun navigateToDetail() {
        CurrentData.currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        startActivity(Intent(this, DetailScreen::class.java))
    }

    override fun display(it: List<ApodUI>) {
        val callback = ImageDiffCallback(adapter.getMoments(), it)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setMoments(it)
        result.dispatchUpdatesTo(adapter)
    }

    override fun onDestroy() {
        mController.stop()
        mController.disconnect()
        super.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        CurrentData.currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        super.onSaveInstanceState(outState)
    }
}
