package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;
  Map<String, Object> parameters;


  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);

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
  void getParentDroitAllocation_Given_1ParentWitLucrativeActivity_ShouldBe_Parent1() {
    parameters.put("parent1ActiviteLucrative", true);
    String result = allocationService.getParentDroitAllocation(parameters);
    assertThat(result.equals("parent1"));
  }

  @Test
  void getParentDroitAllocation_Given_2ndParent_LivingWithChildren_ShouldBe_Parent2() {
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("enfantResidence", "Sierre");
    parameters.put("parent1Residence", "Sion");
    parameters.put("parent2Residence", "Sierre");
    String result = allocationService.getParentDroitAllocation(parameters);
    assertThat(result.equals("parent2"));
  }

  @Test
  void getParentDroitAllocation_Given_1stParent_BiggerSalary_ShouldBe_Parent1() {
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("enfantResidence", "Sierre");
    parameters.put("parent1Residence", "Sierre");
    parameters.put("parent2Residence", "Sierre");
    parameters.put("parent1Salaire", 250000);
    parameters.put("parent2Salaire", 70000);
    String result = allocationService.getParentDroitAllocation(parameters);
    assertThat(result.equals("parent1"));
  }

  @Test
  void getParentDroitAllocation_Given_1stParent_BiggerSalary_ButParent2LivesWithChildren_ShouldBe_Parent1() {
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("enfantResidence", "Sierre");
    parameters.put("parent1Residence", "Sion");
    parameters.put("parent2Residence", "Sierre");
    parameters.put("parent1Salaire", 250000);
    parameters.put("parent2Salaire", 70000);
    String result = allocationService.getParentDroitAllocation(parameters);
    assertThat(result.equals("parent2"));
  }

  @Test
  void getParentDroitAllocation_Given_1stParent_BiggerSalary_AndParent1LivesWithChildren_ShouldBe_Parent1() {
    parameters.put("parent1ActiviteLucrative", true);
    parameters.put("parent2ActiviteLucrative", true);
    parameters.put("enfantResidence", "Sierre");
    parameters.put("parent1Residence", "Sion");
    parameters.put("parent2Residence", "Sierre");
    parameters.put("parent1Salaire", 250000);
    parameters.put("parent2Salaire", 70000);
    String result = allocationService.getParentDroitAllocation(parameters);
    assertThat(result.equals("parent2"));
  }


}