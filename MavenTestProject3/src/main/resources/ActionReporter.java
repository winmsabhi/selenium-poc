

package utilities;

public class ActionReporter {
private String actionkeyword = "";
private String pageObject = "";
private String actionData = "";
private String[] currentTestDetails;
private boolean testPass;
private String internal = " ";
private String errorScreenShotPath = "";
private String ScreenShotPath="";
private String PNR="";
private String lastName="";
public String getInternal() {
return internal;
}

public void setInternal(String internal) {
this.internal = internal;
}

public void setActionDetails() {
}

public String getActionkeyword() {
return actionkeyword;
}

public void setActionkeyword(String actionkeyword) {
this.actionkeyword = actionkeyword;
}

public String getPageObject() {
return pageObject;
}

public void setPageObject(String pageObject) {
this.pageObject = pageObject;
}

public String getActionData() {
return actionData;
}

public void setActionData(String actionData) {
this.actionData = actionData;
}

public String[] getCurrentTestDetails() {
return currentTestDetails;
}
public String getCurrentTestDetailsAsString() {
return String.join(", ", currentTestDetails);
}

public void setCurrentTestDetails(String[] currentTestDetails) {
this.currentTestDetails = currentTestDetails;
}

public boolean isTestPass() {
return testPass;
}

public void setTestPass(boolean currentTestResult) {
this.testPass = currentTestResult;
}

public String getErrorScreenShotPath() {
return errorScreenShotPath;
}

public void setErrorScreenShotPath(String errorScreenShotPath) {
this.errorScreenShotPath = errorScreenShotPath;
}

public String getScreenShotPath() {
return ScreenShotPath;
}

public void setScreenShotPath(String screenShotPath) {
ScreenShotPath = screenShotPath;
}

public String getPNR() {
return PNR;
}

public void setPNR(String pNR) {
PNR = pNR;
}

public String getLastName() {
return lastName;
}

public void setLastName(String lastName) {
this.lastName = lastName;
}
}


