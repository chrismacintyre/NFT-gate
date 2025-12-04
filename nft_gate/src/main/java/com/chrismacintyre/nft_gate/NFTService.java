package com.chrismacintyre.nft_gate;

import io.vertx.core.impl.ConcurrentHashSet;
import org.springframework.stereotype.Service;
import org.web3j.generated.contracts.AccessNFT;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class NFTService {
/*
    private static final long chainId = 137;
    private static final ConcurrentHashSet<String> transferClaims = new ConcurrentHashSet<>();
    private static final String privateKey = System.getenv("PRIVATE_KEY");
    private static final String nodeURL = System.getenv("WEB3J_NODE_URL");
    private static final Web3j web3j = Web3j.build(new HttpService(nodeURL));
    private static final Credentials credentials = Credentials.create(privateKey);
    private static final RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);
    private static final AccessNFT accessNFT = AccessNFT.load(System.getenv("ACCESS_NFT_ADDR"),
                                                              web3j,
                                                              txManager,
                                                              new StaticGasProvider(BigInteger.valueOf(30_100_000_000L),
                                                                      BigInteger.valueOf(6721975L)));

    public String getContractAddress() { return accessNFT.getContractAddress(); }

    public BigInteger getUserTokenBalance(String userAddress) throws Exception {
        return accessNFT.balanceOf(userAddress).send();
    }

    public boolean isOwner(String userAddress) throws Exception {
        return !getUserTokenBalance(userAddress).equals(BigInteger.ZERO);
    }

    public TransactionReceipt claimToken(String userAddress, BigInteger tokenId) throws Exception {
        return accessNFT.claimAccessNFT(userAddress, tokenId).send();
    }

    public void fundUserTransfers(String userAddress) throws Exception {
        if(!transferClaims.contains(userAddress)) {
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger baseFee = ethGasPrice.getGasPrice();
            BigInteger GAS_LIMIT = BigInteger.valueOf(21000);
            BigInteger PRIORITY_FEE = Convert.toWei("30", Convert.Unit.GWEI).toBigInteger();
            BigInteger maxFeePerGas = baseFee.add(PRIORITY_FEE);
            TransactionReceipt transactionReceipt = Transfer.sendFundsEIP1559(
                    web3j,
                    credentials,
                    userAddress,
                    new BigDecimal("0.5"),
                    Convert.Unit.ETHER,
                    GAS_LIMIT,
                    PRIORITY_FEE,
                    maxFeePerGas).send();
            System.out.println(transactionReceipt.getTransactionIndex());
            transferClaims.add(userAddress);
        }
    }

    public void transferToken(String userAddress, String recipientAddress, BigInteger tokenId) throws Exception {
        if (isOwner(userAddress)) {
            fundUserTransfers(userAddress);
            accessNFT.transferFrom(userAddress, recipientAddress, tokenId).send();
        }
    }
*/
}
