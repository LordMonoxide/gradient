package lordmonoxide.gradient.progress;

public enum Age {
  AGE1, AGE2, AGE3, AGE4;

  public static Age get(final int age) {
    if(age < 1 || age > Age.values().length) {
      throw new IndexOutOfBoundsException("Age must be between 1 and " + Age.values().length + " inclusive");
    }

    return Age.values()[age - 1];
  }
}
