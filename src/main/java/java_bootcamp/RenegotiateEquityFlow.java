package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.List;

@InitiatingFlow
@StartableByRPC
public class RenegotiateEquityFlow extends FlowLogic<SignedTransaction> {
    private final ProgressTracker progressTracker = new ProgressTracker();

    private final Double newEquity;

    public RenegotiateEquityFlow(Double newEquity) {
        this.newEquity = newEquity;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        StateAndRef<TokenState> input = getServiceHub().getVaultService().queryBy(TokenState.class).getStates().get(0);
        TokenState inputState = input.getState().getData();

        TransactionBuilder txBuilder = new TransactionBuilder();
        txBuilder.setNotary(getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0));

        txBuilder.addInputState(input);

        TokenState outputState = new TokenState(inputState.getSpv(), inputState.getShareholder1(),
                inputState.getShareholder2(), newEquity, 100 - newEquity,
                inputState.getProjectRevenue(), inputState.getProjectCost());
        txBuilder.addOutputState(outputState, TokenContract.ID);

        txBuilder.addCommand(new TokenContract.Commands.CreateAgreement(),
                inputState.getShareholder1().getOwningKey(), inputState.getShareholder2().getOwningKey());

        SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

        List<FlowSession> counterpartySessions = ImmutableList.of(initiateFlow(inputState.getShareholder2()));
        SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(signedTx, counterpartySessions));

        subFlow(new FinalityFlow(fullySignedTx));

        return null;
    }
}