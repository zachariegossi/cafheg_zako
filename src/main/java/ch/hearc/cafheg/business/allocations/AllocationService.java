package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.Famille;
import ch.hearc.cafheg.business.versements.Parent;
import ch.hearc.cafheg.business.versements.VersementAllocation;
import ch.hearc.cafheg.business.versements.VersementParentParMois;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import com.google.common.collect.Iterables;

import java.util.List;

public class AllocationService {

    private static final String PARENT_1 = "Parent1";
    private static final String PARENT_2 = "Parent2";
    private static final String DRAW = "Draw";

    private final AllocataireMapper allocataireMapper;
    private final AllocationMapper allocationMapper;
    private final VersementMapper versementMapper;

    public AllocationService(
            AllocataireMapper allocataireMapper,
            AllocationMapper allocationMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.allocationMapper = allocationMapper;
        this.versementMapper = versementMapper;
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


        return DRAW;

    }

    public String removeAllocataire(long id) {

        List<VersementParentParMois> versementsParentParMois = versementMapper.findVersementParentEnfantParMois();

        VersementParentParMois versement = versementsParentParMois.stream().filter(e -> e.getParentId()==id)
                .findFirst()
                .orElse(null);

        if(versement==null){
            //Aucun versement trouvé pour ce parent
            int nbRowDeleted = allocataireMapper.removeAllocataireByID(id);
            if( nbRowDeleted > 0){
                return nbRowDeleted+" row(s) deleted";
            } else {
                return "Nothing removed";
            }
        }else{
            return "Impossible de supprimé l'allocataire, il a deja fait des versements.";
        }

        //Allocataire allocataire = allocataireMapper.findById(id);
        //return "done";
    }

    public String updateAllocataire(String idAllocataire, String nomAllocataire, String prenomAllocataire) {
        long id = Long.parseLong(idAllocataire);
        Allocataire allocataire = allocataireMapper.findById(id);
        if(allocataire!=null){
            if(allocataire.getNom().equals(nomAllocataire) && allocataire.getPrenom().equals(prenomAllocataire)){
                return "Allocataire Correct";
            } else {
                int nbRowUpdate = allocataireMapper.updateAllocataireNomPrenom(id, nomAllocataire, prenomAllocataire);
                if(nbRowUpdate > 0){
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
