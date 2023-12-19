package com.migalska.imageviewer.sfm

data class Camera (
    val views: MutableList<CameraView>,
    val intrinsics: MutableList<Intrinsic>,
    val poses: MutableList<Pose>,
)

data class CameraView (
    val viewId: String,
    val poseId: String,
    val intrinsicId: String,
    val path: String,
    val width: String,
    val height: String,
)

data class Intrinsic (
    val intrinsicId: String,
    val pxFocalLength: String,
    val principalPoint: MutableList<String>,
)

data class Pose (
    val poseId: String,
    val pose: PoseDetails,
)

data class PoseDetails (
    val transform: Transform,
)

data class Transform (
    val rotation: MutableList<String>,
    val center: MutableList<String>,
)
