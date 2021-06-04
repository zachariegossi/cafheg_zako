import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class AllocationsServiceTestBDD {

    private AllocationService allocationService;

    private AllocataireMapper allocataireMapper;
    private AllocationMapper allocationMapper;
    Map<String, Object> parameters;
    String result;

    @Before
    public void initMapper() {
        allocataireMapper = Mockito.mock(AllocataireMapper.class);
        allocationMapper = Mockito.mock(AllocationMapper.class);
    }

    @Given("Create AllocationService")
    public void createAllocationService() {
        allocationService = new AllocationService(allocataireMapper, allocationMapper);
        parameters = new HashMap<String, Object>();
    }

    @When("I give {int}st parent with lucrative activity")
    public void iGiveStParentWithLucrativeActivity(int parentNumber) {
        parameters.put("parent"+parentNumber+"ActiviteLucrative", true);
        result = allocationService.getParentDroitAllocation(parameters);
    }



    @Then("The result of One parent with lucrative activity is {int}st parent")
    public void theResultOfOneParentWithLucrativeActivityIsStParent(int parentNumber) {
        Assertions.assertEquals(result, "Parent"+parentNumber);
    }

    @And("I give {int}nd parent with lucrative activity")
    public void iGiveNdParentWithLucrativeActivity(int parentNumber) {
        parameters.put("parent"+parentNumber+"ActiviteLucrative", true);
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
}
