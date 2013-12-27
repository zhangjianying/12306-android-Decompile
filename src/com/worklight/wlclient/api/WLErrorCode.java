package com.worklight.wlclient.api;

public enum WLErrorCode
{
  private final String description;

  static
  {
    REQUEST_TIMEOUT = new WLErrorCode("REQUEST_TIMEOUT", 1, "Request timed out.");
    REQUEST_SERVICE_NOT_FOUND = new WLErrorCode("REQUEST_SERVICE_NOT_FOUND", 2, "Service not found");
    UNRESPONSIVE_HOST = new WLErrorCode("UNRESPONSIVE_HOST", 3, "The service is currently not available.");
    PROCEDURE_ERROR = new WLErrorCode("PROCEDURE_ERROR", 4, "Procedure invocation errorCode.");
    APP_VERSION_ACCESS_DENIAL = new WLErrorCode("APP_VERSION_ACCESS_DENIAL", 5, "Application version denied.");
    APP_VERSION_ACCESS_NOTIFY = new WLErrorCode("APP_VERSION_ACCESS_NOTIFY", 6, "Notify application version changed.");
    WLErrorCode[] arrayOfWLErrorCode = new WLErrorCode[7];
    arrayOfWLErrorCode[0] = UNEXPECTED_ERROR;
    arrayOfWLErrorCode[1] = REQUEST_TIMEOUT;
    arrayOfWLErrorCode[2] = REQUEST_SERVICE_NOT_FOUND;
    arrayOfWLErrorCode[3] = UNRESPONSIVE_HOST;
    arrayOfWLErrorCode[4] = PROCEDURE_ERROR;
    arrayOfWLErrorCode[5] = APP_VERSION_ACCESS_DENIAL;
    arrayOfWLErrorCode[6] = APP_VERSION_ACCESS_NOTIFY;
    $VALUES = arrayOfWLErrorCode;
  }

  private WLErrorCode(String paramString)
  {
    this.description = paramString;
  }

  public String getDescription()
  {
    return this.description;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLErrorCode
 * JD-Core Version:    0.6.0
 */