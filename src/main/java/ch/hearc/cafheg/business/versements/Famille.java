package ch.hearc.cafheg.business.versements;

public class Famille {

  private Parent parent1;
  private Parent parent2;
  private Enfant enfant;

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

  public Enfant getEnfant() {
    return enfant;
  }
}
