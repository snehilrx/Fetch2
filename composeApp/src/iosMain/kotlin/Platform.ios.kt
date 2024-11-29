import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

//    @Composable
//    override fun VideoPlayer(modifier: Modifier, url: String) {
//        val player = remember { AVPlayer(uRL = NSURL.URLWithString(url)!!) }
//        val playerLayer = remember { AVPlayerLayer() }
//        val avPlayerViewController = remember { AVPlayerViewController() }
//        avPlayerViewController.player = player
//        avPlayerViewController.showsPlaybackControls = true
//
//        playerLayer.player = player
//        // Use a UIKitView to integrate with your existing UIKit views
//        UIKitView(
//            factory = {
//                // Create a UIView to hold the AVPlayerLayer
//                val playerContainer = UIView()
//                playerContainer.addSubview(avPlayerViewController.view)
//                // Return the playerContainer as the root UIView
//                playerContainer
//            },
//            onResize = { view: UIView, rect: CValue<CGRect> ->
//                CATransaction.begin()
//                CATransaction.setValue(true, kCATransactionDisableActions)
//                view.layer.setFrame(rect)
//                playerLayer.setFrame(rect)
//                avPlayerViewController.view.layer.frame = rect
//                CATransaction.commit()
//            },
//            update = {
//                player.play()
//                avPlayerViewController.player!!.play()
//            },
//            modifier = modifier)
//    }
}

actual fun getPlatform(): Platform = IOSPlatform()