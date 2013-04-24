package freelance;

public class Miner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GetWork("stratum.btcguild.com",3333,"FreeLance_1","123");
		System.out.println("comm up");
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
