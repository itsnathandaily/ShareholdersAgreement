package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.TransactionState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.StartedMockNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FlowTests {
    private MockNetwork network;
    private StartedMockNode nodeA;
    private StartedMockNode nodeB;
    private StartedMockNode nodeC;

    @Before
    public void setup() {
        network = new MockNetwork(ImmutableList.of("java_bootcamp"));
        nodeA = network.createPartyNode(null);
        nodeB = network.createPartyNode(null);
        nodeC = network.createPartyNode(null);

        ImmutableList.of(nodeA, nodeB, nodeC).forEach(it -> {
            it.registerInitiatedFlow(RenegotiateEquityFlowResponder.class);
        });

        network.runNetwork();
    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

    @Test
    public void createProjectTest() throws Exception {
        CreateAgreementFlow flow = new CreateAgreementFlow(nodeA.getInfo().getLegalIdentities().get(0),
                nodeB.getInfo().getLegalIdentities().get(0),
                nodeC.getInfo().getLegalIdentities().get(0),
                50d, 50d, 100, 95);

        nodeA.startFlow(flow).get();

        RenegotiateEquityFlow flow2 = new RenegotiateEquityFlow(75d);

        nodeB.startFlow(flow2).get();
    }
}