package io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import network.Network;
import network.NetworkControler;

public class ExportNet {
	
	public static void saveGeneticNet(String path, NetworkControler net) throws IOException{
		
		Writer writer;
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + ".net"), "utf-8"));
		
		writer.write(net.getNetworks().length + "\n");
		
		for(int i = 0; i < net.getNetworks().length/3; i++){
			saveNet(path, net.getNetworks()[i], writer);
		}
		
		writer.flush();
		
		writer.close();
		
	}
	
	public static void saveNet(String path, Network net) throws IOException{
		
		Writer writer;
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + ".net"), "utf-8"));
		
		writer.write(net.toString() + "\n");
		
		writer.flush();
		
		writer.close();
		
	}
	
	private static void saveNet(String path, Network net, Writer writer) throws IOException{
		
		writer.write(net.toString() + "\n");
		
	}
	
}
