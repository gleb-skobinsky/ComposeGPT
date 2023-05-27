package com.example.compose.composegpt.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.filament.Engine
import com.google.android.filament.android.UiHelper
import io.github.sceneview.CameraManipulator
import io.github.sceneview.SceneView
import io.github.sceneview.gesture.NodesManipulator
import io.github.sceneview.managers.NodeManager
import io.github.sceneview.nodes.CameraNode
import io.github.sceneview.nodes.Node

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