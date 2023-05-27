/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.composegpt

import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.compose.composegpt.components.App
import com.google.android.filament.View
import io.github.sceneview.SceneView
import io.github.sceneview.loaders.loadHdrIndirectLight
import io.github.sceneview.loaders.loadHdrSkybox
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.model.getAnimationIndex
import io.github.sceneview.nodes.ModelNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Main activity for the app.
 */
class NavActivity : AppCompatActivity(R.layout.activity) {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var transparentSceneView: SceneView
    private lateinit var composeView: ComposeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        transparentSceneView = findViewById<SceneView?>(R.id.backgroundSceneView).apply {
            setLifecycle(lifecycle)
        }
        composeView = findViewById<ComposeView>(R.id.composeView).apply {
            setContent {
                App(viewModel) {
                    CoroutineScope(EmptyCoroutineContext).launch {
                        val model = transparentSceneView.childNodes
                            .filterIsInstance<ModelNode>()
                            .first()
                        val animation = "ThinkingAnimation"
                        val animationIndex = model.animator.getAnimationIndex(animation)!!
                        val duration = 1000L * model.animator.getAnimationDuration(animationIndex).toLong()
                        model.playAnimation(animation, false)
                        delay(duration)
                        model.stopAnimation(animation)
                        model.playAnimation("TalkingAnimation", true)
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transparentSceneView.loadHdrIndirectLight("environments/studio_small_09_2k.hdr", specularFilter = true) {
                    intensity(30_000f)
                }
                transparentSceneView.loadHdrSkybox("environments/studio_small_09_2k.hdr")
                val model = transparentSceneView.modelLoader.loadModel("models/chloe_smooth_animated6.glb")!!
                transparentSceneView.setZOrderOnTop(false)
                transparentSceneView.setBackgroundColor(Color.TRANSPARENT)
                transparentSceneView.holder.setFormat(PixelFormat.TRANSLUCENT)
                transparentSceneView.filamentView.blendMode = View.BlendMode.TRANSLUCENT
                val options = transparentSceneView.renderer.clearOptions
                options.clear = true
                transparentSceneView.renderer.clearOptions = options
                val modelNode = ModelNode(transparentSceneView, model).apply {
                    transform(
                        position = Position(y = -0.7f, z = -0.5f),
                        rotation = Rotation(x = 0.0f)
                    )
                    scaleToUnitsCube(2.0f)
                }
                transparentSceneView.addChildNode(modelNode)
            }
        }
    }

    /*
    private suspend fun ModelNode.playCompleteAnimation(animation: String) {
        val animationIndex = animator.getAnimationIndex(animation)!!
        val duration = animator.getAnimationDuration(animationIndex).toLong() * 1000L
        playAnimation(animationIndex, false)
    }
     */

    override fun onResume() {
        super.onResume()
        try {
            transparentSceneView.resume()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        transparentSceneView.pause()
    }

    override fun onDestroy() {
        transparentSceneView.destroy()
        transparentSceneView.engine.destroy()
        super.onDestroy()
    }
}



