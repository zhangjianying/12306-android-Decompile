package com.tl.uic.model;

public enum StorageType
{
  private int value;

  static
  {
    PHONE_TOTAL = new StorageType("PHONE_TOTAL", 2, 2);
    SD_CARD_FREE = new StorageType("SD_CARD_FREE", 3, 3);
    SD_CARD_USED = new StorageType("SD_CARD_USED", 4, 4);
    SD_CARD_TOTAL = new StorageType("SD_CARD_TOTAL", 5, 5);
    StorageType[] arrayOfStorageType = new StorageType[6];
    arrayOfStorageType[0] = PHONE_FREE;
    arrayOfStorageType[1] = PHONE_USED;
    arrayOfStorageType[2] = PHONE_TOTAL;
    arrayOfStorageType[3] = SD_CARD_FREE;
    arrayOfStorageType[4] = SD_CARD_USED;
    arrayOfStorageType[5] = SD_CARD_TOTAL;
    ENUM$VALUES = arrayOfStorageType;
  }

  private StorageType(int arg3)
  {
    int j;
    this.value = j;
  }

  public int getValue()
  {
    return this.value;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.StorageType
 * JD-Core Version:    0.6.0
 */