package network;

import math.Matrix;

public class NetworkControler {

	private Network[] networks;
	
	private int topNet = -1;
	
	private boolean rand = true;

	public NetworkControler(int size) {

		networks = new Network[size];

		for (int i = 0; i < size; i++) {

			networks[i] = new Network(1.0);

		}

	}

	public void generation(int testAmount) {
		
		topNet = -1;
		
		int[] wins = new int[networks.length], winDist = new int[testAmount * 2 + 1];

		int topCount = testAmount * 2;

		for (int i = 0; i < networks.length; i++) {

			wins[i] = testRand(i, testAmount);
			if(wins[i]<0)continue;
			winDist[wins[i]]++;

		}

		int counter = 0;

		for (; counter < networks.length / 3; topCount--) {
			counter += winDist[topCount];
		}
		
		if(topCount < 0)topCount = 0;
		
		winDist[topCount] -= counter - networks.length / 3;

		Network[] top = new Network[networks.length / 3];
		
		counter = 0;

		for (int j = 0; counter < top.length; j++) {

			if (wins[j] > topCount) {
				top[counter] = new Network(networks[j]);
				counter++;
			}else if(wins[j] == topCount && winDist[topCount] > 0){
				top[counter] = new Network(networks[j]);
				counter++;
				winDist[topCount]--;
			}

		}

		int i = 0;

		for (; i < networks.length / 3; i++) {
			networks[i] = top[i];

			//networks[i + 2 * networks.length / 3] = new Network(top[i], top[(int) (Math.random() * top.length)]);
			networks[i + networks.length / 3] = new Network(top[i], top[(int) (Math.random() * top.length)]);

			networks[i + 2*networks.length / 3] = top[i].mutate();

			//networks[i + networks.length / 3] = top[i].mutate();

		}

	}

	public Network getTop() {

		if(topNet == -1)calcTop();
		
		return networks[topNet];

	}
	
	private void calcTop(){
		
		int iTop = 0, outTop = 0;
		
		for (int i = 0; i < networks.length; i++) {
			
			int j = testRand(i, 500);
			
			if(j > outTop){
				outTop = j;
				iTop = i;
			}

		}
		
		topNet = iTop;
		System.out.println(outTop);
		
	}

	private int testRand(int netIndex, int num) {
		
		Network net = networks[netIndex];
		
		//if(netIndex > networks.length*2/3)System.out.print("over" + " ");
		
		//System.out.println(net.test(Matrix.rowMatrix(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5})).getAttribute(0, 0));
		
		int wins = num;

		for (int i = 0; i < num; i++) {
			
			
			double[] board = new double[9];

			int moves = 0;

			if (Math.random()<0.5) moves++;

			while (numMoves(board) > 0 && gamestate(board) == 0) {

				if (moves % 2 != 0) {
					
					if (rand) {
						int choice = (int) (numMoves(board) * Math.random());

						int counter = 0;

						for (int j = 0; j < board.length; j++) {

							if (board[j] != 0) {
								continue;
							}

							if (choice == counter) {
								board[j] = -1;
								break;
							}

							counter++;

						}
					}else{
						
						double[] output = networks[(int)(Math.random()*networks.length)].test(Matrix.rowMatrix(board)).getSingleArray();

						double jTop = -1, outTop = -1000;

						for (int j = 0; j < board.length; j++) {
							
							if (board[j] != 0) continue;
							
							if (output[j] > outTop) {
								outTop = output[j];
								jTop = j;
							}

						}
						
						try{
							board[(int) jTop] = -1;
						}catch(Exception e){
							for(double d:output){
								System.out.println(d);
							}
						}
						
					}

				} else {

					double[] output = net.test(Matrix.rowMatrix(splitBoard(board))).getSingleArray();

					double jTop = -1, outTop = Integer.MIN_VALUE;
					for (int j = 0; j < board.length; j++) {

						if (board[j] != 0) continue;
						
						if (output[j] > outTop) {
							outTop = output[j];
							jTop = j;
						}

					}

					try{
						board[(int) jTop] = 1;
					}catch(Exception e){
						for(double d:net.toDoubleArry()){
							System.out.println(d + "  " + Double.isNaN(d));
						}
						System.exit(0);
					}

				}

				moves++;

			}

			wins += gamestate(board);

		}

		return wins;

	}

	public int gamestate(double[] board) {

		double win = 0;

		if (((board[0] == board[1]) && (board[1] == board[2])) || ((board[0] == board[4]) && (board[4] == board[8])) || ((board[0] == board[3]) && (board[3] == board[6]))) {

			if (board[0] != 0) win = board[0];

		} else if (((board[1] == board[4]) && (board[4] == board[7])) || ((board[3] == board[4]) && (board[4] == board[5])) || ((board[2] == board[4]) && (board[4] == board[6]))) {

			if (board[4] != 0) win = board[4];

		} else if (((board[2] == board[5]) && (board[5] == board[8])) || ((board[6] == board[7]) && (board[7] == board[8]))) {

			if (board[8] != 0) win = board[8];

		}

		return (int) win;

	}

	public int numMoves(double[] board) {

		int num = 0;

		for (double i : board)
			if (i == 0) num++;

		return num;

	}
	
	public Network[] getNetworks(){
		
		return networks;
		
	}
	
	public double[] splitBoard(double[] board) {
		
		double[] out = new double[18];
		
		for(int i = 0; i < board.length; i++) {
			if(board[i] == 1)out[i] = 1;
			else if(board[i] == -1)out[i+9] = 1;
		}
		
		return out;
		
	}

}
