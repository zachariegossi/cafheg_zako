package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.Famille;
import ch.hearc.cafheg.business.versements.Parent;
import ch.hearc.cafheg.business.versements.VersementParentParMois;
import ch.hearc.cafheg.infrastructure.application.Application;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocationService {

  private static final String PARENT_1 = "Parent1";
  private static final String PARENT_2 = "Parent2";
  private static final String DRAW = "Draw";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;
  private final VersementMapper versementMapper;

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper, VersementMapper versementMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
    this.versementMapper = versementMapper;
    logger.info("starting AllocationService");
    logger.error("This is an error");
  }

  public List<Allocataire> findAllAllocataires(String likeNom) {
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  public String getParentDroitAllocation(Famille famille) {
    System.out.println("Déterminer le droit aux allocations");

    /*
    String eR = (String)parameters.getOrDefault("enfantResidence", "");
    Boolean p1AL = (Boolean)parameters.getOrDefault("parent1ActiviteLucrative", false);
    String p1Residence = (String)parameters.getOrDefault("parent1Residence", "");
    Boolean p2AL = (Boolean)parameters.getOrDefault("parent2ActiviteLucrative", false);
    String p2Residence = (String)parameters.getOrDefault("parent2Residence", "");
    Boolean pEnsemble = (Boolean)parameters.getOrDefault("parentsEnsemble", false);
    Number salaireP1 = (Number) parameters.getOrDefault("parent1Salaire", BigDecimal.ZERO);
    Number salaireP2 = (Number) parameters.getOrDefault("parent2Salaire", BigDecimal.ZERO);
    */

    Parent parent1 = famille.getParent1();
    Parent parent2 = famille.getParent2();
    //Enfant enfant = famille.getEnfant();

    // Both parent exists
    if (parent1 == null) {
      return PARENT_2;
    }
    if (parent2 == null) {
      return PARENT_1;
    }

    // Lucrative activity
    if (!parent1.isActivityLucrative() && parent2.isActivityLucrative()) {
      return PARENT_2;
    }
    if (parent1.isActivityLucrative() && !parent2.isActivityLucrative()) {
      return PARENT_1;
    }

    // Parental Authority
    if (!parent1.isAutoriteParentale() && parent2.isAutoriteParentale()) {
      return PARENT_2;
    }
    if (parent1.isAutoriteParentale() && !parent2.isAutoriteParentale()) {
      return PARENT_1;
    }

    // Living with children
    if (parent1.islivingWithChild() && parent2.islivingWithChild()) {

      // Both Parent employed or Both Independant
      if (!parent1.isIndependant() && !parent2.isIndependant() || parent1.isIndependant() && parent2
          .isIndependant()) {
        if (parent1.getSalaire().doubleValue() > parent2.getSalaire().doubleValue()) {
          return PARENT_1;
        }
        if (parent1.getSalaire().doubleValue() < parent2.getSalaire().doubleValue()) {
          return PARENT_2;
        }
        //One employed and one independant.
      } else {
        if (parent1.isIndependant()) {
          return PARENT_2;
        } else {
          return PARENT_1;
        }
      }
    } else {
      if (parent1.islivingWithChild()) {
        return PARENT_1;
      }
      if (parent2.islivingWithChild()) {
        return PARENT_2;
      }
    }

    return DRAW;

  }

  public String removeAllocataire(long id) {

    List<VersementParentParMois> versementsParentParMois = versementMapper
        .findVersementParentEnfantParMois();

    VersementParentParMois versement = versementsParentParMois.stream()
        .filter(e -> e.getParentId() == id)
        .findFirst()
        .orElse(null);

    if (versement == null) {
      //Aucun versement trouvé pour ce parent
      int nbRowDeleted = allocataireMapper.removeAllocataireByID(id);
      if (nbRowDeleted > 0) {
        return nbRowDeleted + " row(s) deleted";
      } else {
        return "Nothing removed";
      }
    } else {
      return "Impossible de supprimé l'allocataire, il a deja fait des versements.";
    }

    //Allocataire allocataire = allocataireMapper.findById(id);
    //return "done";
  }

  public String updateAllocataire(String idAllocataire, String nomAllocataire,
      String prenomAllocataire) {
    long id = Long.parseLong(idAllocataire);
    Allocataire allocataire = allocataireMapper.findById(id);
    if (allocataire != null) {
      if (allocataire.getNom().equals(nomAllocataire) && allocataire.getPrenom()
          .equals(prenomAllocataire)) {
        return "Allocataire Correct";
      } else {
        int nbRowUpdate = allocataireMapper
            .updateAllocataireNomPrenom(id, nomAllocataire, prenomAllocataire);
        if (nbRowUpdate > 0) {
          return nbRowUpdate + " Allocataire(s) Updated";
        } else {
          return "Unable to update Allocataire.";
        }
      }
    } else {
      return "Allocataire Unfound";
    }
  }
}
