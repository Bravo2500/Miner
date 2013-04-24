package freelance;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comm implements Runnable {
	private Socket connection;
	private BufferedReader in;
	private GetWork worker;
	private OutputStreamWriter osw;
	private BufferedOutputStream bos;
	private Thread t;

	public Comm(String address, int port, GetWork worker)
			throws UnknownHostException, IOException {
		connection = new Socket(address, port);
		in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		bos = new BufferedOutputStream(connection.getOutputStream());
		osw = new OutputStreamWriter(bos, "US-ASCII");
		this.worker = worker;
		t = new Thread(this, "Comm Thread");
		t.start();
	}

	public void write(String data) {
		try {
			osw.write(data);
			osw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				try {
					if (in.ready()) {

						String output = in.readLine();
						System.out.println(output);
						System.out.flush();
						JSONObject parse = new JSONObject(output);
						if (!parse.isNull("result")) {
							try{
								parse.getBoolean("result");
							}catch(JSONException e){
								JSONArray rlt = parse.getJSONArray("result");
								worker.init(rlt.getString(1), rlt.getInt(2));
							}
						}
						if (!parse.isNull("method")) {
							String method = parse.getString("method");
							if (method.equals("mining.set_difficulty")) {
								JSONArray rlt = parse.getJSONArray("params");
								worker.setDiff(rlt.getInt(0));
							}
							if (method.equals("mining.notify")) {
								JSONArray rlt = parse.getJSONArray("params");
								JSONArray mk = rlt.getJSONArray(4);
								String[] merk = new String[mk.length()];
								for(int i=0;i<mk.length();i++){
									merk[i] = mk.getString(i);
								}
								
								worker.newJob(rlt.getString(0),
										rlt.getString(1),merk,
										rlt.getString(2), rlt.getString(3),
										rlt.getString(5), rlt.getString(6),
										rlt.getString(7));
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				try {
					in.close();
					osw.close();
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}
}
