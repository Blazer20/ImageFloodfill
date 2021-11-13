package com.example.smartstaffsolutiontask

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.smartstaffsolutiontask.algorithms.Algorithms
import com.example.smartstaffsolutiontask.base.BaseFragment
import com.example.smartstaffsolutiontask.ui.OnBitmapClickListener
import com.example.smartstaffsolutiontask.viewmodel.MyViewModel
import com.example.smartstaffsolutiontask.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MyFragment: BaseFragment() {

    companion object {
        fun create() = MyFragment()
    }

    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBitmapView()
        setupGenerateButton()
        setupSpeedChooser()
        setupAlgorithmPicker()

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MyViewModel::class.java)
        viewModel.fps.observe(viewLifecycleOwner, Observer { fps ->
            setFps(fps)
        })
        viewModel.algorithms.observe(viewLifecycleOwner, Observer { algorithm ->
            algorithm_picker.setSelection(algorithm.ordinal)
        })
        viewModel.image.observe(viewLifecycleOwner, Observer { image ->
            bitmap_view.image = image
            bitmap_width.setText(image.width.toString())
            bitmap_height.setText(image.height.toString())
        })
        viewModel.isBusy.observe(viewLifecycleOwner, Observer { busy ->
            if (busy)
                blockUi()
            else
                unblockUi()
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }


    private fun setupAlgorithmPicker() {
        algorithm_picker.adapter =
            ArrayAdapter.createFromResource(requireContext(), R.array.list_of_algorithms, R.layout.spinner_layout)
        algorithm_picker.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) = Unit

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.onNewAlgorithm(Algorithms.values()[p2])
                }
            }
    }

    private fun setupSpeedChooser() {
        speed_chooser.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.onNewFps(min(60, max(1, (p1 / 1000.0f * 60).roundToInt())))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })
    }

    private fun setupGenerateButton() {
        generate.setOnClickListener {
            val widthString = bitmap_width.text?.toString()
            val heightString = bitmap_height.text?.toString()
            if (widthString.isNullOrBlank() || heightString.isNullOrBlank())
                return@setOnClickListener
            viewModel.onGenerateImage(widthString.toInt(), heightString.toInt())
        }
    }

    private fun setupBitmapView() {
        bitmap_view?.onBitmapClickListener = object : OnBitmapClickListener {
            override fun onBitmapClicked(x: Int, y: Int) {
                viewModel.onBitmapClicked(x, y)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFps(fps: Int) {
        fps_text?.text = "$fps FPS"
        speed_chooser?.progress =
            (((viewModel.fps.value ?: 0) / 60.0f) * 1000).roundToInt()
    }

    private fun unblockUi() {
        generate.isEnabled = true
    }

    private fun blockUi() {
        generate.isEnabled = false
    }

}