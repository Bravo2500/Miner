package freelance;

import java.io.IOException;
import java.net.UnknownHostException;

public class GetWork {
	private Comm com;
	private String extraNounce1, extraNounce2;
	private String coinBase1, coinBase2;
	private String preHash, id, version, nBits, nTime;
	private String[] merkelBranch;
	private int en2Size;
	private int difficulty;
	private String miner;
	private String password;
	
	public GetWork(String address, int port, String miner, String password) {
		this.miner = miner;
		this.password = password;
		try {
			com = new Comm(address, port, this);
			com.write("{\"params\": [], \"id\": \"s\", \"method\": \"mining.subscribe\"}\n");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void init(String extraNounce1, int en2Size) {
		this.extraNounce1 = extraNounce1;
		this.en2Size = en2Size;
		com.write("{\"params\": [\""+miner+"\", \""+password+"\"], \"id\": 2, \"method\": \"mining.authorize\"}\n");
	}

	public void setDiff(int diff) {
		difficulty = diff;
	}

	public void newJob(String id, String preHash, String[] merkelBranch,
			String coinBase1, String coinBase2, String version, String nBits,
			String nTime) {
		this.id = id;
		this.preHash = preHash;
		this.merkelBranch = merkelBranch;
		this.version = version;
		this.coinBase1 = coinBase1;
		this.coinBase2 = coinBase2;
		this.nBits = nBits;
		this.nTime = nTime;
	}
}
