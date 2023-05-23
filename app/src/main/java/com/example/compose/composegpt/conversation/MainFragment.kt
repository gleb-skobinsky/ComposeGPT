package com.example.compose.composegpt.conversation

/*
class MainFragment : Fragment() {
    lateinit var sceneView: SceneView
    lateinit var composeView: ComposeView

    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById<SceneView>(R.id.transparentSceneView).apply {
            // setLifecycle(lifecycle)
        }
        composeView = view.findViewById<ComposeView>(R.id.composeView).apply {
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
            val hdrFile = "environments/studio_small_09_2k.hdr"
            sceneView.loadHdrIndirectLight(hdrFile, specularFilter = true) {
                intensity(30_000f)
            }
            sceneView.loadHdrSkybox(hdrFile) {
                intensity(50_000f)
            }


            val model = sceneView.modelLoader.loadModel("models/MaterialSuite.glb")!!
            val modelNode = ModelNode(sceneView, model).apply {
                transform(
                    position = Position(z = -4.0f),
                    rotation = Rotation(x = 15.0f)
                )
                scaleToUnitsCube(2.0f)
                playAnimation()
            }
            sceneView.addChildNode(modelNode)

            // somposeView.isGone = true
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(inflater.context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        val composeView = this
        sceneView = SceneView(inflater.context).apply {
            setLifecycle(lifecycle)
        }
        setContent {
            ComposeGPTTheme {
                Box {
                    AndroidView(
                        factory = {
                        SceneView(it).apply {
                            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            setLifecycle(lifecycle)
                        }
                    })
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
            val hdrFile = "environments/studio_small_09_2k.hdr"
            sceneView.loadHdrIndirectLight(hdrFile, specularFilter = true) {
                intensity(30_000f)
            }
            sceneView.loadHdrSkybox(hdrFile) {
                intensity(50_000f)
            }

            val model = sceneView.modelLoader.loadModel("models/MaterialSuite.glb")!!
            val modelNode = ModelNode(sceneView, model).apply {
                transform(
                    position = Position(z = -4.0f),
                    rotation = Rotation(x = 15.0f)
                )
                scaleToUnitsCube(2.0f)
                playAnimation()
            }
            sceneView.addChildNode(modelNode)

            // composeView.isGone = true
        }
    }

}

 */
