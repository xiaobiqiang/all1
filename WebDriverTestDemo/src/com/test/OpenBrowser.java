package com.test;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.w3c.dom.Document;

import com.qq1312952829.encode.ruokuai.RuoKuai;

public class OpenBrowser {
	
	//����firefox��ִ���ļ���λ�ã����ǰ�װ��Ĭ��λ�þͲ������á�
	//�°治��ֱ�Ӵ�firefox����Ҫ����geckodriver.exe������
	static {
		final String browserPath = "D:/Apps/firefox/firefox.exe" ;
		System.setProperty(
							FirefoxDriver.SystemProperty.BROWSER_BINARY, 
							browserPath
							);
		
		final String driverPath = "G:/webdriver/geckodriver.exe" ;
		System.setProperty(
				"webdriver.gecko.driver", 
				driverPath
				);
	}
	
	private WebDriver firefoxDriver;
	
	private final String libUrl = 
			"http://seat.lib.whu.edu.cn/login?targetUri=%2F";
	private final String userName = "2014301200067";
	private final String password = "6666";
	
	public OpenBrowser() {
//		this.firefoxDriver = new FirefoxDriver();
	}
	
	public void ensureToClock() {
		Calendar time = Calendar.getInstance(Locale.CHINESE);
		long ts = Long.parseLong(	time.get(Calendar.YEAR) + 
									"" +
									time.get(Calendar.MONTH) + 
									time.get(Calendar.DAY_OF_MONTH) +
									"222956"
									);
		long current = 0L;
		do {
			time = Calendar.getInstance(Locale.CHINESE);
			current = Long.parseLong(	time.get(Calendar.YEAR) + 
										"" +
										time.get(Calendar.MONTH) + 
										time.get(Calendar.DAY_OF_MONTH) +
										time.get(Calendar.HOUR_OF_DAY) +
										time.get(Calendar.MINUTE) +
										time.get(Calendar.SECOND)
										);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(current < ts);
	}
	
	


	public static void main(String[] args) {
		new OpenBrowser().startReviewSeat();
	}
	
	public void startReviewSeat() {
//		this.ensureToClock();
		//������
		this.openBrowserByUrl(this.libUrl);
		//��½ͼ���
		this.loginLibrary(this.userName, this.password);
	}

	private void openBrowserByUrl(String url) {
		this.firefoxDriver = new FirefoxDriver();
		//������url
		firefoxDriver.get(url);
		//�������
		firefoxDriver.manage().window().maximize();
	}
	
	private void loginLibrary(String userName, String password) {
		//�ҵ�userName�����ı���Դ���뷢��name = "username"
		WebElement userNameText = this.firefoxDriver.findElement(By.name("username"));
		//�ҵ�password�ı������:Դ���뷢��id = "bor_verification"
		WebElement pwdText = this.firefoxDriver.findElement(By.name("password"));
		//�ҵ���֤�������
		WebElement captchaText = this.firefoxDriver.findElement(By.id("captcha"));
		//�ҵ���½��ť:�˽�xpath,���tr,divʲô�Ĳ��д�1��ʼ������λ������tr[5]
		WebElement loginBtn = this.firefoxDriver.findElement(By.className("btn1"));
		
		//����userName
		userNameText.sendKeys(userName);
		//����password
		pwdText.sendKeys(password);
		//��ȡ��֤��
		String captcha = this.findCAPTCHA();
		//������֤��
		captchaText.sendKeys(captcha);
		//�����½
		loginBtn.click();
		
		try {
			this.findSeatBySearch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String findCAPTCHA() {
		String captchaSavepath = "E:/temp/captcha/captcha.png";
		cutCaptchaImageToFile("code", captchaSavepath);
		String captcha = recognizeImageText(captchaSavepath);
		
		return captcha;
	}
	
	private void cutCaptchaImageToFile(String captchaClassName, String path) {
		byte[] scrshootBytes = ((FirefoxDriver)this.firefoxDriver).getScreenshotAs(OutputType.BYTES);
		
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(scrshootBytes);
			BufferedImage scrshootImg = ImageIO.read(bis);
			
			WebElement captchaLabel = this.firefoxDriver.findElement(By.className(captchaClassName));
			Rectangle labelLocation = captchaLabel.getRect();
			BufferedImage captchaImage = scrshootImg.getSubimage(
															labelLocation.x, 
															labelLocation.y, 
															labelLocation.width, 
															labelLocation.height
															);
			File dstFile= new File(path);
			if(!dstFile.exists()) {
				if(!dstFile.getParentFile().exists())
					dstFile.getParentFile().mkdirs();
				dstFile.createNewFile();
			}
			ImageIO.write(captchaImage, "png", dstFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != bis)
					bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String recognizeImageText(String imgpath) {
		String xml = RuoKuai.createByPost(	"xiaobiqiang", 
											"qqqwwe1995.11.01", 
											"3050",
											"60", 
											"1", 
											"b40ffbee5c1cf4e38028c197eb2fc751", 
											imgpath);
		String out = null;
		DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docbf.newDocumentBuilder();
			Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			out = doc.getElementsByTagName("Result").item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}
	

	public void findSeatBySearch() throws Exception {
		//ѡ������,������ͣ�ý���ˢ����
		Thread.sleep(20);
		this.firefoxDriver.findElement(By.id("display_onDate")).click();
		Thread.sleep(10);
		this.firefoxDriver.findElement(By.xpath("//p[@id='options_onDate']/a[@value='2017-12-01']")).click();
		//ѡ������
		this.firefoxDriver.findElement(By.id("display_building")).click();
		Thread.sleep(10);
		this.firefoxDriver.findElement(By.xpath("//p[@id='options_building']/a[@value='1']")).click();
		//ѡ������,�����򳤶�̫����ʱ����ѡ��Actions��perform
		this.firefoxDriver.findElement(By.id("display_room")).click();
		Robot robot = new Robot();
		robot.mouseMove(1000, 500);
		robot.mouseWheel(5);
		Thread.sleep(500);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		//ѡ����ʼʱ��
		this.firefoxDriver.findElement(By.id("display_startMin")).click();
		Thread.sleep(10);
		this.firefoxDriver.findElement(By.xpath("//p[@id='options_startMin']/a[@value='90']")).click();
		//ѡ������ʱ��
		this.firefoxDriver.findElement(By.id("display_endMin")).click();
		Thread.sleep(10);
		this.firefoxDriver.findElement(By.xpath("//p[@id='options_endMin']/a[@value='1320']")).click();
		//�ҵ���ѯ��ť
		this.firefoxDriver.findElement(By.cssSelector("input.searchBtn.fl")).click();
		//����һ��λ��
		Thread.sleep(20);
		this.firefoxDriver.findElement(By.xpath("//ul[@class='item']/li[1]")).click();
		//ѡ��ʼʱ��
		Thread.sleep(10);
		this.firefoxDriver.findElement(By.xpath("//div[@id='startTime']/dl/ul/li[2]")).click();
		//ѡ����ʱ��,ѡ���һ��
		Thread.sleep(20);
		List<WebElement> endList = this.firefoxDriver.findElements(By.xpath("//div[@id='endTime']/dl/ul/li"));
		endList.get(endList.size()-1).click();
		//�õ���֤��
		String captcha = this.findCAPTCHA();
		//�ҵ���֤�������,������֤��
		this.firefoxDriver.findElement(By.xpath("//input[@id='captchaValue']")).sendKeys(captcha);
		//�ҵ�ȷ��ԤԼ��ť�����
		this.firefoxDriver.findElement(By.id("reserveBtn")).click();
	}
}







//��λѡ��ѧУû��ʵ��
/*		//ѡ����λ���,className�пո�ȸ��Ϸ��Ų���ֱ���ã���cssSelector
		this.firefoxDriver.findElement(By.cssSelector("input.seatBtn.fl")).click();
		//�ҵ���������
		List<WebElement> numList = this.firefoxDriver.findElements(By.className("seatLink"));
		//�õ����ֵ�ÿλ������
		int[] hitNums = intCvtIntArrayByBit(count);
		for(int i=hitNums.length-1; i>=0; i--) {
			int k = hitNums[i];
			if(hitNums[i] == 0)
				k = 10;
			numList.get(k-1).click();
		}
		//ȷ��
		this.firefoxDriver.findElement(By.id("searchBySeatID")).click();*/

//��ֳɵ�λ��ǰ����λ�ں�
/*	public int[] intCvtIntArrayByBit(int count) {
		int temp[] = new int[3];
		
		int size = 0, t;
		do {
			count = (t = count) / 10;
			temp[size++] = t - count * 10;
		}while(count != 0);
		
		int[] dst = new int[size];
		System.arraycopy(temp, 0, dst, 0, size);
		
		return dst;
	}*/

/*	private File ImageCvtTiffByFormat(String srcpath, String format) {
File src = new File(srcpath);
if(!src.exists())
	return null;

Iterator<ImageReader> imgReaderIter = ImageIO.getImageReadersByFormatName(format);
ImageReader imgReader = imgReaderIter.next();

File dst = null;
try {
	ImageInputStream iis = ImageIO.createImageInputStream(src);
	imgReader.setInput(iis);
	
	IIOMetadata srcMetadata = imgReader.getStreamMetadata();
	
	TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.CHINESE);    
    tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);    
	
	Iterator<ImageWriter> imgWriterIter = ImageIO.getImageWritersByFormatName("tiff");
	ImageWriter imgWriter = imgWriterIter.next();
	dst = cvtFileName(src);
	ImageOutputStream ios = ImageIO.createImageOutputStream(dst);
	imgWriter.setOutput(ios);
	
	BufferedImage bfimg = imgReader.read(0);
	IIOImage iioImage = new IIOImage(bfimg, null, imgReader.getImageMetadata(0));
	imgWriter.write(srcMetadata, iioImage, tiffWriteParam);
	
	ios.close();
	imgWriter.dispose();
	iis.close();
	imgReader.dispose();
} catch (IOException e) {
	e.printStackTrace();
}

return dst;
}

private File cvtFileName(File src) {
String path = src.getPath();

return new File(path.substring(0, path.lastIndexOf('.')+1) + "tif");
}

private File largeImgByScale(File srcImg, double scale, String out) {
File dst = new File(out);

try {
	
	Thumbnails.of(srcImg).scale(2).toFile(dst);
} catch (IOException e) {
	e.printStackTrace();
}

return dst;
}*/

/*public String recognizeImageText(String imagepath, String format) {
String scaleImgPath = new StringBuilder(imagepath).insert(
		imagepath.lastIndexOf('.'), 0).toString();
File scaleImgFile = largeImgByScale(new File(imagepath), 2, scaleImgPath);
File tifFile = ImageCvtTiffByFormat(scaleImgFile.getAbsolutePath(), format);
String tessPath = "F:/Tesseract-OCR/tesseract.exe";
String srcPath = tifFile.getAbsolutePath();
String outPath = new File(scaleImgPath).getParent() + "/captcha_decoder";
String lang = "-l eng";
String captcha = null;

try {
	Process proc = Runtime.getRuntime().exec(tessPath + " " + srcPath + " " + outPath + " " + lang);
	//�����̶߳�ȡ�������������ô��󻺳����������������
	new Thread() {
		@Override
		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line = null;
			try {
				while((line=br.readLine()) != null){
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}.start();
	
	//�ȴ��ӽ���ִ���꣬�����˳�����0
	int state = proc.waitFor();
	if(state == 0) {
		BufferedReader bufReader = new BufferedReader(
											new InputStreamReader(
													new FileInputStream(
															outPath+".txt"
															)
													)
											);
		captcha = bufReader.readLine();
		bufReader.close();
	}
} catch (Exception e) {
	e.printStackTrace();
}

return captcha;
}*/


