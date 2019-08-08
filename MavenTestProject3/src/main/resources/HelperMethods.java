package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import config.Constants;

public class HelperMethods {
public static Properties OR;
public static String captureScreenshot(String testDetails, String message, WebDriver driver) throws IOException{
File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
// Now you can do whatever you need to do with it, for example copy somewhere
Calendar currentDate = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy/MMM/dd HH:mm:ss");
    String date = formatter.format(currentDate.getTime()).replace(
            "/", "_");
    String dateFormated = date.replace(":", "_");
    String filePath = 
            "D:\\SeleniumScreenShots\\" + testDetails
                + "_" + dateFormated + ".png";
    FileUtils.copyFile(scrFile, new File(filePath));
//FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
System.out.println("SC: "+ message);
return filePath;
}
public static Properties prepareOr() throws Exception {
//System.out.println(Constants.Path_TestData);
String Path_OR = Constants.Path_OR;
FileInputStream fs;
try {
fs = new FileInputStream(Path_OR);

OR = new Properties(System.getProperties());
// Loading all the properties from Object Repository property file in to OR
// object

OR.load(fs);
} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
e.printStackTrace();
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
return OR;
}
public static String imageMarkupHelper(String imgPath) throws Exception {
String temp = "<img width=\"20%\" src=\""+imgPath+"\" data-featherlight = \"" + imgPath + "\" data-src = \"" + imgPath+ "\">";
return temp;
}
}