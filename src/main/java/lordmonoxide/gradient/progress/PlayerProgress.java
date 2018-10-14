package lordmonoxide.gradient.progress;

public class PlayerProgress {
  private Age age = Age.AGE1;

  public Age getAge() {
    return this.age;
  }

  public void setAge(final Age age) {
    this.age = age;
  }

  public boolean meetsAgeRequirement(final Age age) {
    return this.age.ordinal() >= age.ordinal();
  }

  void cloneFrom(final PlayerProgress other) {
    this.age = other.age;
  }
}
