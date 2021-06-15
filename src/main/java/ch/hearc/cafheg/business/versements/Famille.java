package ch.hearc.cafheg.business.versements;

public class Famille {

  private final Parent parent1;
  private final Parent parent2;
  private final Enfant enfant;

  public Famille(Parent parent1, Parent parent2, Enfant enfant) {
    this.parent1 = parent1;
    this.parent2 = parent2;
    this.enfant = enfant;
  }

  public Parent getParent1() {
    return parent1;
  }

  public Parent getParent2() {
    return parent2;
  }

  // Not used
  public Enfant getEnfant() {
    return enfant;
  }
}
