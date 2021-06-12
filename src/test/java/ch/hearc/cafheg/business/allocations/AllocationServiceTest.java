package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.business.versements.Enfant;
import ch.hearc.cafheg.business.versements.Famille;
import ch.hearc.cafheg.business.versements.Parent;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

    private AllocationService allocationService;

    private AllocataireMapper allocataireMapper;
    private AllocationMapper allocationMapper;
    private VersementMapper versementMapper;
    Map<String, Object> parameters;


    @BeforeEach
    void setUp() {
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        allocationMapper = Mockito.mock(AllocationMapper.class);
        versementMapper = Mockito.mock(VersementMapper.class);

        allocationService = new AllocationService(allocataireMapper, allocationMapper, versementMapper);

        parameters = new HashMap<String, Object>();
    }

    @Test
    void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
        Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
        List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
        assertThat(all).isEmpty();
    }

    @Test
    void findAllAllocataires_Given2Geiser_ShouldBe2() {
        Mockito.when(allocataireMapper.findAll("Geiser"))
                .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
                        new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
        List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
        assertAll(() -> assertThat(all.size()).isEqualTo(2),
                () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
                () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
                () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
                () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
                () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
                () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
    }

    @Test
    void findAllocationsActuelles() {
        Mockito.when(allocationMapper.findAll())
                .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
                        LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
                        LocalDate.now(), null)));
        List<Allocation> all = allocationService.findAllocationsActuelles();
        assertAll(() -> assertThat(all.size()).isEqualTo(2),
                () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
                () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
                () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
                () -> assertThat(all.get(0).getFin()).isNull(),
                () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
                () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
                () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
                () -> assertThat(all.get(1).getFin()).isNull());
    }

    @Test
    void getParentDroitAllocation_Given_Parent1DoesNotExists_ShouldBe_Parent2() {
        Parent parent1 = null;
        Parent parent2 = new Parent(false, true, "Bienne", true, true, 25000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        parameters.put("parent1ActiviteLucrative", true);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent1"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent2DoesNotExists_ShouldBe_Parent1() {
        Parent parent2 = null;
        Parent parent1 = new Parent(false, true, "Bienne", true, true, 25000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        parameters.put("parent1ActiviteLucrative", true);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent1"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent1WitLucrativeActivity_ShouldBe_Parent1() {
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 25000);
        Parent parent2 = new Parent(false, true, "Bienne", true, true, 25000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        parameters.put("parent1ActiviteLucrative", true);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent1"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent2WitLucrativeActivity_ShouldBe_Parent2() {
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 25000);
        Parent parent1 = new Parent(false, true, "Bienne", true, true, 25000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        parameters.put("parent1ActiviteLucrative", true);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent2"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent2_ParentalAuthority_ShouldBe_Parent2() {
        Parent parent1 = new Parent(true, false, "Bienne", false, true, 25000);
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 25000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent2"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent1_ParentalAuthority_ShouldBe_Parent1() {
        Parent parent2 = new Parent(true, false, "Bienne", false, true, 25000);
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 25000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent1"));
    }

    @Test
    void getParentDroitAllocation_Given_BothIndependant_Parent1_BiggerSalary_ShouldBe_Parent1() {
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 250000);
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent1"));
    }

    @Test
    void getParentDroitAllocation_Given_BothIndependant_Parent2_BiggerSalary_ShouldBe_Parent2() {
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 250000);
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent2"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent1Independant_ShouldBe_Parent2() {
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 250000);
        Parent parent2 = new Parent(true, true, "Bienne", true, false, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent2"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent2Independant_ShouldBe_Parent1() {
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 250000);
        Parent parent1 = new Parent(true, true, "Bienne", true, false, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent2"));
    }
    @Test
    void getParentDroitAllocation_Given_Parent1LivingWithChild_ShouldBe_Parent1() {
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 250000);
        Parent parent2 = new Parent(true, true, "Bienne", false, false, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent1"));
    }

    @Test
    void getParentDroitAllocation_Given_Parent2LivingWithChild_ShouldBe_Parent2() {
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 250000);
        Parent parent1 = new Parent(true, true, "Bienne", false, false, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("parent2"));
    }

    @Test
    void getParentDroitAllocation_Given_ParentSimilar_ShouldBe_Draw() {
        Parent parent2 = new Parent(true, true, "Bienne", true, true, 75000);
        Parent parent1 = new Parent(true, true, "Bienne", true, true, 75000);
        Enfant enfant = new Enfant(new NoAVS("172.000.000.001"), "Kelso", "Bob");
        Famille famille = new Famille(parent1, parent2, enfant);
        String result = allocationService.getParentDroitAllocation(famille);
        assertThat(result.equals("Draw"));
    }


    //INSERT INTO ALLOCATAIRES VALUES(14,'756.6457.6513.65','Kacy','Stead');
    @Test
    void updateAllocataire_Given_14_Kacy_Steed_ShouldBe_Allocataire_Updated() {
        String id = "14";
        String nom = "Kacy";
        String prenom = "Steed";
        String result = allocationService.updateAllocataire(id, nom,prenom);
        assertThat(result.equals("Allocataire Updated"));
    }

    //INSERT INTO ALLOCATAIRES VALUES(14,'756.6457.6513.65','Kacy','Stead');
    @Test
    void updateAllocataire_Given_25_Kacy_Steed_ShouldBe_Allocataire_Unfound() {
        String id = "25";
        String nom = "Kacy";
        String prenom = "Steed";
        String result = allocationService.updateAllocataire(id, nom,prenom);
        assertThat(result.equals("Allocataire Unfound"));
    }

    //INSERT INTO ALLOCATAIRES VALUES(14,'756.6457.6513.65','Kacy','Stead');
    @Test
    void updateAllocataire_Given_14_Kacy_Stead_ShouldBe_Allocataire_Correct() {
        String id = "25";
        String nom = "Kacy";
        String prenom = "Steed";
        String result = allocationService.updateAllocataire(id, nom,prenom);
        assertThat(result.equals("Allocataire Correct"));
    }


}