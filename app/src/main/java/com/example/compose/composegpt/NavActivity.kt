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

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.AttributeSet
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.compose.composegpt.theme.ComposeGPTTheme
import com.google.android.filament.Engine
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import io.github.sceneview.CameraManipulator
import io.github.sceneview.SceneView
import io.github.sceneview.gesture.NodesManipulator
import io.github.sceneview.loaders.loadHdrIndirectLight
import io.github.sceneview.loaders.loadHdrSkybox
import io.github.sceneview.managers.NodeManager
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.nodes.CameraNode
import io.github.sceneview.nodes.ModelNode
import io.github.sceneview.nodes.Node
import kotlinx.coroutines.launch

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
                    transparentSceneView.childNodes.filterIsInstance<ModelNode>().first().playAnimation()
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transparentSceneView.loadHdrIndirectLight("environments/studio_small_09_2k.hdr", specularFilter = true) {
                    intensity(30_000f)
                }
                transparentSceneView.loadHdrSkybox("environments/studio_small_09_2k.hdr")
                val model = transparentSceneView.modelLoader.loadModel("models/chloe_smooth_animated2.glb")!!
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
                    /*
                    CoroutineScope(EmptyCoroutineContext).launch {
                        playAnimation()
                        delay(5000L)
                        stopAnimation()
                    }
                     */
                }
                transparentSceneView.addChildNode(modelNode)
            }
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

    override fun onDestroy() {
        transparentSceneView.destroy()
        transparentSceneView.engine.destroy()
        super.onDestroy()
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

class ChloeSceneView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    /**
     * Provide your own instance if you want to share Filament resources between multiple views.
     */
    sharedEngine: Engine? = null,
    /**
     * Provide your own instance if you want to share [Node]s instances between multiple views.
     */
    sharedNodeManager: NodeManager? = null,
    /**
     * Provide your own instance if you want to share [Node]s selection between multiple views.
     */
    sharedNodesManipulator: NodesManipulator? = null,
    /**
     * Provided by Filament to manage SurfaceView and SurfaceTexture.
     *
     * To choose a specific rendering resolution, add the following line:
     * `uiHelper.setDesiredSize(1280, 720)`
     */
    uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK),
    cameraNode: CameraNode? = null,
    cameraManipulator: ((width: Int, height: Int) -> CameraManipulator)? = null,
    /*{ width, height ->
        Manipulator.Builder()
            .viewport(width, height)
            .flightMaxMoveSpeed(0f)
            .zoomSpeed(0f)
            .orbitSpeed(0f, 0f)
            .flightPanSpeed(0f, 0f)
            .build(Manipulator.Mode.FREE_FLIGHT)
    },
    */
) : SceneView(
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr,
    defStyleRes = defStyleRes,
    sharedEngine = sharedEngine,
    sharedNodeManager = sharedNodeManager,
    sharedNodesManipulator = sharedNodesManipulator,
    uiHelper = uiHelper,
    cameraNode = cameraNode,
    cameraManipulator = cameraManipulator
)

@Composable
fun App(viewModel: ViewModel, onAction: () -> Unit) {
    ComposeGPTTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Text(
                "Meet Chloe, your ultimate virtual assistant",
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp).align(Alignment.TopCenter)
            )
            Row(
                Modifier.padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter)
                    .size(64.dp)
                    .border(width = 1.dp, brush = SolidColor(lightBlue), shape = RoundedCornerShape(50))
                    .background(
                        Brush.radialGradient(
                            listOf(
                                lightBlue,
                                androidx.compose.ui.graphics.Color.Transparent,
                            )
                        ),
                        RoundedCornerShape(50)
                    )
                    .clickable {
                        onAction()
                    }
                    .clip(RoundedCornerShape(50))
            ) {}
        }
    }
}

val lightBlue = androidx.compose.ui.graphics.Color(173, 216, 230)

