camera 相机介绍
camerainfo （1、facing前置摄像头和后置摄像头camera_facing_back后置，camera_facing_front前置。2、orientation图像采集的方向0,90，180,270）
图像传感器 image sensor
setdisplayorientation(图像预览方向)
相机传感方向上下x坐标，左右y坐标（前置摄像头坐下开始往上x，往右y。后置摄像头右上开始往左y，往下x）
size 图片大小（width，height宽高）
parameters相机服务设置
             getSupportedPreviewSizes()
             获得相机支持的预览图片大小，返回值是一个List<Size>数组
             setPreviewSize(int width, int height)
             设置相机预览图片的大小
             getSupportedPreviewFormats()
             获得相机支持的图片预览格式，所有的相机都支持ImageFormat.NV21
             更多的图片格式可以自行百度或是查看ImageFormat类
             setPreviewFormat(int pixel_format)
             设置预览图片的格式
             getSupportedPictureSizes()
             获得相机支持的采集的图片大小(即拍照后保存的图片的大小)
             setPictureSize(int width, int height)
             设置保存的图片的大小
             getSupportedPictureFormats()
             获得相机支持的图片格式
             setPictureFormat(int pixel_format)
             设置保存的图片的格式
             getSupportedFocusModes()
             获得相机支持的对焦模式
             setFocusMode(String value)
             设置相机的对焦模式
             getMaxNumDetectedFaces()
             返回当前相机所支持的最大的人脸检测个数
             比如，我的手机是vivo x9，后置摄像头所支持最大的人脸检测个数是10，也就是说当画面中人脸数超过10个的时候，只能检测到10张人脸
PreviewCallback
            PreviewCallback是一个抽象接口
            void onPreviewFrame(byte[] data, Camera camera)
            通过onPreviewFrame方法来获取到相机预览的数据，第一个参数data，就是相机预览到的原始数据。
            这些预览到的原始数据是非常有用的，比如我们可以保存下来当做一张照片，还有很多第三方的人脸检测及静默活体检测的sdk，都需要我们把相机预览的数据实时地传递过去。
Face
            Face类用来描述通过Camera的人脸检测功能检测到的人脸信息
            rect
            rect 是一个Rect对象，它所表示的就是检测到的人脸的区域。
            注意：这个Rect对象中的坐标系并不是安卓屏幕的坐标系，需要进行转换后才能使用，具体会在后面实现人脸检测功能的时候详细介绍
score
            检测到的人脸的可信度，范围是1 到100
            leftEye
            leftEye 是一个Point对象，表示检测到的左眼的位置坐标
            rightEye
            rightEye是一个Point对象，表示检测到的右眼的位置坐标
            mouth
            mouth是一个Point对象，表示检测到的嘴的位置坐标
            leftEye ，rightEye和mouth这3个人脸中关键点，并不是所有相机都支持的，如果相机不支持的话，这3个的值为null
FaceDetectionListener
            这是一个抽象接口，当开始人脸检测时开始回调
            onFaceDetection(Face[] faces, Camera camera)
            第一参数代表检测到的人脸，是一个Face数组(画面内可能存在多张人脸)
Camera类中的方法
            getNumberOfCameras()
            返回当前设备上可用的摄像头个数
            open()
            使用当前设备上第一个后置摄像头创建一个Camera对象。如果当前设备没有后置摄像头，则返回值为null
            open(int cameraId)
            使用传入id所表示的摄像头创建一个Camera对象，如果id所表示的摄像头已经被打开，则会抛出异常
总结
            Camera负责采集数据和各种操作，SurfaceView负责把Camera采集到的数据实时地显示在屏幕上
            我们通过SuraceHolder中的回调可以监听Surface的状态（创建、变化、销毁）
            相机图像数据都是来自于相机硬件的图像传感器这个方向是不能改变的
            相机在预览的时候是有一个预览方向的，可以通过setDisplayOrientation()设置
            前置摄像头在进行角度旋转之前，图像会进行一个水平的镜像翻转，所以用户在看预览图像的时候就像照镜子一样
            相机所采集的照片也是有一个方向的，这个方向与预览时的方向互不相干
            我们可以通过setParameters(Parameters params)设置当前相机的参数信息，比如 保存的图片大小，对焦模式等等
            在关闭页面 或者 打开其他摄像头之前，一定要先调用release()方法释放当前相机资源
