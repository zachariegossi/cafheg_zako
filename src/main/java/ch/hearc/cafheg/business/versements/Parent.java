package ch.hearc.cafheg.business.versements;

public class Parent {

    private boolean activityLucrative;
    private boolean autoriteParentale;
    private String canton;
    private boolean independant;
    private Number salaire;


    public Parent(boolean activityLucrative, boolean autoriteParentale, String canton, boolean independant, Number salaire) {
        this.activityLucrative = activityLucrative;
        this.autoriteParentale = autoriteParentale;
        this.canton = canton;
        this.independant = independant;
        this.salaire = salaire;
    }

    public boolean isActivityLucrative() {
        return activityLucrative;
    }

    public void setActivityLucrative(boolean activityLucrative) {
        this.activityLucrative = activityLucrative;
    }

    public boolean isAutoriteParentale() {
        return autoriteParentale;
    }

    public void setAutoriteParentale(boolean autoriteParentale) {
        this.autoriteParentale = autoriteParentale;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public boolean isIndependant() {
        return independant;
    }

    public void setIndependant(boolean independant) {
        this.independant = independant;
    }

    public Number getSalaire() {
        return salaire;
    }

    public void setSalaire(Number salaire) {
        this.salaire = salaire;
    }
}
