import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		binarySrcImg("E:/temp/captcha/captcha.png", 140);
	}
	
	public static String RGBtoGray(String path) {		
		Mat srcImg = Imgcodecs.imread(path);
		Mat grayImg = new Mat();
		Imgproc.cvtColor(srcImg, grayImg, Imgproc.COLOR_RGB2GRAY);
		//½µÔë
		Mat blurImg = new Mat();
		Imgproc.blur(grayImg, blurImg, new Size(3, 3));
		String blurpath = new File(path).getParent() + "/blurImg.png";
		Imgcodecs.imwrite(blurpath, grayImg);
		//±ß½ç¼ì²â
		Mat edgeImg = new Mat();
		double threshold = 60;
		Imgproc.Canny(blurImg, edgeImg, threshold, threshold*2);
		//±£´æ
		String dstpath = new File(path).getParent() + "/binaryImg.png";
		Imgcodecs.imwrite(dstpath, edgeImg);
		
		return dstpath;
	}
	
	public static File binarySrcImg(String path, double threshold) {
		File dst = null;
		
		Mat src = Imgcodecs.imread(path);
		double[] temp = new double[src.channels()];
		double min;
		for(int i=0; i<src.cols(); i++) {
			for(int j=0; j<src.rows(); j++) {
				temp = src.get(j, i);
				min = Double.MAX_VALUE;  //±ðÍüÁËÕâÒ»¾ä
				for(int k=0; k<temp.length; k++) {
					min = min < temp[k] ? min : temp[k];
					System.out.println("" + i + " " + j + " " + temp[k] + " " + min);
					temp[k] = 255;
				}
				
				if(min > threshold)
					src.put(j, i, temp);
			}
		}
		
		String dstpath = new File(path).getParent() + "/binary.png";
		Imgcodecs.imwrite(dstpath, src);
		
		return new File(dstpath);
	}
	
	public static Mat BgRemove(String path) {
		Mat frame = Imgcodecs.imread(path);
        Mat hsvImg = new Mat();  
        List<Mat> hsvPlanes = new ArrayList<>();  
        Mat thresholdImg = new Mat();  
  
        int thresh_type = Imgproc.THRESH_BINARY_INV;  
  
        // threshold the image with the average hue value  
        hsvImg.create(frame.size(), CvType.CV_8U);  
        Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);  
        Core.split(hsvImg, hsvPlanes);  
  
        // get the average hue value of the image  
  
        Scalar average = Core.mean(hsvPlanes.get(0));  
        double threshValue = average.val[0];  
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0,  
                thresh_type);  
  
        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));  
  
        // dilate to fill gaps, erode to smooth edges  
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(),  
                new Point(-1, -1), 1);  
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1),  
                3);  
  
        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0,  
                Imgproc.THRESH_BINARY);  
  
        // create the new image  
        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255,  
                255, 255));  
        thresholdImg.convertTo(thresholdImg, CvType.CV_8U);  
        frame.copyTo(foreground, thresholdImg);// ÑÚÄ¤Í¼Ïñ¸´ÖÆ  
        
        String out = new File(path).getParent().concat("/foreground.png");
        Imgcodecs.imwrite(out, foreground);
        return foreground;  
	}

}
