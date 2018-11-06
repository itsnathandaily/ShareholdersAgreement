package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

@InitiatingFlow
@StartableByRPC
public class CreateAgreementFlow extends FlowLogic<Void> {
    private final ProgressTracker progressTracker = new ProgressTracker();

    private final Party spv;
    private final Party shareholder1;
    private final Party shareholder2;
    private final Double equity1;
    private final Double equity2;
    private final int projectRevenue;
    private final int projectCost;

    public CreateAgreementFlow(Party spv, Party shareholder1, Party shareholder2, Double equity1, Double equity2, int projectRevenue, int projectCost) {
        this.spv = spv;
        this.shareholder1 = shareholder1;
        this.shareholder2 = shareholder2;
        this.equity1 = equity1;
        this.equity2 = equity2;
        this.projectRevenue = projectRevenue;
        this.projectCost = projectCost;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        TransactionBuilder txBuilder = new TransactionBuilder();
        txBuilder.setNotary(getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0));

        TokenState projectState = new TokenState(spv, shareholder1, shareholder2, equity1, equity2, projectRevenue, projectCost);
        txBuilder.addOutputState(projectState, TokenContract.ID);

        txBuilder.addCommand(new TokenContract.Commands.CreateAgreement(), spv.getOwningKey());

        SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

        subFlow(new FinalityFlow(signedTx));

        return null;
    }
}