package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import network.Network;
import network.NetworkControler;

public class ImportNet {
	
	public static NetworkControler readGeneticNet(String path) throws FileNotFoundException{
		
		Scanner in = new Scanner(new File(path + ".net"));
		
		NetworkControler output = new NetworkControler(Integer.parseInt(in.nextLine()));
		
		for(int i = 0; i < output.getNetworks().length/3; i++){
			output.getNetworks()[i] = new Network(in.nextLine());
		}
		
		in.close();
		
		return output;
		
	}
	
	public static String readNet(String path) throws FileNotFoundException{
		
		Scanner in = new Scanner(new File(path + ".net"));
		
		String out = in.nextLine();
		
		in.close();
		
		return out;
		
	}
	
}
