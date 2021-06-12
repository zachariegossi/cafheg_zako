package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.Enfant;
import ch.hearc.cafheg.business.versements.Famille;
import ch.hearc.cafheg.business.versements.Parent;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AllocationService {

    private static final String PARENT_1 = "Parent1";
    private static final String PARENT_2 = "Parent2";
    private static final String DRAW = "Draw";

    private final AllocataireMapper allocataireMapper;
    private final AllocationMapper allocationMapper;

    public AllocationService(
            AllocataireMapper allocataireMapper,
            AllocationMapper allocationMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocationMapper = allocationMapper;
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
        Enfant enfant = famille.getEnfant();
        if (parent1 == null) {
            return PARENT_2;
        }
        if (parent2 == null) {
            return PARENT_1;
        }

        if (parent1.isActivityLucrative() && parent2.isActivityLucrative()) {
            if (parent1.isAutoriteParentale() && parent2.isAutoriteParentale()) {

            } else {
                if (!parent1.isAutoriteParentale()) {
                    return PARENT_2;
                }
                if (!parent2.isAutoriteParentale()) {
                    return PARENT_1;
                }
            }
        } else if (!parent1.isActivityLucrative() && parent2.isActivityLucrative()) {
            return PARENT_2;
        } else if (parent1.isActivityLucrative() && !parent2.isActivityLucrative()) {
            return PARENT_1;
        } else {
            return DRAW;
        }

    }
}
}
