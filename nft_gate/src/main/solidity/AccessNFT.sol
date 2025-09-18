// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.10;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import "@openzeppelin/contracts/utils/Strings.sol";
import "@openzeppelin/contracts/utils/Base64.sol";
import "@openzeppelin/contracts/utils/types/Time.sol";

abstract contract MintingTerms {
    function mintSet(uint256 batchSize) public virtual;
}

contract AccessNFT is ERC721, Ownable, ReentrancyGuard, MintingTerms {
    using Strings for uint256;
    using Time for *;

    uint256 public constant MAX_SUPPLY = 50;
    uint256 public totalMinted;
    uint256 public nextTokenId = 1;
    Time.Delay private _delay;

    string private _sharedTokenURI = "ipfs://bafybeiefmpvihzqfy2svp52l6akra7e6pqozxtf7mkztk3li5xoglcwlkm";
    mapping(address => uint256) public walletClaims;

    event MintingProgress(uint256 indexed totalMinted);
    event TokenClaimed(address indexed to, uint256 indexed tokenId);

    constructor() ERC721("AccessNFT", "accNFT") Ownable(msg.sender) {}

    function setExpiration(uint32 span) internal onlyOwner {
        _delay = Time.toDelay(span);
    }

    function tokenURI(uint256 tokenId) public view override returns (string memory) {
        return string.concat(_sharedTokenURI, "/", tokenId.toString());
    }

    function setSharedTokenURI(string calldata newURI) external onlyOwner {
        _sharedTokenURI = newURI;
    }

    function revokeTokens() internal onlyOwner {
        if(_delay.get() == 0){
            for(uint256 i=1; i < nextTokenId; i++) {
                _burn(i);
            }
        }
    }

    function mintSet(uint256 batchSize) public override onlyOwner {
        require(batchSize > 0, "Batch size must be > 0");
        require(totalMinted + batchSize <= MAX_SUPPLY, "Exceeds MAX_SUPPLY");

        uint256 startTokenId = nextTokenId;
        uint256 endTokenId = startTokenId + batchSize;

        for (uint256 tokenId = startTokenId; tokenId < endTokenId; tokenId++) {
            _safeMint(owner(), tokenId);
            tokenURI(tokenId);
        }

        nextTokenId = endTokenId;
        totalMinted += batchSize;

        emit MintingProgress(totalMinted);
    }

    function claimAccessNFT(address to, uint256 tokenId) external nonReentrant {
        require(to != address(0), "Invalid address");
        require(walletClaims[to] <= 1, "Per-wallet limit reached");
        require(ownerOf(tokenId) == owner(), "Token not available");
        require(tokenId < nextTokenId, "Cannot claim an unminted token");
        walletClaims[to]++;
        _safeTransfer(owner(), to, tokenId, "");

        emit TokenClaimed(to, tokenId);
    }

}
