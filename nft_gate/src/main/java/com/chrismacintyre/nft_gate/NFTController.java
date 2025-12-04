package com.chrismacintyre.nft_gate;

import io.vertx.core.impl.logging.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.web3j.crypto.Credentials;
import org.web3j.generated.contracts.AccessNFT;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Controller
public class NFTController {
/*
    private static final long chainId = 137;
    private static final Set<String> transferClaims = new HashSet<>();
    private static final String privateKey = System.getenv("PRIVATE_KEY");
    private static final String nodeUrl = System.getenv("WEB3J_NODE_URL");
    private static final Web3j web3j = Web3j.build(new HttpService(nodeUrl));
    private static final Credentials credentials = Credentials.create(privateKey);
    private static final RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);
    private static final AccessNFT accessNFT = AccessNFT.load(System.getenv("ACCESS_NFT_ADDR"), web3j, txManager, new StaticGasProvider(BigInteger.valueOf(30_100_000_000L), BigInteger.valueOf(6721975L)));
    private static final Logger log = (Logger) LoggerFactory.getLogger(NFTController.class);

*/
}
