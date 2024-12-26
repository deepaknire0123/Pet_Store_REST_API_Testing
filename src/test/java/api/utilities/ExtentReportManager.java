/**
 * 
 */
package api.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


/**
 * This class implements actions upon test case PASS/FAIL/SKIP status and customizes the Extent Report for REST API testing.
 */
public class ExtentReportManager implements ITestListener{
	
	public ExtentSparkReporter sparkReporter; //UI of the report
	public ExtentReports extent; // Configuration info on the report
	//public ExtentTest test; // creating entries in the report and updating the status of the test methods //entries for test
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>(); // For thread safety
	String reportName;	
	
	
	
	public void onStart(ITestContext testContext)// testContext- Method that got executed
	{		
		/*
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	    Date dt = new Date(); //java.util.Date; - to generate the date
	    String currentdatetimestamp = df.format(dt);// - pass the date object
	    */
		
		// Report configurations -The above steps in single line
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		reportName = "REST-API-Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+ "/reports/" + reportName); // specify location of the report
		
		sparkReporter.config().setDocumentTitle("Pet Store REST API Automation Report");
		sparkReporter.config().setReportName("REST API Testing");
		sparkReporter.config().setTheme(Theme.DARK);
		sparkReporter.config().setJs("window.chartsEnabled = true;");  // Enable charts
		
		
		// System information
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "Pet Store API"); 
		extent.setSystemInfo("Module", "User Module");
		extent.setSystemInfo("Environemnt", "QA");
		extent.setSystemInfo("Tester", System.getProperty("user.name")); //dynamic data
		
		// Dynamic info from XML parameters
		// Get and set dynamic system info from the XML parameters only once
		String os = testContext.getCurrentXmlTest().getParameter("os"); //Capture the parameters from XML file -dynamically
		if (os != null) extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		if (browser != null) extent.setSystemInfo("Browser", browser);
		
		
		//captures the groups in the xml and display in report - Including groups if any exist in the XML file
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups(); //Getting all the groups from the xml in list
		if(!includedGroups.isEmpty()) 
		{
			extent.setSystemInfo("Groups", includedGroups.toString());
		}	
	}
	
//	public void onTestStart(ITestResult result) {
//        // Create ExtentTest instance for the current test and set it in ThreadLocal
//        ExtentTest test = extent.createTest(result.getTestClass().getName());
//        extentTest.set(test);
//    }
	
    public void onTestStart(ITestResult result) 
    {
        // Hybrid approach: Include class name and method name in the test entry
        String testName = result.getTestClass().getName() + " :: " + result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
    }
	
	public void onTestSuccess(ITestResult result) 
	{
		ExtentTest test = extentTest.get();
		//test = extent.createTest(result.getTestClass().getName()); //Creating the new entry - getting the class name 
		test.assignCategory(result.getMethod().getGroups()); // to display test method and groups in report 
		test.log(Status.PASS, result.getMethod().getMethodName()+" passed successfully.");
	}
	
	public void onTestFailure(ITestResult result) 
	{
		ExtentTest test = extentTest.get();
		//test = extent.createTest(result.getTestClass().getName());//from the result we are getting the class from class getting the name
		test.assignCategory(result.getMethod().getGroups());// from the result getting the test methd - which category the test belongs to
		
		//Attaching the error message
		test.log(Status.FAIL, result.getMethod().getMethodName() + " failed.");
        test.log(Status.INFO, "Error: " + result.getThrowable().getMessage());
	}
	
    public void onTestSkipped(ITestResult result) 
    {
        ExtentTest test = extentTest.get();
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, result.getMethod().getMethodName() + " was skipped.");
        if (result.getThrowable() != null) 
        {
            test.log(Status.INFO, "Reason: " + result.getThrowable().getMessage());
        }
    }
	
	public void onFinish (ITestContext testContext) 
	{
		extent.flush(); // it will consolidate all the info and generate the report
		
		//Extra stmts - we need to open the report manually, Hence write the code as soon as script executed it opens the report
		
		String pathOfExtentReport = System.getProperty("user.dir")+ "/reports/"+reportName; //path of the report
		File extentReport = new File(pathOfExtentReport); //extent report file
		
		try {
		Desktop.getDesktop().browse(extentReport.toURI()); //Opens the browser automatically
		} catch (IOException e) { //if does not it will throw an exception
			e.printStackTrace();
		}
	}
	

}
