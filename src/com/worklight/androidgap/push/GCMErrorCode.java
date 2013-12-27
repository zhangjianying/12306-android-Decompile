package com.worklight.androidgap.push;

public enum GCMErrorCode
{
  private final String description;

  static
  {
    ACCOUNT_MISSING = new GCMErrorCode("ACCOUNT_MISSING", 1, "Push Service: There is no Google account on the phone.");
    AUTHENTICATION_FAILED = new GCMErrorCode("AUTHENTICATION_FAILED", 2, "Push Service: Bad password.");
    TOO_MANY_REGISTRATIONS = new GCMErrorCode("TOO_MANY_REGISTRATIONS", 3, "Push Service: The user has too many applications registered.");
    INVALID_SENDER = new GCMErrorCode("INVALID_SENDER", 4, "Push Service: The sender account is not recognized.");
    PHONE_REGISTRATION_ERROR = new GCMErrorCode("PHONE_REGISTRATION_ERROR", 5, "Push Service: Incorrect phone registration with Google.");
    INVALID_PARAMETERS = new GCMErrorCode("INVALID_PARAMETERS", 6, "Push Service: Invalid parameter for email sender.");
    UNEXPECTED = new GCMErrorCode("UNEXPECTED", 7, "Push Service: Unexpected error from google GCM service");
    GCMErrorCode[] arrayOfGCMErrorCode = new GCMErrorCode[8];
    arrayOfGCMErrorCode[0] = SERVICE_NOT_AVAILABLE;
    arrayOfGCMErrorCode[1] = ACCOUNT_MISSING;
    arrayOfGCMErrorCode[2] = AUTHENTICATION_FAILED;
    arrayOfGCMErrorCode[3] = TOO_MANY_REGISTRATIONS;
    arrayOfGCMErrorCode[4] = INVALID_SENDER;
    arrayOfGCMErrorCode[5] = PHONE_REGISTRATION_ERROR;
    arrayOfGCMErrorCode[6] = INVALID_PARAMETERS;
    arrayOfGCMErrorCode[7] = UNEXPECTED;
    $VALUES = arrayOfGCMErrorCode;
  }

  private GCMErrorCode(String paramString)
  {
    this.description = paramString;
  }

  public String getDescription()
  {
    return this.description;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.push.GCMErrorCode
 * JD-Core Version:    0.6.0
 */