import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.business.allocations.NoAVS;
import ch.hearc.cafheg.business.versements.Enfant;
import ch.hearc.cafheg.business.versements.Famille;
import ch.hearc.cafheg.business.versements.Parent;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class AllocationsServiceTestBDD {

    private AllocationService allocationService;

    private AllocataireMapper allocataireMapper;
    private AllocationMapper allocationMapper;
    private VersementMapper versementMapper;
    private Parent parent1;
    private Parent parent2;
    private Enfant enfant;
    String result;

    @Before
    public void initMapper() {
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        allocationMapper = Mockito.mock(AllocationMapper.class);
        versementMapper = Mockito.mock(VersementMapper.class);

        //Parent parent = Mockito.mock(Parent.class);
        //Enfant enfait = Mockito.mock(Enfant.class);
        //famille = Mockito.mock(Famille.class);
    }

    @Given("Create AllocationService")
    public void createAllocationService() {
        allocationService = new AllocationService(allocataireMapper, allocationMapper, versementMapper);
        parent1 = new Parent(false,false,"",false,false,0);
        parent2 = new Parent(false,false,"",false,false,0);
        enfant = new Enfant(new NoAVS("000.0000.000.0000"),"Kelso","Bob");
    }
    @When("I give 1st parent with lucrative activity")
    public void iGiveStParentWithLucrativeActivity() {
        parent1 = new Parent(true,false,"",false,false,0);
        result = allocationService.getParentDroitAllocation(new Famille(parent1, parent2, enfant));
    }
    @Then("The result of One parent with lucrative activity is 1st parent")
    public void theResultOfOneParentWithLucrativeActivityIsStParent() {
        Assertions.assertEquals(result, "Parent1");
    }

    /* Abandoné car plus évalué
    @And("I give 2nd parent with lucrative activity")
    public void iGive2ndParentWithLucrativeActivity() {
        parent2 = new Parent(true,false,"",false,false,0);
        result = allocationService.getParentDroitAllocation(new Famille(parent1, parent2, enfant));
    }

    @And("the child lives with the {int}st parent")
    public void theChildLivesWithTheStParent(int numberParent) {
        parameters.put("enfantResidence", "Sierre");
        parameters.put("parent1Residence", "Sion");
        parameters.put("parent2Residence", "Sion");
        parameters.put("parent"+numberParent+"Residence", "Sierre");
        result = allocationService.getParentDroitAllocation(parameters);
    }

    @Then("The result of Two parents with lucrative activity divorced and the child live with the first one is {int}st parent")
    public void theResultOfTwoParentsWithLucrativeActivityDivorcedAndTheChildLiveWithTheFirstOneIsStParent(int parentNumber) {
        Assertions.assertEquals(result, "Parent"+parentNumber);
    }

     */
}
