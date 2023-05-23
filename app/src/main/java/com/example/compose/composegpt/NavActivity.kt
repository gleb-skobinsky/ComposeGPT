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

import android.graphics.PixelFormat
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.example.compose.composegpt.theme.ComposeGPTTheme
import com.google.android.filament.View
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.nodes.ModelNode

/**
 * Main activity for the app.
 */
class NavActivity : AppCompatActivity(R.layout.activity) {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var transparentSceneView: SceneView
    private lateinit var composeView: ComposeView
    private lateinit var imageView: android.view.View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        imageView = findViewById(R.id.imageView)
        transparentSceneView = findViewById<SceneView?>(R.id.backgroundSceneView).apply {
            setLifecycle(lifecycle)
        }
        composeView = findViewById<ComposeView>(R.id.composeView).apply {
            setContent {
                ComposeGPTTheme {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button({}) {
                            Text("Hello world")
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            val model = transparentSceneView.modelLoader.loadModel("models/MaterialSuite.glb")!!
            transparentSceneView.setZOrderOnTop(true)
            transparentSceneView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            transparentSceneView.holder.setFormat(PixelFormat.TRANSLUCENT)
            transparentSceneView.filamentView.blendMode = View.BlendMode.TRANSLUCENT
            transparentSceneView.scene.skybox = null
            val options = transparentSceneView.renderer.clearOptions
            options.clear = true
            transparentSceneView.renderer.clearOptions = options
            val modelNode = ModelNode(transparentSceneView, model).apply {
                transform(
                    position = Position(z = -4.0f),
                    rotation = Rotation(x = 15.0f)
                )
                scaleToUnitsCube(2.0f)
                playAnimation()
            }
            transparentSceneView.addChildNode(modelNode)

            imageView.isGone = true
        }
    }

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


    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        supportFragmentManager.commit {
            add(R.id.containerFragment, MainFragment::class.java, Bundle())
        }
    }
     */
}